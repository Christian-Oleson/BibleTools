package com.oleson

import com.oleson.services.EsvService
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import java.io.File

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
    name = "bibletools",
    description = ["Fetch Bible passages from the ESV API and convert them to Markdown format."],
    mixinStandardHelpOptions = true
)
class BibletoolsCommand @Inject constructor(private val esvService: EsvService) : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["Enable verbose output for debugging."])
    private var verbose: Boolean = false

    @Option(
        names = ["-p", "--passage"],
        description = ["Bible passage to fetch (e.g., 'John 3:16', 'Romans 8:28-39')."],
        required = true
    )
    private lateinit var passage: String

    @Option(
        names = ["-k", "--api-key"],
        description = ["ESV API key. Can also be set via ESV_API_KEY environment variable."]
    )
    private var apiKey: String? = null

    @Option(
        names = ["-o", "--output"],
        description = ["Output file path for the Markdown. If not specified, prints to console."]
    )
    private var outputFile: String? = null

    override fun run() {
        val resolvedApiKey = resolveApiKey()
        if (resolvedApiKey == null) {
            System.err.println("Error: ESV API key is required.")
            System.err.println("Provide it via --api-key option or ESV_API_KEY environment variable.")
            return
        }

        if (verbose) {
            println("Fetching passage: $passage")
            println("API key: ${resolvedApiKey.take(4)}****")
            outputFile?.let { println("Output file: $it") }
        }

        try {
            val markdownText = esvService.getTextPassage(resolvedApiKey, passage)

            if (outputFile != null) {
                File(outputFile!!).writeText(markdownText)
                println("Markdown written to: $outputFile")
            } else {
                println(markdownText)
            }
        } catch (e: Exception) {
            System.err.println("Error fetching passage: ${e.message}")
            if (verbose) {
                e.printStackTrace()
            }
        }
    }

    private fun resolveApiKey(): String? {
        return apiKey ?: System.getenv("ESV_API_KEY")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(BibletoolsCommand::class.java, *args)
        }
    }
}
