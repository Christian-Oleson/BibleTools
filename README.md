# Bible Tools

A CLI tool to fetch Bible passages from the ESV API and convert them to Markdown format.

## Requirements

- Java 21+
- ESV API key (get one at https://api.esv.org/)

## Installation

```bash
./gradlew build
```

This creates a runnable JAR at `build/libs/bibletools-*-all.jar`.

## Usage

```bash
# Basic usage (prints to console)
java -jar build/libs/bibletools-0.1-all.jar -p "John 3:16" -k YOUR_API_KEY

# Save to file
java -jar build/libs/bibletools-0.1-all.jar -p "Romans 8:28-39" -k YOUR_API_KEY -o passage.md

# Using environment variable for API key
export ESV_API_KEY=your_api_key_here
java -jar build/libs/bibletools-0.1-all.jar -p "Psalm 23"

# Verbose mode
java -jar build/libs/bibletools-0.1-all.jar -p "John 3:16" -k YOUR_API_KEY -v
```

### CLI Options

| Option | Description |
|--------|-------------|
| `-p, --passage` | Bible passage to fetch (required) |
| `-k, --api-key` | ESV API key (or set `ESV_API_KEY` env var) |
| `-o, --output` | Output file path for Markdown |
| `-v, --verbose` | Enable verbose output |
| `-h, --help` | Show help message |

## Development

```bash
# Run tests
./gradlew test

# Run directly with Gradle
./gradlew run --args="-p 'John 3:16' -k YOUR_API_KEY"
```

---

## Micronaut 4.0.6 Documentation

- [User Guide](https://docs.micronaut.io/4.0.6/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.0.6/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.0.6/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Feature mockito documentation

- [https://site.mockito.org](https://site.mockito.org)


## Feature http-client-jdk documentation

- [Micronaut HTTP Client Jdk documentation](https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient)

- [https://openjdk.org/groups/net/httpclient/intro.html](https://openjdk.org/groups/net/httpclient/intro.html)


## Feature ksp documentation

- [Micronaut Kotlin Symbol Processing (KSP) documentation](https://docs.micronaut.io/latest/guide/#kotlin)

- [https://kotlinlang.org/docs/ksp-overview.html](https://kotlinlang.org/docs/ksp-overview.html)


## Feature reactor documentation

- [Micronaut Reactor documentation](https://micronaut-projects.github.io/micronaut-reactor/snapshot/guide/index.html)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature cache-caffeine documentation

- [Micronaut Caffeine Cache documentation](https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html)

- [https://github.com/ben-manes/caffeine](https://github.com/ben-manes/caffeine)

