package com.azeem.tobuyapp.ui.home

import android.os.Bundle
import android.view.*
import com.airbnb.epoxy.EpoxyTouchHelper
import com.azeem.tobuyapp.R
import com.azeem.tobuyapp.database.entity.ItemEntity
import com.azeem.tobuyapp.databinding.FragmentHomeBinding
import com.azeem.tobuyapp.BaseFragment
import com.azeem.tobuyapp.ItemEntityInterface
import com.azeem.tobuyapp.ui.home.bottomsheet.SortOrderBottomSheetDialogFragment

class HomeFragment : BaseFragment(), ItemEntityInterface {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigationViewNavGraph(R.id.action_homeFragment_to_addItemFragment)
        }
        val controller = HomeEpoxyController(this)
        binding.homeEpoxyRecyclerView.setController(controller)
        shareViewModel.homeViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            controller.viewState = viewState

        }
        EpoxyTouchHelper.initSwiping(binding.homeEpoxyRecyclerView)
            .right()
            .withTarget(HomeEpoxyController.ItemEntityEpoxyModel::class.java)
            .andCallbacks(object :
                EpoxyTouchHelper.SwipeCallbacks<HomeEpoxyController.ItemEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: HomeEpoxyController.ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemThatWasRemoved = model?.itemEntity ?: return
                    shareViewModel.deleteItem(itemThatWasRemoved.itemEntity)
                }
            })
    }

    override fun onBumpPriority(itemEntity: ItemEntity) {
        val currentPriority = itemEntity.priority
        var newPriority = currentPriority + 1
        if (newPriority > 3) {
            newPriority = 1
        }
        val updatedItemEntity = itemEntity.copy(priority = newPriority)
        shareViewModel.updateItem(updatedItemEntity)
    }

    override fun onItemSelected(itemEntity: ItemEntity) {
        val navDirections =
            HomeFragmentDirections.actionHomeFragmentToAddItemFragment(itemEntity.id)
        navigationViewNavGraph(navDirections)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menuItemSort) {
            SortOrderBottomSheetDialogFragment().show(childFragmentManager, null)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }


}