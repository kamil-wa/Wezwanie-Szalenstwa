package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

    var playerCharacter = Character(0,0,0,0, 5,5, mutableListOf<String>(),mutableListOf<Int>())

class tworzenie_postaci : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tworzenie_postaci)
        initListeners()
    }

    private fun initListeners(){
        val ButtonRollStrength = findViewById<Button>(R.id.rollStrength)
        val ButtonRollDexterity = findViewById<Button>(R.id.rollDexterity)
        val ButtonRollIntelligence= findViewById<Button>(R.id.rollInt)
        val ButtonRollCthulhu = findViewById<Button>(R.id.rollCthulhu)
        val ButtonRollLuck = findViewById<Button>(R.id.rollLuck)
        val ButtonAccept = findViewById<Button>(R.id.Zapisz_postac_i_startuj)

        ButtonRollStrength.setOnClickListener(rollStrengthValue)
        ButtonRollDexterity.setOnClickListener(rollDexterityValue)
        ButtonRollIntelligence.setOnClickListener(rollIntelligenceValue)
        ButtonRollCthulhu.setOnClickListener(rollCthulhuValue)
        ButtonRollLuck.setOnClickListener(rollLuckValue)
        ButtonAccept.setOnClickListener(acceptAndPlay)
    }

    private val rollStrengthValue = View.OnClickListener {
        val randomValue = (10..50).random()
        val strengthTextView = findViewById<Button>(R.id.strengthValue) as TextView
        strengthTextView.setText(randomValue.toString())
    }

    private val rollDexterityValue = View.OnClickListener {
        val randomValue = (10..40).random()
        val dexterityTextView = findViewById<Button>(R.id.dexterityValue) as TextView
        dexterityTextView.setText(randomValue.toString())
    }

    private val rollIntelligenceValue = View.OnClickListener {
        val randomValue = (20..60).random()
        val intelligenceTextView = findViewById<Button>(R.id.intValue) as TextView
        intelligenceTextView.setText(randomValue.toString())
    }

    private val rollCthulhuValue = View.OnClickListener {
        val randomValue = (5..30).random()
        val cthulhuTextView = findViewById<Button>(R.id.cthulhuValue) as TextView
        cthulhuTextView.setText(randomValue.toString())
    }

    private val rollLuckValue = View.OnClickListener {
        val randomValue = (10..50).random()
        val luckTextView = findViewById<Button>(R.id.luckValue) as TextView
        luckTextView.setText(randomValue.toString())
    }

    private val acceptAndPlay = View.OnClickListener {
        val strengthTextView = findViewById<Button>(R.id.strengthValue) as TextView
        val dexterityTextView = findViewById<Button>(R.id.dexterityValue) as TextView
        val intelligenceTextView = findViewById<Button>(R.id.intValue) as TextView
        val cthulhuTextView = findViewById<Button>(R.id.cthulhuValue) as TextView
        val luckTextView = findViewById<Button>(R.id.luckValue) as TextView

        playerCharacter.strength = Integer.parseInt(strengthTextView.text.toString())
        playerCharacter.dexterity = Integer.parseInt(dexterityTextView.text.toString())
        playerCharacter.intelligence = Integer.parseInt(intelligenceTextView.text.toString())
        playerCharacter.cthulhu = Integer.parseInt(cthulhuTextView.text.toString())
        playerCharacter.luck = Integer.parseInt(luckTextView.text.toString())

        playerCharacter.encounteredParagraphs.clear()
        playerCharacter.inventory.clear()

        val startGameParagraf = Intent(this, main_game::class.java)
        startActivity(startGameParagraf)
    }

}