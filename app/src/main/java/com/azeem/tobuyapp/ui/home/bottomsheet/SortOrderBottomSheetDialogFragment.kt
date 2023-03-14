package com.azeem.tobuyapp.ui.home.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.arch.ToBuyViewModel
import com.azeem.tobuyapp.databinding.FragmentHomeBinding
import com.azeem.tobuyapp.databinding.FragmentSortOrderBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortOrderBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortOrderBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel:ToBuyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSortOrderBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val epoxyController = BottomSheetEpoxyController(ToBuyViewModel.HomeViewState.Sort.values()){
            viewModel.currentSort = it
            dismiss()
        }
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}