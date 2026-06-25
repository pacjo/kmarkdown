package nodomain.pacjo.kmarkdown.config

data class MarkdownRendererConfig(
    val newlinesNumBeforeSectionHeader: Int = 2,
    val newlinesNumAfterSectionHeader: Int = 1,
    val listItemIndent: Int = 4,
    val listItemMarkers: List<Char> = listOf('-', '*', '+'),
    val orderedListDelimiter: Char = '.',
    val emphasisDelimiter: Char = '*',
    val blockSpacing: Int = 1,
) {
    init {
        require(newlinesNumBeforeSectionHeader >= 0) {
            "newlinesNumBeforeSectionHeader must be >= 0, got $newlinesNumBeforeSectionHeader"
        }
        require(newlinesNumAfterSectionHeader >= 0) {
            "newlinesNumAfterSectionHeader must be >= 0, got $newlinesNumAfterSectionHeader"
        }
        require(listItemIndent >= 0) {
            "listItemIndent must be >= 0, got $listItemIndent"
        }
        require(listItemMarkers.isNotEmpty()) {
            "listItemMarkers must not be empty"
        }
        require(listItemMarkers.all { it in listOf('-', '+', '*') }) {
            "listItemMarkers must only contain '-', '+', or '*', got $listItemMarkers"
        }
        require(orderedListDelimiter == '.' || orderedListDelimiter == ')') {
            "orderedListDelimiter must be '.' or ')', got '$orderedListDelimiter'"
        }
        require(emphasisDelimiter == '*' || emphasisDelimiter == '_') {
            "emphasisDelimiter must be '*' or '_', got '$emphasisDelimiter'"
        }
        require(blockSpacing >= 0) {
            "blockSpacing must be >= 0, got $blockSpacing"
        }
    }
}
