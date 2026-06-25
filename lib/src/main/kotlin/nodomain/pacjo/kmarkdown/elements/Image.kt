package nodomain.pacjo.kmarkdown.elements

import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents an image element.
 */
sealed class Image : Element() {

    class Inline(
        val alt: String,
        val destination: String,
        val title: String? = null
    ) : Image() {
        override fun render(config: MarkdownRendererConfig): String {
            return "![$alt]($destination${title?.let { " \"$it\"" }})"
        }
    }

    class FullReference(
        val alt: String,
        val label: String
    ) : Image() {
        override fun render(config: MarkdownRendererConfig): String = "![$alt][$label]"
    }

    class CollapsedReference(
        val label: String
    ) : Image() {
        override fun render(config: MarkdownRendererConfig): String = "![$label][]"
    }

    class ShortcutReference(
        val label: String
    ) : Image() {
        override fun render(config: MarkdownRendererConfig): String = "![$label]"
    }
}
