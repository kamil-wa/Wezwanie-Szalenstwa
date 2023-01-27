package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_NONE
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.TestLooperManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class main_game : AppCompatActivity() {

    var answerOneID = 0
    var answerTwoID = 0
    var answerThreeID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        initListeners()
        initializeParagraphAndAnswers(1);
    }

    private fun initListeners(){
        findViewById<Button>(R.id.characterButton).setOnClickListener(ButtonCharacterListener)
        findViewById<Button>(R.id.answerOneButton).setOnClickListener(ButtonAnswerOneListener)
        findViewById<Button>(R.id.answerTwoButton).setOnClickListener(ButtonAnswerTwoListener)
        findViewById<Button>(R.id.answerThreeButton).setOnClickListener(ButtonAnswerThreeListener)
    }

    private fun initializeParagraphAndAnswers(ID: Int){
        HPSet();

        if(ID == 1000)
        {
            val startGameParagraf = Intent(this, main_game::class.java)
            startActivity(startGameParagraf)
        }

        val mainTextView = findViewById<Button>(R.id.mainTextView) as TextView

        findViewById<Button>(R.id.answerOneButton).setVisibility(View.GONE)
        findViewById<Button>(R.id.answerTwoButton).setVisibility(View.GONE)
        findViewById<Button>(R.id.answerThreeButton).setVisibility(View.GONE)

        FirebaseFirestore.getInstance().collection("paragraphs").whereEqualTo("ID", ID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (data in task.result) {
                    playerCharacter.encounteredParagraphs.add(ID)

                    var string = data["description"].toString()
                    mainTextView.setText(string)

                    if(data["requiresItem"] != null)
                    {
                        if(data["itsAFight"] != null)
                        {
                            if(playerCharacter.inventory.contains((data["requiresItem"].toString())))
                            {
                                playerCharacter.HP -= Integer.parseInt(data["lostHP"].toString())
                                findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
                                answerOneID = Integer.parseInt(data["battleSuccessID"].toString())
                                findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                            }
                            else
                            {
                                playerCharacter.HP = 0
                                findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
                                answerOneID = Integer.parseInt(data["battleFailedID"].toString())
                                findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                            }
                        }
                        else if(playerCharacter.inventory.contains((data["requiresItem"].toString())))
                        {
                            mainTextView.append(data["itemText"].toString())
                        }
                    }


                    if(data["testedAbility"] != null)
                    {
                        var checkTest = Integer.parseInt(data["testedAbility"].toString())
                        if(Integer.parseInt(data["testResolution"].toString()) == 1) //test powoduje dodanie itemków
                        {
                            if(playerTest(checkTest))
                            {
                                mainTextView.text = mainTextView.text.toString() + "\n Test udany!\n\n " + data["testSuccess"].toString()
                                val itemsSuccessList = data.get("itemsSuccess") as MutableList<String>
                                for (items in itemsSuccessList)
                                {
                                    playerCharacter.inventory.add(items)
                                }
                            }
                            else
                            {
                                mainTextView.text = mainTextView.text.toString() + "\n Test nieudany!\n\n " + data["testFailed"].toString()
                                val itemsSuccessList = data.get("itemsFailed") as MutableList<String>
                                for (items in itemsSuccessList)
                                {
                                    playerCharacter.inventory.add(items)
                                }
                            }
                        }
                        else if(Integer.parseInt(data["testResolution"].toString()) == 2) // test powoduje przejście do nowego paragrafu
                        {
                            if(playerTest(checkTest))
                            {
                                findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
                                answerOneID = Integer.parseInt(data["testSuccessID"].toString())
                                findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                            }
                            else
                            {
                                findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
                                answerOneID = Integer.parseInt(data["testFailedID"].toString())
                                findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                            }
                        }
                    }
                }
            }
        }

        FirebaseFirestore.getInstance().collection("answers").whereEqualTo("paragraphID", ID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (data in task.result) {
                    val answersList = data.get("answers") as MutableList<String>

                    if(!playerCharacter.encounteredParagraphs.contains(Integer.parseInt(answersList[1])))
                    {
                        if(data["answerOneRequiredItem"] != null)
                        {
                            if(playerCharacter.inventory.contains((data["answerOneRequiredItem"].toString())))
                            {
                                findViewById<Button>(R.id.answerOneButton).text = answersList[0]
                                answerOneID = Integer.parseInt(answersList[1])
                                findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                            }
                        }
                        else
                        {
                            findViewById<Button>(R.id.answerOneButton).text = answersList[0]
                            answerOneID = Integer.parseInt(answersList[1])
                            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                        }
                    }
                    else if(data["forceAnswer"] != null)
                    {
                        findViewById<Button>(R.id.answerOneButton).text = answersList[0]
                        answerOneID = Integer.parseInt(answersList[1])
                        findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
                    }

                    if(answersList.size > 2)
                    {
                        if (!playerCharacter.encounteredParagraphs.contains(Integer.parseInt(answersList[3])))
                        {
                            if(data["answerOneRequiredItem"] != null)
                            {
                                if(playerCharacter.inventory.contains((data["answerOneRequiredItem"].toString())))
                                {
                                    findViewById<Button>(R.id.answerTwoButton).text = answersList[2]
                                    answerTwoID = Integer.parseInt(answersList[3])
                                    findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)
                                }
                            }
                            else {
                                findViewById<Button>(R.id.answerTwoButton).text = answersList[2]
                                answerTwoID = Integer.parseInt(answersList[3])
                                findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)
                            }
                        }
                    }

                    if(answersList.size > 4)
                    {
                        if (!playerCharacter.encounteredParagraphs.contains(Integer.parseInt(answersList[5])))
                        {
                            if(data["answerOneRequiredItem"] != null)
                            {
                                if(playerCharacter.inventory.contains((data["answerOneRequiredItem"].toString())))
                                {
                                    findViewById<Button>(R.id.answerThreeButton).text = answersList[4]
                                    answerThreeID = Integer.parseInt(answersList[5])
                                    findViewById<Button>(R.id.answerThreeButton).setVisibility(View.VISIBLE)
                                }
                            }
                            else {
                                findViewById<Button>(R.id.answerThreeButton).text = answersList[4]
                                answerThreeID = Integer.parseInt(answersList[5])
                                findViewById<Button>(R.id.answerThreeButton).setVisibility(View.VISIBLE)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun HPSet()
    {
        val hpText = findViewById<Button>(R.id.HPView) as TextView
        hpText.setText("HP: " + playerCharacter.HP.toString())
    }

    private fun playerTest(abilityID: Int): Boolean
    {
        val mainTextView = findViewById<TextView>(R.id.mainTextView) as TextView
        val rolld100 = (1..100).random()

        when (abilityID) {
            1 -> {

                mainTextView.text = mainTextView.text.toString() + "\n\n Test siły! \n Rzuciłeś: " + rolld100.toString() + " vs twoja siłą równa: " + playerCharacter.strength + " "
                return rolld100 <= playerCharacter.strength
            }
            2 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test inteligencji! \n Rzuciłeś: " + rolld100.toString() + " vs twoja inteligencja równa: " + playerCharacter.dexterity + " "
                return rolld100 <= playerCharacter.intelligence
            }
            3 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test wiedzy! \n Rzuciłeś: " + rolld100.toString() + " vs twoja wiedza równa: " + playerCharacter.intelligence + " "
                return rolld100 <= playerCharacter.intelligence
            }
            4 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test mitów! \n Rzuciłeś: " + rolld100.toString() + " vs twoje mity równe: " + playerCharacter.cthulhu + " "
                return rolld100 <= playerCharacter.cthulhu
            }
            5 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test szczęścia! \n Rzuciłeś: " + rolld100.toString() + " vs twoje szczęście równe: " + playerCharacter.luck + " "
                return rolld100 <= playerCharacter.luck
            }
            else -> return false
        }
    }

    private val ButtonCharacterListener = View.OnClickListener {
        val PostacIntent = Intent(this, postac_gracza::class.java)
        startActivity(PostacIntent)
    }

    private val ButtonAnswerOneListener = View.OnClickListener {
        initializeParagraphAndAnswers(answerOneID)
    }

    private val ButtonAnswerTwoListener = View.OnClickListener {
        initializeParagraphAndAnswers(answerTwoID)
    }

    private val ButtonAnswerThreeListener = View.OnClickListener {
        initializeParagraphAndAnswers(answerThreeID)
    }

}