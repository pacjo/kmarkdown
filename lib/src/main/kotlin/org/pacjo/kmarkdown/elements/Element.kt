package org.pacjo.kmarkdown.elements

import org.pacjo.kmarkdown.config.MarkdownRendererConfig

abstract class Element {
    abstract fun render(config: MarkdownRendererConfig = MarkdownRendererConfig()): String
}
