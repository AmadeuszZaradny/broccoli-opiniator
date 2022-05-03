package pl.ama

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.OkHttpClient
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

        broccoliOpinionsFacade.rateAllDiets(args[0], args[1], 4, "Wszystko w porządku :)")
    }
}