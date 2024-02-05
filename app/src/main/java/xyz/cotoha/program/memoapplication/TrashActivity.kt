// TrashActivity.kt
package xyz.cotoha.program.memoapplication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class TrashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        val trashDb = TrashDatabaseHelper(this).readableDatabase
        val cursor = trashDb.query(
            TrashContract.TrashEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val memoList = ArrayList<String>()
        while (cursor.moveToNext()) {
            val content = cursor.getString(cursor.getColumnIndexOrThrow(TrashContract.TrashEntry.COLUMN_NAME_CONTENT))
            memoList.add(content)
        }
        cursor.close()

        val listView = findViewById<ListView>(R.id.trashListView)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, memoList)

        val btnBackToMain = findViewById<Button>(R.id.btnBackToMain)
        btnBackToMain.setOnClickListener {
            // メイン画面に戻るIntentを作成し、startActivityで遷移する
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
