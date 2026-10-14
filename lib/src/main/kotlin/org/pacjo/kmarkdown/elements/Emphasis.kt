package org.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import org.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents emphasised inline text.
 */
class Emphasis(
    val content: KtList<Element>
) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        val inner = content.joinToString("") { it.render(config) }
        val d = config.emphasisDelimiter
        return "$d$inner$d"
    }
}
