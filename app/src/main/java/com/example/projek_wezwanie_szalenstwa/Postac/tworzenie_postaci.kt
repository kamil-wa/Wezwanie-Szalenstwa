package com.example.projek_wezwanie_szalenstwa.Postac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.projek_wezwanie_szalenstwa.R

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
        val ButtonComplete = findViewById<Button>(R.id.Zapisz_postac_i_startuj)

        ButtonRollStrength.setOnClickListener(rollStrengthValue)
        ButtonRollDexterity.setOnClickListener(rollDexterityValue)
        ButtonRollIntelligence.setOnClickListener(rollIntelligenceValue)
        ButtonRollCthulhu.setOnClickListener(rollCthulhuValue)
        ButtonRollLuck.setOnClickListener(rollLuckValue)
        ButtonComplete.setOnClickListener(completeTheCreation)
    }

    private val rollStrengthValue = View.OnClickListener {
        val randomValue = (1..100).random()
        val strengthTextView = findViewById<Button>(R.id.strengthValue) as TextView
        strengthTextView.setText(randomValue.toString());
    }

    private val rollDexterityValue = View.OnClickListener {
        val randomValue = (1..100).random()
        val dexterityTextView = findViewById<Button>(R.id.dexterityValue) as TextView
        dexterityTextView.setText(randomValue.toString());
    }

    private val rollIntelligenceValue = View.OnClickListener {
        val randomValue = (1..100).random()
        val intelligenceTextView = findViewById<Button>(R.id.intValue) as TextView
        intelligenceTextView.setText(randomValue.toString());
    }

    private val rollCthulhuValue = View.OnClickListener {
        val randomValue = (1..100).random()
        val cthulhuTextView = findViewById<Button>(R.id.cthulhuValue) as TextView
        cthulhuTextView.setText(randomValue.toString());
    }

    private val rollLuckValue = View.OnClickListener {
        val randomValue = (1..100).random()
        val luckTextView = findViewById<Button>(R.id.luckValue) as TextView
        luckTextView.setText(randomValue.toString());
    }

    private val completeTheCreation = View.OnClickListener {
        val randomValue = (1..100).random()
        val luckTextView = findViewById<Button>(R.id.luckValue) as TextView
        luckTextView.setText(randomValue.toString());
    }


}