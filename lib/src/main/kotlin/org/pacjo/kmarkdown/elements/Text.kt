package org.pacjo.kmarkdown.elements

import org.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents plain inline text content.
 *
 * Used as the leaf node inside paragraphs, headings, emphasis, etc.
 */
class Text(val value: String) : Element() {
    override fun render(config: MarkdownRendererConfig): String = value
}
