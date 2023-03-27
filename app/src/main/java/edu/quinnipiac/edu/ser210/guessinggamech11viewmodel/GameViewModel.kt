package edu.quinnipiac.edu.ser210.guessinggamech11viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import edu.quinnipiac.edu.ser210.guessinggamech11viewmodel.databinding.FragmentGameBinding

class GameViewModel: ViewModel() {
        private val words = listOf("Android", "Activity", "Fragment")
        //the words the user has to goes
        private val secretWord = words.random().uppercase()
        //how thw word is displayed
        private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String>
    get() = _secretWordDisplay
        //correct and incorrect Guesses
        private var correctGuesses = ""
       private val _incorrectGuesses = MutableLiveData<String>("")
    val incorrectGuesses: LiveData<String>
        get() = _incorrectGuesses
        //livesLeft
       private val _livesLeft = MutableLiveData<Int>(8)
    val livesLeft: LiveData<Int>
        get() = _livesLeft
    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    init {
        _secretWordDisplay.value = deriveSecretWordDisplay()
    }

        //this builds a string for how the secret word should be
        //displayed on the screen
        private fun deriveSecretWordDisplay(): String{
            var display = ""
            secretWord.forEach {
                //call checkletter for each letter in secretword, and
                //add its return value to the end of the display variable
                display += checkLetter(it.toString())
            }
            return display
        }

        //this checks whether the secret word contains the letter
        //the user has guessed. If so, it returns the letter.
        //If not, it returns ""
        private fun checkLetter(str:String) = when (correctGuesses.contains(str)){
            true -> str
            false -> "_"
        }

        //this gets called each time the user makes a guess
        fun makeGuess(guess:String){
            if(guess.length == 1){
                if (secretWord.contains(guess)){
                    //for each correct guess, update correctGuesses
                    //and secretWordDisplay
                    correctGuesses += guess
                    _secretWordDisplay.value = deriveSecretWordDisplay()
                }
                //for each wrong guess, update incorrectGuess and livesLeft
                else {
                    _incorrectGuesses.value += "$guess"
                    _livesLeft.value = livesLeft.value?.minus(1)

                }
                if(isWon() || isLost()) _gameOver.value = true
            }
        }

        //the game is won if the secret word matches secretWordDisplay
        private fun isWon() = secretWord.equals(secretWordDisplay.value, true)

        //the game is lost when the user runs out of lives
        private fun isLost() = livesLeft.value?: 0 <= 0

        fun wonLostMessage(): String {
            var message = ""
            if(isWon()) message = "You won!"
            else if (isLost()) message = "You lost!"
            message += "The word was $secretWord."
            //wonLostMessage() returns a string indicating whether
            //the user has won or lost, and what the secret word was.
            return message
        }

    fun finishGame(){
        _gameOver.value = true
    }

    }
