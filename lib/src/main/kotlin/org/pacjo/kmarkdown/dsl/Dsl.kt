package org.pacjo.kmarkdown.dsl

import org.pacjo.kmarkdown.config.MarkdownRendererConfig
import org.pacjo.kmarkdown.elements.Alignment
import org.pacjo.kmarkdown.elements.BlockQuote
import org.pacjo.kmarkdown.elements.Code
import org.pacjo.kmarkdown.elements.Element
import org.pacjo.kmarkdown.elements.Emphasis
import org.pacjo.kmarkdown.elements.Heading
import org.pacjo.kmarkdown.elements.Html
import org.pacjo.kmarkdown.elements.Image
import org.pacjo.kmarkdown.elements.Language
import org.pacjo.kmarkdown.elements.Link
import org.pacjo.kmarkdown.elements.ListItem
import org.pacjo.kmarkdown.elements.Paragraph
import org.pacjo.kmarkdown.elements.Strong
import org.pacjo.kmarkdown.elements.Strikethrough
import org.pacjo.kmarkdown.elements.Table
import org.pacjo.kmarkdown.elements.TableColumn
import org.pacjo.kmarkdown.elements.TableRow
import org.pacjo.kmarkdown.elements.Text
import org.pacjo.kmarkdown.elements.ThematicBreak
import org.pacjo.kmarkdown.elements.List as MdList

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

    fun table(builder: TableBuilder.() -> Unit) {
        val b = TableBuilder()
        b.builder()
        blocks.add(Table(b.columns.toList(), b.rows.toList()))
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

    fun strikethrough(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        elements.add(Strikethrough(b.elements.toList()))
    }
}

class BulletListBuilder(private val depth: Int) {
    internal val items = mutableListOf<ListItem>()

    fun item(builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        items.add(ListItem(b.blocks.toList()))
    }

    fun taskItem(checked: Boolean = false, builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        items.add(ListItem(b.blocks.toList(), checked = checked))
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

    fun taskItem(checked: Boolean = false, builder: BlockBuilder.() -> Unit) {
        val b = BlockBuilder(depth)
        b.builder()
        items.add(ListItem(b.blocks.toList(), checked = checked))
    }
}

class TableBuilder {
    internal val columns = mutableListOf<TableColumn>()
    internal val rows = mutableListOf<TableRow>()

    fun column(header: InlineBuilder.() -> Unit, alignment: Alignment = Alignment.LEFT) {
        val b = InlineBuilder()
        b.header()
        columns.add(TableColumn(b.elements.toList(), alignment))
    }

    fun column(header: String, alignment: Alignment = Alignment.LEFT) {
        column(header = { +header }, alignment = alignment)
    }

    fun row(builder: RowBuilder.() -> Unit) {
        val b = RowBuilder()
        b.builder()
        rows.add(TableRow(b.cells.toList()))
    }
}

class RowBuilder {
    internal val cells = mutableListOf<List<Element>>()

    fun cell(builder: InlineBuilder.() -> Unit) {
        val b = InlineBuilder()
        b.builder()
        cells.add(b.elements.toList())
    }

    fun cell(text: String) {
        cell { +text }
    }
}

fun markdown(builder: BlockBuilder.() -> Unit): List<Element> {
    val b = BlockBuilder()
    b.builder()
    return b.blocks.toList()
}

fun List<Element>.renderAll(config: MarkdownRendererConfig = MarkdownRendererConfig()): String {
    val self = this
    return buildString {
        for (i in self.indices) {
            if (i > 0) {
                val curr = self[i]
                val n = if (curr is Heading) config.newlinesNumBeforeSectionHeader
                else config.newlinesNumAfterSectionHeader
                repeat(n + 1) { append('\n') }
            }
            append(self[i].render(config))
        }
    }
}
