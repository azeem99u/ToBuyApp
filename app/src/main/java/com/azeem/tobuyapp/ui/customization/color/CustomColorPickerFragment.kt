package com.azeem.tobuyapp.ui.customization.color

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.azeem.tobuyapp.BaseFragment
import com.azeem.tobuyapp.SharedPrefUtil
import com.azeem.tobuyapp.databinding.FragmentCustomColorPickerBinding
import com.azeem.tobuyapp.ui.activities.MainActivity
import java.util.*


class CustomColorPickerFragment : Fragment() {
    private var _binding: FragmentCustomColorPickerBinding? = null
    private val binding get() = _binding!!

    private val safeArgs:CustomColorPickerFragmentArgs by navArgs()
    private val viewModel:CustomColorPickerViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPriorityName(safeArgs.priorityName){ red,green,blue ->
            binding.redColorLayout.seekBar.progress = red
            binding.greenColorLayout.seekBar.progress = green
            binding.blueColorLayout.seekBar.progress = blue
        }
        binding.redColorLayout.apply {
            textView.text = "Red"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onRedChange))
        }

        binding.blueColorLayout.apply {
            textView.text = "Blue"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onBlueChange))
        }

        binding.greenColorLayout.apply {
            textView.text = "Green"
            seekBar.setOnSeekBarChangeListener(SeekBarListener(viewModel::onGreenChange))
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner){ viewState->
            binding.titleTextView.text = viewState.getFormattedTitle()
            val color = Color.rgb(viewState.red,viewState.green,viewState.blue)
            binding.colorView.setBackgroundColor(color)
        }

        binding.saveButton.setOnClickListener {
            val viewState = viewModel.viewStateLiveData.value ?: return@setOnClickListener
            val color = Color.rgb(viewState.red,viewState.green,viewState.blue)
            when(safeArgs.priorityName.lowercase(Locale.US)){
                "low" -> SharedPrefUtil.setLowPriorityColor(color)
                "medium" -> SharedPrefUtil.setMediumPriorityColor(color)
                "high" -> SharedPrefUtil.setHighPriorityColor(color)
            }
            (activity as MainActivity).navController.navigateUp()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class SeekBarListener(
    private val onProgressChange:(Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onProgressChange(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

}
