package edu.quinnipiac.edu.ser210.guessinggamech11viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import edu.quinnipiac.edu.ser210.guessinggamech11viewmodel.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    //properties for view binding
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel:GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root
        //makes a viewModel provider and only makes a fresh view model if theres not an existing one in the UI controller
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        //set the data binding variable
        binding.gameViewModel = viewModel
        //to respond to live data updates
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.gameOver.observe(viewLifecycleOwner, Observer{newValue ->
            if(newValue){
                val action = GameFragmentDirections
                    .actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                view.findNavController().navigate(action)
            }
        })
        binding.guessButton.setOnClickListener() {
            //call makeGuess to deal with the users guess
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            //reset the edit text and update the screen
            binding.guess.text = null
        }
        return view
    }

    //when the fragment no longer has access to its layout,
    //set the _binding property to null
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}