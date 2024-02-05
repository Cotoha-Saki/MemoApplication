package xyz.cotoha.program.memoapplication

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var memoListView: ListView
    private lateinit var dbHelper: MainDatabaseHelper
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // ダブルクリックとみなす時間間隔（ミリ秒）
    private var lastClickTime: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = MainDatabaseHelper(this)
        memoListView = findViewById(R.id.lv)

        loadMemos()

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            showAddMemoDialog()
        }

        findViewById<Button>(R.id.btngar).setOnClickListener {
            // ゴミ箱画面へ遷移するIntentを作成し、startActivityで遷移する
            val intent = Intent(this, TrashActivity::class.java)
            startActivity(intent)
        }


        memoListView.setOnItemClickListener { _, _, position, _ ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                editMemo(position)
            }
            lastClickTime = currentTime
        }

        memoListView.setOnItemLongClickListener { _, _, position, _ ->
            deleteMemo(position)
            true
        }
    }

    private fun showAddMemoDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("新規作成")
            .setMessage("メモを入力してください")
            .setView(input)
            .setPositiveButton("作成") { _, _ ->
                val memoText = input.text.toString()
                addMemo(memoText)
            }
            .setNegativeButton("キャンセル", null)
            .show()
    }

    private fun addMemo(memoText: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(MemoContract.MemoEntry.COLUMN_NAME_CONTENT, memoText)
        }
        db.insert(MemoContract.MemoEntry.TABLE_NAME, null, values)
        loadMemos()
    }

    private fun loadMemos() {
        val memoList = ArrayList<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            MemoContract.MemoEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val content =
                cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_CONTENT))
            memoList.add(content)
        }
        cursor.close()
        memoListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, memoList)
    }

    private fun editMemo(position: Int) {
        val memoList = ArrayList<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            MemoContract.MemoEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToPosition(position)
        val memoId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_CONTENT))
        cursor.close()

        val input = EditText(this)
        input.setText(content)
        AlertDialog.Builder(this)
            .setTitle("メモを編集")
            .setView(input)
            .setPositiveButton("保存") { _, _ ->
                val editedMemoText = input.text.toString()
                updateMemo(memoId, editedMemoText)
            }
            .setNegativeButton("キャンセル", null)
            .show()
    }

    private fun updateMemo(memoId: Long, editedMemoText: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(MemoContract.MemoEntry.COLUMN_NAME_CONTENT, editedMemoText)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(memoId.toString())
        db.update(
            MemoContract.MemoEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        loadMemos()
    }

    private fun deleteMemo(position: Int) {
        val db = dbHelper.writableDatabase
        val cursor = db.query(
            MemoContract.MemoEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToPosition(position)
        val memoId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_CONTENT))
        cursor.close()

        // ゴミ箱データベースにメモを移動
        val trashDb = TrashDatabaseHelper(this).writableDatabase
        val values = ContentValues().apply {
            put(TrashContract.TrashEntry.COLUMN_NAME_CONTENT, content)
        }
        trashDb.insert(TrashContract.TrashEntry.TABLE_NAME, null, values)

        // メイン画面のデータベースからメモを削除
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(memoId.toString())
        db.delete(
            MemoContract.MemoEntry.TABLE_NAME,
            selection,
            selectionArgs
        )

        loadMemos()
    }

}
