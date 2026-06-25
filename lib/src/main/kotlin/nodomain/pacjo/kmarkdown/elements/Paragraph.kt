package nodomain.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a paragraph containing inline elements.
 */
class Paragraph(
    val content: KtList<Element>
) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        return content.joinToString("") { it.render(config) }
    }
}
