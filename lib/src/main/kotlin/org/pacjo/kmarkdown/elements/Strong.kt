package org.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import org.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents strong inline text.
 */
class Strong(
    val content: KtList<Element>
) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        val inner = content.joinToString("") { it.render(config) }
        val d = config.emphasisDelimiter
        val dd = "$d$d"
        return "$dd$inner$dd"
    }
}
