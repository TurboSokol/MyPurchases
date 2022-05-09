package com.turbosokol.mypurchases.common.my_lists.model

data class MyListModel (
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