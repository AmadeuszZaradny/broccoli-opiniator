package pl.ama.htmlprocessing

import org.jsoup.Jsoup
import org.jsoup.select.Elements

class HtmlProcessor {

    companion object {
        fun String.extractFormHiddenElements(formName: String): List<Elements> =
            Jsoup.parse(this).body()
                .getElementsByClass(formName).toList()
                .map { form -> form.getElementsByAttributeValue("type", "hidden") }

        fun Elements.toNameValueMap(): Map<String, String> =
            this.associate { element -> element.attr("name") to element.attr("value") }
    }

}