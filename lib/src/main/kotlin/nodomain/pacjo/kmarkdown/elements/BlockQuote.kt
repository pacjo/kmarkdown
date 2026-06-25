package nodomain.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a block quote, which can contain any sequence of block-level elements.
 */
class BlockQuote(
    val blocks: KtList<Element>
) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        val sep = "\n".repeat(config.blockSpacing + 1)
        return blocks.joinToString(sep) { block ->
            block.render(config).lines().joinToString("\n") { line ->
                "> $line"
            }
        }
    }
}
