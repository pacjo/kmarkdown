package nodomain.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents an ATX heading element.
 */
class Heading(
    val level: Int,
    val content: KtList<Element>
) : Element() {
    init {
        require(level in 1..6) { "level must be between 1 and 6, got $level" }
    }

    override fun render(config: MarkdownRendererConfig): String {
        val inner = content.joinToString("") { it.render(config) }
        return "${"#".repeat(level)} $inner"
    }
}
