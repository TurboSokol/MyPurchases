package com.turbosokol.mypurchases.common.lists.model

data class ListModel (
val listId: Long,
val title: String,
val spentSum: Int,
val expectedSum: Int
) {
    override fun toString(): String = """
        listsDb [
            id: $listId
            title: $title
            spentSum: $spentSum
            expectedSum: $expectedSum
            ]
    """.trimIndent()
}