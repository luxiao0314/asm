package com.asm.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.asm.demo.utils.TestAnnotation

class MainActivity : AppCompatActivity() {

    val tag = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        findViewById<TextView>(R.id.tv_content).setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }
    }

    @TestAnnotation(code = 10000, message = "MainActivity.initData")
    private fun initData() {
        Thread.sleep(2000)
        Log.d(tag, "initData")
    }
}