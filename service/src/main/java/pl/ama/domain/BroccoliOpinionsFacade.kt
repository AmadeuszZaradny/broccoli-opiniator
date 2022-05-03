package pl.ama.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.OkHttpClient
import org.jsoup.select.Elements
import pl.ama.client.broccoli.BroccoliClient
import pl.ama.client.broccoli.DietToRate
import pl.ama.htmlprocessing.HtmlProcessor.Companion.extractFormHiddenElements
import pl.ama.htmlprocessing.HtmlProcessor.Companion.toNameValueMap

interface BroccoliOpinionsFacade {
    fun rateAllDiets(email: String, password: String, stars: Int, message: String)
}

data class BroccoliConfig(
    val url: String
)

class BroccoliOpinionsFacadeImpl(
    private val httpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    private val config: BroccoliConfig
): BroccoliOpinionsFacade {

    private val client = BroccoliClient(httpClient, objectMapper, config.url)

    override fun rateAllDiets(email: String, password: String, stars: Int, message: String) {
        val session = client.authorize(email, password)
        println("User data before opinions: " + client.getUserData(session))

        val dietsToRate = client.getDietsToRateFormHtml(session).getDietsToRateFromRateDietFromHtml()
        println("Diets to rate: $dietsToRate")

        dietsToRate.forEach { dietToRate: DietToRate? -> client.rateDiet(session, dietToRate!!, stars, message) }
        println("User data after opinions: " + client.getUserData(session))
    }

    private fun String.getDietsToRateFromRateDietFromHtml() =
        this.extractFormHiddenElements(DIET_FORM_NAME)
            .map { it.toDietToRate() }


    private fun Elements.toDietToRate(): DietToRate {
        val namesToValues  = this.toNameValueMap()
        return DietToRate(
            menuId = namesToValues[MENU_ID_NAME]?.toInt() ?: throw CouldNotExtractFormFieldFromHtml(MENU_ID_NAME, this),
            date = namesToValues[DATE_NAME] ?: throw CouldNotExtractFormFieldFromHtml(DATE_NAME, this),
            productId = namesToValues[PRODUCT_ID_NAME]?.toInt() ?: throw CouldNotExtractFormFieldFromHtml(PRODUCT_ID_NAME, this),
            mealTimeId = namesToValues[MEAL_TIME_ID_NAME]?.toInt() ?: throw CouldNotExtractFormFieldFromHtml(MEAL_TIME_ID_NAME, this),
            orderItemId = namesToValues[ORDER_ITEM_ID_NAME]?.toInt() ?: throw CouldNotExtractFormFieldFromHtml(ORDER_ITEM_ID_NAME, this)
        )
    }

    companion object {
        private const val DIET_FORM_NAME = "diet-rate__form"
        private const val MENU_ID_NAME = "menu_id"
        private const val DATE_NAME = "date"
        private const val PRODUCT_ID_NAME = "product_id"
        private const val MEAL_TIME_ID_NAME = "meal_time_id"
        private const val ORDER_ITEM_ID_NAME = "order_item_id"

    }
}