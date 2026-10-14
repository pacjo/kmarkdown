package org.pacjo.kmarkdown.elements

import org.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a thematic break (horizontal rule).
 */
class ThematicBreak : Element() {
    override fun render(config: MarkdownRendererConfig): String = "---"
}
