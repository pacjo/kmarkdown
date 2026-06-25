package nodomain.pacjo.kmarkdown.elements

import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

abstract class Element {
    abstract fun render(config: MarkdownRendererConfig = MarkdownRendererConfig()): String
}
