package xyz.cotoha.program.memoapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Viewの取得
        val btnAdd:Button =findViewById(R.id.btnAdd)
        val lv:ListView =findViewById(R.id.lv)

        //1)アダプターに入れてListViewにセット
        val adapter =ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()//最初は空っぽのリスト/ arrayListOf()
        )
        lv.adapter =adapter

        btnAdd.setOnClickListener {
            val et=EditText(this)
            AlertDialog.Builder(this)
                .setTitle("メモの作成")
                .setView(et)
                .setPositiveButton("追加",DialogInterface.OnClickListener { dialogInterface, i ->
                    val myTodo =et.text.toString()
                    adapter.add(myTodo)
                })
                .setNegativeButton("キャンセル",null)
                .show()
        }
    }
}