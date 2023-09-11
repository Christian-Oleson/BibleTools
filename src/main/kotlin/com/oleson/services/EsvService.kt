package com.oleson.services

import com.fasterxml.jackson.annotation.JsonProperty
import com.oleson.clients.EsvApiClient
import io.github.furstenheim.CopyDown
import io.github.furstenheim.Options
import io.github.furstenheim.OptionsBuilder
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.QueryValue
import io.micronaut.serde.ObjectMapper
import io.micronaut.serde.annotation.Serdeable.Deserializable
import jakarta.inject.Inject


class EsvService @Inject constructor(private val esvApiClient: EsvApiClient, private val objectMapper: ObjectMapper) {

    fun getTextPassage(
    @QueryValue apiKey: String,
    @QueryValue passageQuery: String
    ): String {
        val html = esvApiClient.getHTMLPassage("Token $apiKey", MediaType.TEXT_PLAIN, passageQuery)

        val responseHtml = objectMapper.readValue(html, EsvApiResponse::class.java)
        return convert(responseHtml.passages)
    }

    private fun convert(passages: List<String>) : String {

        val optionsBuilder = OptionsBuilder.anOptions()
        val options: Options = optionsBuilder
            .withBr("-") // more options
            .build()


        val converter = CopyDown(options)
        val sb = StringBuilder()
        passages.forEach { sb.appendLine(converter.convert(convertToMarkdownHeading(it)).replace("**", "")) }
        return sb.toString()
    }

    private fun convertToMarkdownHeading(input: String): String {
        val regex = """\[(\d+)\]""".toRegex()
        return regex.replace(input) { result ->
            val number = result.groupValues[1]
            "\n\n## $number\n\n"
        }
    }
}

@Introspected
@Deserializable
data class EsvApiResponse(
    val query: String,
    val canonical: String,
    val parsed: List<List<Long>>,
    @JsonProperty("passage_meta")
    val passageMeta: List<PassageMeta>,
    val passages: List<String>
)

@Introspected
@Deserializable
data class PassageMeta(
    val canonical: String,
    @JsonProperty("chapter_start")
    val chapterStart: List<Long>,
    @JsonProperty("chapter_end")
    val chapterEnd: List<Long>,
    @JsonProperty("prev_verse")
    val prevVerse: Long,
    @JsonProperty("next_verse")
    val nextVerse: Long,
    @JsonProperty("prev_chapter")
    val prevChapter: List<Long>,
    @JsonProperty("next_chapter")
    val nextChapter: List<Long>
)