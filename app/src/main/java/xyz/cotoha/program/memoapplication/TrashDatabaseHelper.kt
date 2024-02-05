package xyz.cotoha.program.memoapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TrashDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${TrashContract.TrashEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${TrashContract.TrashEntry.COLUMN_NAME_CONTENT} TEXT,
            ${TrashContract.TrashEntry.COLUMN_NAME_DELETE_DATE} INTEGER) 
        """.trimIndent()
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    fun deleteMemo(content: String) {
        val db = writableDatabase
        db.delete(
            TrashContract.TrashEntry.TABLE_NAME,
            "${TrashContract.TrashEntry.COLUMN_NAME_CONTENT} = ?",
            arrayOf(content)
        )
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${TrashContract.TrashEntry.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 2 // データベースのバージョンを更新
        const val DATABASE_NAME = "TrashMemo.db"
    }
}
