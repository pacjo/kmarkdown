plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    // Apply publishing plugin
    alias(libs.plugins.maven.publish)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("org.pacjo.kmarkdown", "kmarkdown", "1.0.0")

    pom {
        name.set("kmarkdown")
        description.set("Kotlin generator for Markdown.")
        inceptionYear.set("2026")
        url.set("https://github.com/pacjo/kmarkdown/")
        licenses {
            license {
                name.set("The GNU General Public License v3.0")
                url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.txt")
            }
        }
        developers {
            developer {
                id.set("pacjo")
                name.set("Kamil Koryciński")
                url.set("https://github.com/pacjo/")
            }
        }
        scm {
            url.set("https://github.com/pacjo/kmarkdown/")
            connection.set("scm:git:git://github.com/pacjo/kmarkdown.git")
            developerConnection.set("scm:git:ssh://git@github.com/pacjo/kmarkdown.git")
        }
    }
}
