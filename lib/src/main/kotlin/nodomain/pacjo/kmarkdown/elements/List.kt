package nodomain.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import nodomain.pacjo.kmarkdown.config.MarkdownRendererConfig

/**
 * Represents a list (ordered or bullet) containing list items.
 */
sealed class List : Element() {
    abstract val items: KtList<ListItem>

    data class Bullet(
        override val items: KtList<ListItem>,
    ) : List() {
        override fun render(config: MarkdownRendererConfig): String {
            val marker = config.listItemMarkers.first()
            val markerStr = "$marker "
            val indentStr = " ".repeat(markerStr.length)
            return renderItems(markerStr, indentStr, config)
        }
    }

    data class Ordered(
        override val items: KtList<ListItem>,
        val startNumber: Int = 1,
    ) : List() {
        init {
            require(startNumber >= 0) { "Start number must be non-negative, got $startNumber" }
        }

        override fun render(config: MarkdownRendererConfig): String {
            return items.mapIndexed { index: Int, item: ListItem ->
                val number = startNumber + index
                val markerStr = "$number${config.orderedListDelimiter} "
                val indentStr = " ".repeat(markerStr.length)
                renderItem(item, markerStr, indentStr, config)
            }.joinToString("\n")
        }
    }

    internal fun renderItems(markerStr: String, indentStr: String, config: MarkdownRendererConfig): String {
        return items.joinToString("\n") { item: ListItem ->
            renderItem(item, markerStr, indentStr, config)
        }
    }
}

class ListItem(
    val blocks: KtList<Element>
) : Element() {
    override fun render(config: MarkdownRendererConfig): String {
        return blocks.joinToString("\n\n") { it.render(config) }
    }
}

private fun renderItem(
    item: ListItem,
    markerStr: String,
    indentStr: String,
    config: MarkdownRendererConfig
): String {
    val renderedBlocks = item.blocks.map { it.render(config) }

    if (renderedBlocks.isEmpty()) {
        return markerStr.trimEnd()
    }

    val first = renderedBlocks.first()
    val rest = renderedBlocks.drop(1)

    val firstLines = first.lines()
    val firstRendered = firstLines.mapIndexed { i, line ->
        if (i == 0) "$markerStr$line"
        else "$indentStr$line"
    }.joinToString("\n")

    if (rest.isEmpty()) {
        return firstRendered
    }

    val restRendered = rest.joinToString("\n\n") { block: String ->
        block.lines().joinToString("\n") { line ->
            if (line.isBlank()) line else "$indentStr$line"
        }
    }

    return "$firstRendered\n\n$restRendered"
}
