package com.oleson

import com.oleson.services.ConfigService
import com.oleson.services.EsvService
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import java.io.File

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
    name = "bibletools",
    description = ["Fetch Bible passages from the ESV API and convert them to Markdown format."],
    mixinStandardHelpOptions = true,
    subcommands = [ConfigCommand::class]
)
class BibletoolsCommand @Inject constructor(
    private val esvService: EsvService,
    private val configService: ConfigService
) : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["Enable verbose output for debugging."])
    private var verbose: Boolean = false

    @Option(
        names = ["-p", "--passage"],
        description = ["Bible passage to fetch (e.g., 'John 3:16', 'Romans 8:28-39')."]
    )
    private var passage: String? = null

    @Option(
        names = ["-k", "--api-key"],
        description = ["ESV API key. Can also be set via 'bibletools config --set-key' or ESV_API_KEY env var."]
    )
    private var apiKey: String? = null

    @Option(
        names = ["-o", "--output"],
        description = ["Output file path for the Markdown. If not specified, prints to console."]
    )
    private var outputFile: String? = null

    override fun run() {
        if (passage == null) {
            System.err.println("Error: Passage is required. Use -p or --passage to specify a Bible passage.")
            System.err.println("Example: bibletools -p \"John 3:16\"")
            System.err.println("Use --help for more options.")
            return
        }

        val resolvedApiKey = resolveApiKey()
        if (resolvedApiKey == null) {
            System.err.println("Error: ESV API key is required.")
            System.err.println("Set it using: bibletools config --set-key YOUR_API_KEY")
            System.err.println("Or provide via --api-key option or ESV_API_KEY environment variable.")
            return
        }

        if (verbose) {
            println("Fetching passage: $passage")
            println("API key: ${resolvedApiKey.take(4)}****")
            outputFile?.let { println("Output file: $it") }
        }

        try {
            val markdownText = esvService.getTextPassage(resolvedApiKey, passage!!)

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
        return apiKey
            ?: System.getenv("ESV_API_KEY")
            ?: configService.getApiKey()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(BibletoolsCommand::class.java, *args)
        }
    }
}

@Command(
    name = "config",
    description = ["Manage BibleTools configuration."],
    mixinStandardHelpOptions = true
)
class ConfigCommand @Inject constructor(private val configService: ConfigService) : Runnable {

    @Option(
        names = ["--set-key"],
        description = ["Set the ESV API key."]
    )
    private var setKey: String? = null

    @Option(
        names = ["--show"],
        description = ["Show current configuration."]
    )
    private var show: Boolean = false

    override fun run() {
        when {
            setKey != null -> {
                configService.setApiKey(setKey!!)
                println("API key saved to: ${configService.getConfigPath()}")
            }
            show -> {
                val apiKey = configService.getApiKey()
                println("Config file: ${configService.getConfigPath()}")
                if (apiKey != null) {
                    println("API key: ${apiKey.take(4)}${"*".repeat(apiKey.length - 4)}")
                } else {
                    println("API key: not set")
                }
            }
            else -> {
                println("Use --set-key to set API key or --show to view config.")
                println("Example: bibletools config --set-key YOUR_API_KEY")
            }
        }
    }
}
