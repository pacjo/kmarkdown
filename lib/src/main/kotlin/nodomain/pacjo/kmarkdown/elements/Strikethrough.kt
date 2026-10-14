package nodomain.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

class Strikethrough(val content: KtList<Element>) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        val inner = content.joinToString("") { it.render(config) }
        return "~~$inner~~"
    }
}
