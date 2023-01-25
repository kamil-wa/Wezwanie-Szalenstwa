package com.example.projek_wezwanie_szalenstwa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.TestLooperManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        initializeParagraphAndAnwers(1);
    }

    private fun initListeners(){
        findViewById<Button>(R.id.characterButton).setOnClickListener(ButtonCharacterListener)
        findViewById<Button>(R.id.answerOneButton).setOnClickListener(ButtonAnswerOneListener)
        findViewById<Button>(R.id.answerTwoButton).setOnClickListener(ButtonAnswerTwoListener)
        findViewById<Button>(R.id.answerThreeButton).setOnClickListener(ButtonAnswerThreeListener)
    }

    private fun initializeParagraphAndAnwers(ID: Int){
        HPSet();

        val mainTextView = findViewById<Button>(R.id.mainTextView) as TextView

        findViewById<Button>(R.id.answerTwoButton).setVisibility(View.GONE)
        findViewById<Button>(R.id.answerThreeButton).setVisibility(View.GONE)

        FirebaseFirestore.getInstance().collection("paragraphs").whereEqualTo("ID", ID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (data in task.result) {
                    var string = data["description"].toString()
                    mainTextView.setText(string)
                    if(data["testedAbility"] != null)
                    {
                        var checkTest = Integer.parseInt(data["testedAbility"].toString())
                        if(playerTest(checkTest))
                        {
                            mainTextView.append(data["testSuccess"].toString())
                            if(Integer.parseInt(data["testResolution"].toString()) == 1)
                            {
                                val itemsSuccessList = data.get("itemsSuccess") as MutableList<String>
                                for (items in itemsSuccessList)
                                {
                                    playerCharacter.inventory.add(items)
                                }
                            }
                        }
                        else
                        {
                            mainTextView.append(data["testFailed"].toString())
                            if(Integer.parseInt(data["testResolution"].toString()) == 1)
                            {
                                val itemsSuccessList = data.get("itemsFailed") as MutableList<String>
                                for (items in itemsSuccessList)
                                {
                                    playerCharacter.inventory.add(items)
                                }
                            }
                        }
                    }
                    break
                }
            }
        }

        FirebaseFirestore.getInstance().collection("answers").whereEqualTo("paragraphID", ID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (data in task.result) {
                    findViewById<Button>(R.id.answerTwoButton).setVisibility(View.GONE)
                    findViewById<Button>(R.id.answerThreeButton).setVisibility(View.GONE)

                    findViewById<Button>(R.id.answerOneButton).text = data["answerOneText"].toString()
                    answerOneID = Integer.parseInt(data["answerOneID"].toString())

                    if(Integer.parseInt(data["answersCount"].toString()) == 2)
                    {
                        findViewById<Button>(R.id.answerTwoButton).text = data["answerTwoText"].toString()
                        answerTwoID = Integer.parseInt(data["answerTwoID"].toString())
                        findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)
                    }

                    if(Integer.parseInt(data["answersCount"].toString()) == 3)
                    {
                        findViewById<Button>(R.id.answerTwoButton).text = data["answerTwoText"].toString()
                        answerTwoID = Integer.parseInt(data["answerTwoID"].toString())
                        findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)

                        findViewById<Button>(R.id.answerThreeButton).text = data["answerThreeText"].toString()
                        answerThreeID = Integer.parseInt(data["answerThreeID"].toString())
                        findViewById<Button>(R.id.answerThreeButton).setVisibility(View.VISIBLE)
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

        if(abilityID == 1)
        {
            mainTextView.append("\n \n Test siły!")
            val rolld100 = (1..100).random()
            mainTextView.append("\n Rzuciłeś: " + rolld100.toString() + " vs twoja siłą równa: " + playerCharacter.strength)

            if(rolld100 <= playerCharacter.strength)
            {
                mainTextView.append("\nTest udany!\n\n")
                return true
            }
            else{
                mainTextView.append("\nTest nieudany!\n\n")
                return false
            }
        }
        else if(abilityID == 2)
        {
            mainTextView.append("\n \n Test inteligencji!")
            val rolld100 = (1..100).random()
            mainTextView.append("\n Rzuciłeś: " + rolld100.toString() + " vs twoja inteligencja równa: " + playerCharacter.dexterity)

            if(rolld100 <= playerCharacter.intelligence)
            {
                mainTextView.append("\nTest udany!\n\n")
                return true
            }
            else{
                mainTextView.append("\nTest nieudany!\n\n")
                return false
            }
        }
        else if(abilityID == 3)
        {
            mainTextView.append("\n \n Test wiedzy!")
            val rolld100 = (1..100).random()
            mainTextView.append("\n Rzuciłeś: " + rolld100.toString() + " vs twoja wiedza równa: " + playerCharacter.intelligence)

            if(rolld100 <= playerCharacter.intelligence)
            {
                mainTextView.append("\nTest udany!\n\n")
                return true
            }
            else{
                mainTextView.append("\nTest nieudany!\n\n")
                return false
            }
        }
        else if(abilityID == 4)
        {
            mainTextView.append("\n \n Test mitów!")
            val rolld100 = (1..100).random()
            mainTextView.append("\nRzuciłeś: " + rolld100.toString() + " vs twoje mity równe: " + playerCharacter.cthulhu)

            if(rolld100 <= playerCharacter.cthulhu)
            {
                mainTextView.append("\nTest udany!\n\n")
                return true
            }
            else{
                mainTextView.append("\nTest nieudany!\n\n")
                return false
            }
        }
        else if(abilityID == 5)
        {
            mainTextView.append("\n \n Test szczęścia! ")
            val rolld100 = (1..100).random()
            mainTextView.append("\n Rzuciłeś: " + rolld100.toString() + " vs twoje szczęście równe: " + playerCharacter.luck)

            if(rolld100 <= playerCharacter.luck)
            {
                mainTextView.append("\nTest udany!\n\n")
                return true
            }
            else{
                mainTextView.append("\nTest nieudany!\n\n")
                return false
            }
        }
        return false
    }

    private val ButtonCharacterListener = View.OnClickListener {
        val PostacIntent = Intent(this, postac_gracza::class.java)
        startActivity(PostacIntent)
    }

    private val ButtonAnswerOneListener = View.OnClickListener {
        initializeParagraphAndAnwers(answerOneID)
    }

    private val ButtonAnswerTwoListener = View.OnClickListener {
        initializeParagraphAndAnwers(answerTwoID)
    }

    private val ButtonAnswerThreeListener = View.OnClickListener {
        initializeParagraphAndAnwers(answerThreeID)
    }

}