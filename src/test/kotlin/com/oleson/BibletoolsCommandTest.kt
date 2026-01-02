package com.oleson

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class BibletoolsCommandTest {

    private lateinit var originalOut: PrintStream
    private lateinit var originalErr: PrintStream
    private lateinit var baos: ByteArrayOutputStream
    private lateinit var errBaos: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        originalOut = System.out
        originalErr = System.err
        baos = ByteArrayOutputStream()
        errBaos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        System.setErr(PrintStream(errBaos))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun testHelpOption() {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        val args = arrayOf("--help")
        PicocliRunner.run(BibletoolsCommand::class.java, ctx, *args)

        val output = baos.toString()
        Assertions.assertTrue(output.contains("bibletools"))
        Assertions.assertTrue(output.contains("--passage"))
        Assertions.assertTrue(output.contains("--api-key"))
        Assertions.assertTrue(output.contains("--output"))

        ctx.close()
    }

    @Test
    fun testMissingPassageShowsError() {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        val args = arrayOf<String>()
        PicocliRunner.run(BibletoolsCommand::class.java, ctx, *args)

        val errOutput = errBaos.toString()
        Assertions.assertTrue(errOutput.contains("Passage is required"))

        ctx.close()
    }

    @Test
    fun testMissingApiKeyShowsError() {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        val args = arrayOf("-p", "John 3:16")
        PicocliRunner.run(BibletoolsCommand::class.java, ctx, *args)

        val errOutput = errBaos.toString()
        Assertions.assertTrue(errOutput.contains("ESV API key is required"))

        ctx.close()
    }

    @Test
    fun testVerboseOutputIncludesPassage() {
        val ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        val args = arrayOf("-v", "-p", "John 3:16", "-k", "test-key")
        // Note: This will fail to fetch since test-key is invalid, but verbose output should show passage
        PicocliRunner.run(BibletoolsCommand::class.java, ctx, *args)

        val output = baos.toString()
        Assertions.assertTrue(output.contains("Fetching passage: John 3:16"))
        Assertions.assertTrue(output.contains("API key: test****"))

        ctx.close()
    }
}
