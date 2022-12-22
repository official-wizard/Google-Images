package dev.officialwizard.google.images

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder

object GoogleImages {
    private const val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36"
    private const val googleSearchQuery = "https://www.google.com/search"

    fun search(query: String, safeSearch: Boolean): List<String> {
        return getImagesSrc(createConnection(query, safeSearch).get())
    }

    private fun getImagesSrc(document: Document): List<String> {
        return document.getElementsByAttributeValue("class", "rg_i Q4LuWd")
            .filter { node -> node.hasAttr("data-src") && node.attr("data-src").isNotEmpty() }
            .map { it.attr("data-src") }.toList()
    }

    private fun createConnection(query: String, safeSearch: Boolean): Connection {
        return Jsoup.connect(createQuery(query, safeSearch))
            .userAgent(userAgent)
    }

    private fun createQuery(query: String, safeSearch: Boolean): String {
        var result = "$googleSearchQuery?q=${URLEncoder.encode(query, "UTF-8")}&tbm=isch"

        if (safeSearch) {
            result += "&safe=active"
        }

        return result
    }
}