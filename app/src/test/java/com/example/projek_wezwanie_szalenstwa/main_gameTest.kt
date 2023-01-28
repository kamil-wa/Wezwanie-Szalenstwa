package com.example.projek_wezwanie_szalenstwa
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class main_gameTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun roll1d100() {

        val acrivity = main_game()
        val result = acrivity.roll1d100()
        assertTrue(result >= 1 && result <= 100)
    }


    @Test
    fun testDecreasePlayerHP() {
        val acrivity = main_game()
        var tp = tworzenie_postaci()
        acrivity.decreasePlayerHP(2)
        assertEquals(3, playerCharacter.HP)
    }

    @Test
    fun testAddCompletedParagraphToAPlayer() {
        val acrivity = main_game()
        var tp = tworzenie_postaci()
        acrivity.addEncounteredParagraph(5)
        val encList = playerCharacter.encounteredParagraphs
        for (i in 0..encList.size){
            assertEquals(5, playerCharacter.encounteredParagraphs[0])
        }
    }

    @Test
    fun testIfItemIsInInventory() {
        val activity = main_game()
        var tp = tworzenie_postaci()
        var items = listOf<String>("Item1","Item2","Item3")
        playerCharacter.inventory.addAll(items)

        assertTrue(activity.checkItemInInventory("Item2"))

    }



}
