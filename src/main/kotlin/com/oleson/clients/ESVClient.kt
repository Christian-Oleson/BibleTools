package com.oleson.clients

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.QueryValue

@Client("https://api.esv.org")
interface EsvApiClient {

    @Get("/v3/passage/text/")
    fun getTextPassage(
        @Header("Authorization") authorization: String,
        @Header("Accept") acceptType: String,
        @QueryValue("q") passageQuery: String,
        @QueryValue("include-passage-references") includePassageReferences: Boolean = true,
        @QueryValue("include-verse-numbers") includeVerseNumbers: Boolean = true,
        @QueryValue("include-first-verse-numbers") includeFirstVerseNumbers: Boolean = true,
        @QueryValue("include-footnotes") includeFootnotes: Boolean = true,
        @QueryValue("include-footnote-body") includeFootnoteBody: Boolean = true,
        @QueryValue("include-headings") includeHeadings: Boolean = true,
        @QueryValue("include-short-copyright") includeShortCopyright: Boolean = true,
        @QueryValue("include-copyright") includeCopyright: Boolean = false,
        @QueryValue("inline-styles") inlineStyles: Boolean = false,
        @QueryValue("wrapping-div") wrappingDiv: Boolean = false,
        @QueryValue("div-classes") divClasses: String = "passage",
        @QueryValue("paragraph-tag") paragraphTag: String = "p",
        @QueryValue("include-book-titles") includeBookTitles: Boolean = false,
        @QueryValue("include-verse-anchors") includeVerseAnchors: Boolean = false,
        @QueryValue("include-chapter-numbers") includeChapterNumbers: Boolean = true,
        @QueryValue("include-crossrefs") includeCrossrefs: Boolean = false,
        @QueryValue("include-subheadings") includeSubheadings: Boolean = true,
        @QueryValue("include-surrounding-chapters") includeSurroundingChapters: Boolean = false,
        @QueryValue("include-surrounding-chapters-below") includeSurroundingChaptersBelow: String = "smart",
        @QueryValue("link-url") linkUrl: String = "",
        @QueryValue("crossref-url") crossRefUrl: String = "",
        @QueryValue("preface-url") prefaceUrl: String = "https://www.esv.org/preface/",
        @QueryValue("include-audio-link") includeAudioLink: Boolean = true,
        @QueryValue("attach-audio-link-to") attachAudioLinkTo: String = "passage"
    ): String

    @Get("/v3/passage/html/")
    fun getHTMLPassage(
        @Header("Authorization") authorization: String,
        @Header("Accept") acceptType: String,
        @QueryValue("q") passageQuery: String,
        @QueryValue("include-passage-references") includePassageReferences: Boolean = true,
        @QueryValue("include-verse-numbers") includeVerseNumbers: Boolean = true,
        @QueryValue("include-first-verse-numbers") includeFirstVerseNumbers: Boolean = true,
        @QueryValue("include-footnotes") includeFootnotes: Boolean = true,
        @QueryValue("include-footnote-body") includeFootnoteBody: Boolean = true,
        @QueryValue("include-headings") includeHeadings: Boolean = true,
        @QueryValue("include-short-copyright") includeShortCopyright: Boolean = true,
        @QueryValue("include-copyright") includeCopyright: Boolean = false,
        @QueryValue("inline-styles") inlineStyles: Boolean = false,
        @QueryValue("wrapping-div") wrappingDiv: Boolean = false,
        @QueryValue("div-classes") divClasses: String = "passage",
        @QueryValue("paragraph-tag") paragraphTag: String = "p",
        @QueryValue("include-book-titles") includeBookTitles: Boolean = false,
        @QueryValue("include-verse-anchors") includeVerseAnchors: Boolean = false,
        @QueryValue("include-chapter-numbers") includeChapterNumbers: Boolean = true,
        @QueryValue("include-crossrefs") includeCrossrefs: Boolean = false,
        @QueryValue("include-subheadings") includeSubheadings: Boolean = true,
        @QueryValue("include-surrounding-chapters") includeSurroundingChapters: Boolean = false,
        @QueryValue("include-surrounding-chapters-below") includeSurroundingChaptersBelow: String = "smart",
        @QueryValue("link-url") linkUrl: String = "",
        @QueryValue("crossref-url") crossRefUrl: String = "",
        @QueryValue("preface-url") prefaceUrl: String = "https://www.esv.org/preface/",
        @QueryValue("include-audio-link") includeAudioLink: Boolean = true,
        @QueryValue("attach-audio-link-to") attachAudioLinkTo: String = "passage"
    ): String
}
