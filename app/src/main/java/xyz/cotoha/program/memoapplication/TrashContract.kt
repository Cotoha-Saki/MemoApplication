package xyz.cotoha.program.memoapplication

import android.provider.BaseColumns

object TrashContract {
    // テーブルのカラム名を定義する
    object TrashEntry : BaseColumns {
        const val TABLE_NAME = "trash"
        const val COLUMN_NAME_CONTENT = "content"
        const val COLUMN_NAME_DELETE_DATE = "delete_date" // 削除日を格納する列を追加
    }
}
