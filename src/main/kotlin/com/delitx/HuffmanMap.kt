package com.delitx

import java.util.*

class HuffmanMap<Symbol> {

    val codesTable: Map<Symbol, String>

    constructor(symbols: List<Pair<Symbol, Int>>) {
        val comparator = compareBy<Pair<Node<Symbol>, Int>> { it.second }
        val queue = PriorityQueue(comparator)
        queue.addAll(symbols.map { Pair(NodeSymbol(it.first), it.second) })
        while (queue.size > 1) {
            val first = queue.remove()
            val second = queue.remove()
            queue.add(Pair(mergeNodes(first.first, second.first), first.second + second.second))
        }
        val rootNode = if (queue.size == 1) {
            queue.remove().first
        } else {
            throw IllegalArgumentException()
        }
        val codes = mutableMapOf<Symbol, String>()
        for ((symbol, _) in symbols) {
            codes[symbol] = rootNode.getCode(symbol) ?: throw IllegalArgumentException()
        }
        codesTable = codes
    }


    constructor(symbols: Map<Symbol, String>) {
        codesTable = symbols
    }

    fun encodeText(text: List<Symbol>): String {
        val resultBuilder = StringBuilder()
        for (symbol in text) {
            resultBuilder.append(getCode(symbol) ?: "")
        }
        return resultBuilder.toString()
    }

    fun getCode(symbol: Symbol): String? = codesTable[symbol]

    fun getSymbolsByCode(code: String): List<Symbol> {
        var currentCode = code
        val result = mutableListOf<Symbol>()
        while (currentCode != "") {
            val (symbol, restOfCode) = getSymbolByCode(currentCode)
            if (symbol != null) {
                result.add(symbol)
            }
            currentCode = restOfCode
        }
        return result
    }

    private fun getSymbolByCode(code: String): Pair<Symbol?, String> {
        var codeLeft = code
        var currentCode = ""
        while (codeLeft != "") {
            currentCode += codeLeft.first()
            codeLeft = codeLeft.substring(1)
            val symbol = codesTable.toList().find { it.second == currentCode }?.first
            if (symbol != null) {
                return Pair(symbol, codeLeft)
            }
        }
        return Pair(null, "")
    }

    private fun mergeNodes(firstNode: Node<Symbol>, secondNode: Node<Symbol>) =
        NodeParent(firstNode, secondNode)

    private sealed interface Node<Symbol> {
        fun getCode(symbol: Symbol): String?
    }

    private data class NodeParent<Symbol>(
        val leftChildren: Node<Symbol>,
        val rightChildren: Node<Symbol>
    ) : Node<Symbol> {
        override fun getCode(symbol: Symbol): String? {
            val leftCode = leftChildren.getCode(symbol)
            val rightCode = rightChildren.getCode(symbol)
            if (leftCode != null) {
                return "0$leftCode"
            }
            if (rightCode != null) {
                return "1$rightCode"
            }
            return null
        }
    }

    private data class NodeSymbol<Symbol>(
        val symbol: Symbol
    ) : Node<Symbol> {
        override fun getCode(symbol: Symbol): String? = if (symbol == this.symbol) {
            ""
        } else {
            null
        }
    }
}

fun String.toHuffmanMap(): HuffmanMap<Char> {
    val map = getCharOccurRate()
    return HuffmanMap(map)
}

fun String.getCharOccurRate(): List<Pair<Char, Int>> {
    val map = mutableMapOf<Char, Int>()
    for (char in this) {
        if (!map.containsKey(char)) {
            map[char] = 0
        }
        map[char] = map[char]!! + 1
    }
    return map.toList()
}
