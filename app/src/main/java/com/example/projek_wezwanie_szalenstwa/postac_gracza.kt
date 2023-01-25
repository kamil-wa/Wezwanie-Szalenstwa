package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.projek_wezwanie_szalenstwa.R

class postac_gracza : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postac_gracza)
        initListeners()

        val strengthText = findViewById<Button>(R.id.strengthCSValue) as TextView
        val dexterityText = findViewById<Button>(R.id.dexterityCSValue) as TextView
        val intelligenceText = findViewById<Button>(R.id.intCSValue) as TextView
        val cthulhuText = findViewById<Button>(R.id.cthulhuCSValue) as TextView
        val luckText = findViewById<Button>(R.id.luckCSValue) as TextView
        val HPText = findViewById<Button>(R.id.HPCSValue) as TextView
        val itemsList = findViewById<Button>(R.id.itemsList) as TextView

        strengthText.text = playerCharacter.strength.toString();
        dexterityText.text = playerCharacter.dexterity.toString();
        intelligenceText.text = playerCharacter.intelligence.toString();
        cthulhuText.text = playerCharacter.cthulhu.toString();
        luckText.text = playerCharacter.luck.toString();
        HPText.text = playerCharacter.HP.toString();

        for(items in playerCharacter.inventory)
        {
            itemsList.append(items + " ")
        }
    }

    private fun initListeners(){
        val ButtonBack = findViewById<Button>(R.id.BackButtonCharacter)
        ButtonBack.setOnClickListener(ButtonBackListener)
    }

    private val ButtonBackListener = View.OnClickListener {
        finish()
    }
}