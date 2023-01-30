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

    var itemOneToGive = ""
    var itemTwoToGive = ""
    var itemThreeToGive = ""

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

    private fun hideButtons()
    {
        findViewById<Button>(R.id.answerOneButton).setVisibility(View.GONE)
        findViewById<Button>(R.id.answerTwoButton).setVisibility(View.GONE)
        findViewById<Button>(R.id.answerThreeButton).setVisibility(View.GONE)
    }

    private fun checkIfComplete(ID: Int)
    {
        if(ID == 1000) startActivity(Intent(this, end_game::class.java))
    }

    fun addEncounteredParagraph(ID: Int)
    {
        playerCharacter.encounteredParagraphs.add(ID)
    }

    fun decreasePlayerHP(HP: Int)
    {
        playerCharacter.HP -= HP
    }

    fun checkItemInInventory(item: String): Boolean
    {
        return playerCharacter.inventory.contains(item)
    }

    fun addItemToInventory(item: String)
    {
        playerCharacter.inventory.add(item)
    }

    private fun battleTime(requiredWeapon: String, hpLost: Int, successID: Int, failedID: Int)
    {
        if(!playerCharacter.inventory.contains(requiredWeapon))
        {
            decreasePlayerHP(hpLost)
            HPSet()
            findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
            answerOneID = successID
            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
        }
        else
        {
            decreasePlayerHP(playerCharacter.HP)
            HPSet()
            findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
            answerOneID = failedID
            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
        }
    }

    private fun addItemsAfterTest(test: Int, mainTextView: TextView, textSuccess: String, itemsSuccess: MutableList<String>, textFailed: String, itemsFailed: MutableList<String>)
    {
        if(playerTest(test, roll1d100()))
        {
            mainTextView.text = mainTextView.text.toString() + "\n Test udany!\n\n " + textSuccess
            val itemsSuccessList = itemsSuccess
            for (items in itemsSuccessList)
            {
                playerCharacter.inventory.add(items)
            }
        }
        else
        {
            mainTextView.text = mainTextView.text.toString() + "\n Test nieudany!\n\n " + textFailed
            val itemsSuccessList = itemsFailed
            for (items in itemsSuccessList)
            {
                playerCharacter.inventory.add(items)
            }
        }
    }

    private fun moveToParagraphAfterTest(test: Int, testSuccess: String, testFailed: String)
    {
        if(playerTest(test, roll1d100()))
        {
            findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
            answerOneID = Integer.parseInt(testSuccess)
            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
        }
        else
        {
            findViewById<Button>(R.id.answerOneButton).text = "Kontynuuj"
            answerOneID = Integer.parseInt(testFailed)
            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
        }
    }

    private fun showTextAfterParagraph(test: Int, mainTextView: TextView, textSuccess: String, textFailed: String)
    {
        if(playerTest(test, roll1d100())) mainTextView.text = mainTextView.text.toString() + "\n Test udany!\n\n " + textSuccess
        else mainTextView.text = mainTextView.text.toString() + "\n Test nieudany!\n\n " + textFailed
    }


    private fun loadParagraph(ID: Int)
    {
        val mainTextView = findViewById<Button>(R.id.mainTextView) as TextView

        FirebaseFirestore.getInstance().collection("paragraphs").whereEqualTo("ID", ID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (data in task.result) {
                    addEncounteredParagraph(ID)
                    mainTextView.setText(data["description"].toString())

                    if(data["itsAFight"] != null) battleTime(data["requiresItem"].toString(), Integer.parseInt(data["lostHP"].toString()), Integer.parseInt(data["battleSuccessID"].toString()), Integer.parseInt(data["battleFailedID"].toString()))

                    if(data["requiresItem"] != null)
                        if (checkItemInInventory((data["requiresItem"].toString()))) mainTextView.append(data["itemText"].toString())

                    if(data["testedAbility"] != null)
                    {
                        var checkTest = Integer.parseInt(data["testedAbility"].toString())

                        when (Integer.parseInt(data["testResolution"].toString())) {
                            1 -> {
                                addItemsAfterTest(checkTest, mainTextView, data["testSuccess"].toString(), data.get("itemsSuccess") as MutableList<String>, data["testFailed"].toString(), data.get("itemsFailed") as MutableList<String>)
                            }
                            2 -> {
                                moveToParagraphAfterTest(checkTest, data["testSuccessID"].toString(), data["testFailedID"].toString())
                            }
                            3 -> {
                                showTextAfterParagraph(checkTest, mainTextView, data["testSuccess"].toString(), data["testFailed"].toString())
                            }
                        }
                    }
                }
            }
        }
    }
    private fun loadAnswers(ID: Int)
    {
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
                        else if(data["answerOneGivesItem"] != null)
                        {
                            itemOneToGive = data["answerOneGivesItem"].toString()
                            findViewById<Button>(R.id.answerOneButton).text = answersList[0]
                            answerOneID = Integer.parseInt(answersList[1])
                            findViewById<Button>(R.id.answerOneButton).setVisibility(View.VISIBLE)
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
                            if(data["answerTwoRequiredItem"] != null)
                            {
                                if(playerCharacter.inventory.contains((data["answerTwoRequiredItem"].toString())))
                                {
                                    findViewById<Button>(R.id.answerTwoButton).text = answersList[2]
                                    answerTwoID = Integer.parseInt(answersList[3])
                                    findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)
                                }
                            }
                            else if(data["answerTwoGivesItem"] != null)
                            {
                                itemTwoToGive = data["answerTwoGivesItem"].toString()
                                findViewById<Button>(R.id.answerTwoButton).text = answersList[2]
                                answerTwoID = Integer.parseInt(answersList[3])
                                findViewById<Button>(R.id.answerTwoButton).setVisibility(View.VISIBLE)
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
                            if(data["answerThreeRequiredItem"] != null)
                            {
                                if(playerCharacter.inventory.contains((data["answerThreeRequiredItem"].toString())))
                                {
                                    findViewById<Button>(R.id.answerThreeButton).text = answersList[4]
                                    answerThreeID = Integer.parseInt(answersList[5])
                                    findViewById<Button>(R.id.answerThreeButton).setVisibility(View.VISIBLE)
                                }
                            }
                            else if(data["answerThreeGivesItem"] != null)
                            {
                                itemThreeToGive = data["answerThreeGivesItem"].toString()
                                findViewById<Button>(R.id.answerThreeButton).text = answersList[4]
                                answerThreeID = Integer.parseInt(answersList[5])
                                findViewById<Button>(R.id.answerThreeButton).setVisibility(View.VISIBLE)
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

    private fun initializeParagraphAndAnswers(ID: Int){
        HPSet()
        checkIfComplete(ID)
        hideButtons()
        loadParagraph(ID)
        loadAnswers(ID)
    }

    fun HPSet()
    {
        val hpText = findViewById<Button>(R.id.HPView) as TextView
        hpText.setText("HP: " + playerCharacter.HP.toString())
    }

    fun roll1d100(): Int
    {
        return (1..100).random()
    }

    private fun playerTest(abilityID: Int, value: Int): Boolean
    {
        val mainTextView = findViewById<TextView>(R.id.mainTextView) as TextView
        val roll = value

        when (abilityID) {
            1 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test siły! \n Rzuciłeś: " + roll.toString() + " vs twoja siłą równa: " + playerCharacter.strength + " "
                return roll <= playerCharacter.strength
            }
            2 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test zręczności! \n Rzuciłeś: " + roll.toString() + " vs twoja zręczność równa: " + playerCharacter.dexterity + " "
                return roll <= playerCharacter.dexterity
            }
            3 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test wiedzy! \n Rzuciłeś: " + roll.toString() + " vs twoja wiedza równa: " + playerCharacter.intelligence + " "
                return roll <= playerCharacter.intelligence
            }
            4 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test mitów! \n Rzuciłeś: " + roll.toString() + " vs twoje mity równe: " + playerCharacter.cthulhu + " "
                return roll <= playerCharacter.cthulhu
            }
            5 -> {
                mainTextView.text = mainTextView.text.toString() + "\n\n Test szczęścia! \n Rzuciłeś: " + roll.toString() + " vs twoje szczęście równe: " + playerCharacter.luck + " "
                return roll <= playerCharacter.luck
            }
            else -> return false
        }
    }

    private val ButtonCharacterListener = View.OnClickListener {
        val PostacIntent = Intent(this, postac_gracza::class.java)
        startActivity(PostacIntent)
    }

    private val ButtonAnswerOneListener = View.OnClickListener {
        if(itemOneToGive != "") {
            addItemToInventory(itemOneToGive)
            itemOneToGive = ""
        }

        initializeParagraphAndAnswers(answerOneID)
    }

    private val ButtonAnswerTwoListener = View.OnClickListener {
        if(itemTwoToGive != "") {
            addItemToInventory(itemTwoToGive)
            itemTwoToGive = ""
        }

        initializeParagraphAndAnswers(answerTwoID)
    }

    private val ButtonAnswerThreeListener = View.OnClickListener {
        if(itemThreeToGive != "") {
            addItemToInventory(itemThreeToGive)
            itemThreeToGive = ""
        }

        initializeParagraphAndAnswers(answerThreeID)
    }

}