package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class end_game : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)
        initListeners()
    }

    private fun initListeners() {
        val ButtonRestart = findViewById<Button>(R.id.returnMenu)
        ButtonRestart.setOnClickListener(ButtonRestartListener)
    }

    private val ButtonRestartListener = View.OnClickListener {
        val MenuIntent = Intent(this, MainActivity::class.java)
        startActivity(MenuIntent)
    }
}