package com.example.projek_wezwanie_szalenstwa
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


import org.junit.Assert
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
        val playerCharacter = Character(1,2,3,4,5,2,mutableListOf<String>(),mutableListOf<Int>())
        acrivity.decreasePlayerHP(2)
        assertEquals(1, playerCharacter.HP)
    }
}
