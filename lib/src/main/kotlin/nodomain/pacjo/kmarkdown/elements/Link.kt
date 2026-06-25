package nodomain.pacjo.kmarkdown.elements

import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a link element.
 */
sealed class Link : Element() {

    class ReferenceDefinition(
        val label: String,
        val destination: String,
        val title: String? = null
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String {
            return "[$label]: $destination${title?.let { " \"$it\"" }}"
        }
    }

    class Url(
        val text: String,
        val destination: String,
        val title: String? = null
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String {
            return "[$text]($destination${title?.let { " \"$it\"" }})"
        }
    }

    class Autolink(
        val uri: String
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String = "<$uri>"
    }

    class FullReference(
        val text: String,
        val label: String
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String = "[$text][$label]"
    }

    class CollapsedReference(
        val label: String
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String = "[$label][]"
    }

    class ShortcutReference(
        val label: String
    ) : Link() {
        override fun render(config: MarkdownRendererConfig): String = "[$label]"
    }
}
