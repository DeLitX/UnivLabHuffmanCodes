package com.delitx

private const val SYMBOL_CODE_DELIMITER = " - "
private const val CODE_CODE_DELIMITER = " ~ "
private const val CODE_TABLE_ENCODED_TEXT_DELIMITER = " | "
fun main() {
    while (true) {
        println("Введіть 1 для кодування тексту, 2 для декодування тексту, 3 для виходу")
        val input = readlnOrNull()
        when (input) {
            "1" -> {
                println("Введіть текст для кодування")
                val text = readlnOrNull()
                if (text != null) {
                    encodeTextAction(text)
                }
            }
            "2" -> {
                println("Введіть закодований текст")
                val encodedText = readLine()
                if (encodedText != null) {
                    try {
                        decodeTextAction(encodedText)
                    } catch (e: Exception) {
                        println("Неправильний закодований текст")
                    }
                }
            }
            "3" -> break
        }
    }
}

private fun encodeTextAction(text: String) {
    val huffmanMap = text.toHuffmanMap()
    val encodedText = huffmanMap.encodeText(text.toList())
    val codeTable = huffmanMap.codesTable
        .map { (symbol, code) -> symbol + SYMBOL_CODE_DELIMITER + code }
    val codeTableText = codeTable.joinToString(separator = CODE_CODE_DELIMITER)
    println("Закодований текст:")
    println(codeTableText + CODE_TABLE_ENCODED_TEXT_DELIMITER + encodedText)
}

private fun decodeTextAction(text: String) {
    val (codeTableText, encodedText) = text.split(CODE_TABLE_ENCODED_TEXT_DELIMITER)
    val codeTable = codeTableText.split(CODE_CODE_DELIMITER).associate {
        val (symbol, code) = it.split(SYMBOL_CODE_DELIMITER)
        symbol.first() to code
    }
    val huffmanMap = HuffmanMap(codeTable)
    val decodedText = huffmanMap.getSymbolsByCode(encodedText).joinToString(separator = "")
    println("Декодований текст:")
    println(decodedText)
}