package pl.ama.client.broccoli

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class BroccoliClient(
    private val okHttpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    private val domainUrl: String
) {

    fun authorize(email: String, password: String): BroccoliSession {
        val request = Request.Builder()
            .url(domainUrl + AUTHORIZE_PATH)
            .defaultHeaders()
            .sessionCookie("ehlcqbq3id6hiqs7eigqtaqffu") // I have to download session id on the start
            .post(
                FormBody.Builder()
                    .add("login[username]", email)
                    .add("login[password]", password)
                    .add("form_key", "JBYogUnAlARmu2nW") // you should download form id
                    .build()
            )
            .build()

        return BroccoliSession(
            sessionId = okHttpClient.newCall(request)
                .execute()
                .getSessionId()
        )
    }

    fun getUserData(session: BroccoliSession): UserData {
        val request = Request.Builder()
            .url(domainUrl + USER_DATA_PATH)
            .defaultHeaders()
            .sessionCookie(session.sessionId)
            .build()

        return okHttpClient.newCall(request).execute().toObject(UserData::class.java)
    }

    fun getDietsToRateFormHtml(session: BroccoliSession): String {
        val request = Request.Builder()
            .url(domainUrl + DIETS_TO_RATE_PATH)
            .defaultHeaders()
            .sessionCookie(session.sessionId)
            .build()

        return okHttpClient.newCall(request)
            .execute()
            .body!!
            .string()
    }

    fun rateDiet(session: BroccoliSession, dietToRate: DietToRate, stars: Int, note: String) {
        val bodyJson = objectMapper.writeValueAsString(dietToRate.toRateDietRequest(stars, note))

        val request = Request.Builder()
            .url(domainUrl + RATE_DIET_PATH)
            .header("content-type", "application/json")
            .defaultHeaders()
            .sessionCookie(session.sessionId)
            .post(bodyJson.toRequestBody(JSON))
            .build()

        okHttpClient.newCall(request).execute()
    }

    private fun Request.Builder.sessionCookie(sessionId: String) = this.header("cookie", "$SESSION_FIELD_NAME=$sessionId")

    private fun Request.Builder.defaultHeaders() = this.header("x-requested-with", "XMLHttpRequest")

    private fun DietToRate.toRateDietRequest(stars: Int, note: String) =
        RateDietRequest(
            opinion = RateDietRequest.Opinion(
                menuId = this.menuId,
                date = this.date,
                stars = stars,
                note = note,
                productId = productId,
                mealTimeId = mealTimeId,
                orderItemId = orderItemId
            )
        )

    private fun <T> Response.toObject(responseType: Class<T>) = objectMapper
        .readValue(this.body!!.string(), responseType)

    private fun Response.getSessionId() =
        this.headers
            .values("Set-Cookie")[0]
            .split(";")
            .find { string -> string.contains(SESSION_FIELD_NAME) }
            ?.split("=")
            ?.last() ?: throw CouldNotFindSessionIdException(this.body!!)

    companion object {
        private const val AUTHORIZE_PATH = "/customer/account/loginPost/referer/aHR0cHM6Ly9kaWV0eW9kYnJva3VsYS5wbC9jdXN0b21lci9hY2NvdW50L2luZGV4Lw%2C%2C/"
        private const val USER_DATA_PATH = "/customer/section/load"
        private const val DIETS_TO_RATE_PATH = "/customer/diets/rate/"
        private const val RATE_DIET_PATH = "/rest/V1/customer/menu-diet/save-opinion"
        private const val SESSION_FIELD_NAME = "PHPSESSID"
        private val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }
}
