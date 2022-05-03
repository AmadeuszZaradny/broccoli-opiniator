package pl.ama.domain

import org.jsoup.select.Elements

class CouldNotExtractFormFieldFromHtml(fieldName: String, html: Elements):
    RuntimeException("Could not extract field $fieldName from html $html")