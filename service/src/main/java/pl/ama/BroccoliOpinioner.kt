package pl.ama

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import pl.ama.domain.BroccoliConfig
import pl.ama.domain.BroccoliOpinionsFacade
import pl.ama.domain.BroccoliOpinionsFacadeImpl

object BroccoliOpinioner {

    @JvmStatic
    fun main(args: Array<String>) {

        val objectMapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        val broccoliOpinionsFacade: BroccoliOpinionsFacade = BroccoliOpinionsFacadeImpl(
            httpClient = OkHttpClient(),
            objectMapper = objectMapper,
            config = BroccoliConfig(
                url = "https://dietyodbrokula.pl"
            )
        )

        val email = args.getOrElse(0) { System.getenv("BROCCOLI_EMAIL") }
        val password = args.getOrElse(1) { System.getenv("BROCCOLI_PASSWORD") }

        broccoliOpinionsFacade.rateAllDiets(email, password, 4, "Wszystko w porządku :)")
    }
}
