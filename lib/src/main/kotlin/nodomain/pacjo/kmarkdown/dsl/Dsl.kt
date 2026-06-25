package nodomain.pacjo.kmarkdown.dsl

import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig
import nodomain.pacjo.kmarkdown.elements.BlockQuote
import nodomain.pacjo.kmarkdown.elements.Code
import nodomain.pacjo.kmarkdown.elements.Element
import nodomain.pacjo.kmarkdown.elements.Emphasis
import nodomain.pacjo.kmarkdown.elements.Heading
import nodomain.pacjo.kmarkdown.elements.Html
import nodomain.pacjo.kmarkdown.elements.Image
import nodomain.pacjo.kmarkdown.elements.Language
import nodomain.pacjo.kmarkdown.elements.Link
import nodomain.pacjo.kmarkdown.elements.ListItem
import nodomain.pacjo.kmarkdown.elements.Paragraph
import nodomain.pacjo.kmarkdown.elements.Strong
import nodomain.pacjo.kmarkdown.elements.Text
import nodomain.pacjo.kmarkdown.elements.ThematicBreak
import nodomain.pacjo.kmarkdown.elements.List as MdList

class BlockBuilder(private val depth: Int = 0) {
    internal val blocks = mutableListOf<Element>()

    fun paragraph(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        blocks.add(Paragraph(b.elements.toList()))
    }

    /** Heading at the current nesting level (depth + 1). */
    fun heading(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        blocks.add(Heading(depth + 1, b.elements.toList()))
    }

    /** Heading with nested body content. The heading is one level deeper than a plain [heading] at this depth. */
    fun section(heading: InlineBuilder.() -> Unit, body: BlockBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.heading()
        blocks.add(Heading(depth + 2, b.elements.toList()))
        val inner = BlockBuilder(depth + 1)
        inner.body()
        blocks.addAll(inner.blocks)
    }

    /** Shorthand for a section with a plain-text heading. */
    fun section(heading: String, body: BlockBuilder.() -> Unit) {
        section(heading = { +heading }, body = body)
    }

    fun codeBlock(content: String, language: Language? = null) {
        blocks.add(Code.Block(content, language))
    }

    fun thematicBreak() {
        blocks.add(ThematicBreak())
    }

    fun html(content: String) {
        blocks.add(Html(content))
    }

    fun blockQuote(builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        blocks.add(BlockQuote(b.blocks.toList()))
    }

    fun bulletList(builder: BulletListBuilder.() -> Unit) {
        val b = BulletListBuilder(depth)
        b.builder()
        blocks.add(MdList.Bullet(b.items.toList()))
    }

    fun orderedList(startNumber: Int = 1, builder: OrderedListBuilder.() -> Unit) {
        val b = OrderedListBuilder(depth)
        b.startNumber = startNumber
        b.builder()
        blocks.add(MdList.Ordered(b.items.toList(), startNumber))
    }
}

class InlineBuilder {
    internal val elements = mutableListOf<Element>()

    operator fun String.unaryPlus() {
        elements.add(Text(this))
    }

    fun emphasis(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        elements.add(Emphasis(b.elements.toList()))
    }

    fun strong(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        elements.add(Strong(b.elements.toList()))
    }

    fun code(value: String) {
        elements.add(Code.Inline(value))
    }

    fun link(url: String, text: String, title: String? = null) {
        elements.add(Link.Url(text, url, title))
    }

    fun image(alt: String, url: String, title: String? = null) {
        elements.add(Image.Inline(alt, url, title))
    }

    fun autolink(uri: String) {
        elements.add(Link.Autolink(uri))
    }

    fun html(content: String) {
        elements.add(Html(content))
    }
}

class BulletListBuilder(private val depth: Int) {
    internal val items = mutableListOf<ListItem>()

    fun item(builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        items.add(ListItem(b.blocks.toList()))
    }
}

class OrderedListBuilder(private val depth: Int) {
    internal val items = mutableListOf<ListItem>()
    var startNumber: Int = 1

    fun item(builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        items.add(ListItem(b.blocks.toList()))
    }
}

fun markdown(builder: BlockBuilder.() -> Unit): List<Element> {
    val b = BlockBuilder()
    b.builder()
    return b.blocks.toList()
}

fun List<Element>.render(config: MarkdownRendererConfig = MarkdownRendererConfig()): String {
    val sep = "\n".repeat(config.blockSpacing + 1)
    return joinToString(sep) { it.render(config) }
}
