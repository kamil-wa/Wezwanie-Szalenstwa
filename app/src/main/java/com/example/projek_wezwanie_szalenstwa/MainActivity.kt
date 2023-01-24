package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.projek_wezwanie_szalenstwa.Postac.tworzenie_postaci

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }

    private fun initListeners(){
        val ButtonStart = findViewById<Button>(R.id.Zacznij_gre_button)
        ButtonStart.setOnClickListener(ButtonStartListener)
    }

    private val ButtonStartListener = View.OnClickListener {
        val TworzenieIntent = Intent(this, tworzenie_postaci::class.java)
        startActivity(TworzenieIntent)
    }
}