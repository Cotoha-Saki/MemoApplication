package xyz.cotoha.program.memoapplication

import android.provider.BaseColumns

object MemoContract {
    object MemoEntry : BaseColumns {
        const val TABLE_NAME = "memos"
        const val COLUMN_NAME_CONTENT = "content"
    }
}
