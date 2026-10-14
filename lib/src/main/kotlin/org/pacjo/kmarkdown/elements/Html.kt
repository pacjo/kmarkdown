package org.pacjo.kmarkdown.elements

import org.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a raw HTML element.
 */
class Html(val content: String) : Element() {
    override fun render(config: MarkdownRendererConfig): String = content
}
