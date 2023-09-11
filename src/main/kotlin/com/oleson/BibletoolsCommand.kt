package com.oleson

import com.oleson.services.EsvService
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "bibletools", description = ["..."],
        mixinStandardHelpOptions = true)
class BibletoolsCommand @Inject constructor(private val esvService: EsvService) : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose : Boolean = false

    override fun run() {
        if (verbose) {
            println("Hi!")
        }

        val apiKey = "" // Replace with your ESV API key
        val passageQuery = "John 3" // Replace with your desired passage query
        val markdownText = esvService.getTextPassage(apiKey, passageQuery)
        println(markdownText)
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.run(BibletoolsCommand::class.java, *args)
        }
    }
}
