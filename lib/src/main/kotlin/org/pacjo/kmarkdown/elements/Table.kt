package org.pacjo.kmarkdown.elements

import kotlin.collections.List as KtList
import org.pacjo.kmarkdown.config.MarkdownRendererConfig

enum class Alignment { LEFT, CENTER, RIGHT }

class TableColumn(val header: KtList<Element>, val alignment: Alignment = Alignment.LEFT)

class TableRow(val cells: KtList<KtList<Element>>)

class Table(
    val columns: KtList<TableColumn>,
    val rows: KtList<TableRow>
) : Element() {

    override fun render(config: MarkdownRendererConfig): String {
        val columnCount = columns.size
        val renderedHeader = columns.map { it.header.joinToString("") { e -> e.render(config) } }
        val renderedRows = rows.map { row ->
            (0 until columnCount).map { i ->
                row.cells.getOrElse(i) { emptyList() }.joinToString("") { e -> e.render(config) }
            }
        }

        val widths = (0 until columnCount).map { i ->
            val cellLengths = renderedRows.map { it[i].length }
            maxOf(renderedHeader[i].length, cellLengths.maxOrNull() ?: 0)
        }

        fun pad(content: String, width: Int, alignment: Alignment): String = when (alignment) {
            Alignment.LEFT -> content.padEnd(width)
            Alignment.CENTER -> {
                val left = (width - content.length) / 2
                val right = width - content.length - left
                " ".repeat(left) + content + " ".repeat(right)
            }

            Alignment.RIGHT -> content.padStart(width)
        }

        fun delimiter(alignment: Alignment, width: Int): String = when (alignment) {
            Alignment.LEFT -> ":${"-".repeat(width + 1)}"
            Alignment.CENTER -> ":${"-".repeat(width)}:"
            Alignment.RIGHT -> "${"-".repeat(width + 1)}:"
        }

        val headerLine = columns.mapIndexed { i, col ->
            " ${pad(renderedHeader[i], widths[i], col.alignment)} "
        }.joinToString("|", "|", "|")

        val delimiterLine = columns.mapIndexed { i, col ->
            delimiter(col.alignment, widths[i])
        }.joinToString("|", "|", "|")

        val dataLines = renderedRows.map { row ->
            row.mapIndexed { i, content ->
                " ${pad(content, widths[i], columns[i].alignment)} "
            }.joinToString("|", "|", "|")
        }

        return (listOf(headerLine, delimiterLine) + dataLines).joinToString("\n")
    }
}
