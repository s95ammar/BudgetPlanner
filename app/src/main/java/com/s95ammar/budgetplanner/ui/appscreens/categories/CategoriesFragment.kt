package com.s95ammar.budgetplanner.ui.appscreens.categories

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCategoriesBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesAdapter
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.BaseViewBinderFragment
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import com.s95ammar.budgetplanner.ui.appscreens.categories.data.CategoriesUiEvent as UiEvent

@AndroidEntryPoint
class CategoriesFragment : BaseViewBinderFragment<FragmentCategoriesBinding>(R.layout.fragment_categories) {

    private val viewModel: CategoriesViewModel by viewModels()
    private val adapter by lazy { CategoriesAdapter(viewModel::onCategoryItemClick, viewModel::onCategoryItemLongClick) }

    override fun initViewBinding(view: View): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.fab.setOnClickListener { viewModel.onNavigateToCreateCategory() }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.allCategories.observe(viewLifecycleOwner) { setAllCategories(it) }
        viewModel.performUiEvent.observeEvent(viewLifecycleOwner) { performUiEvent(it) }
    }

    private fun setAllCategories(categories: List<Category>) {
        adapter.submitList(categories)
        binding.recyclerView.isGone = categories.isEmpty()
        binding.instructionsLayout.root.isVisible = categories.isEmpty()
        binding.instructionsLayout.messageTextView.text = getString(R.string.create_category_instruction)
    }

    private fun performUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is UiEvent.ListenAndNavigateToEditCategory -> listenAndNavigateToCreateEditCategory(uiEvent.categoryId)
            is UiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.category)
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorSnackbar(loadingState.throwable)
            }
        }
    }

    private fun listenAndNavigateToCreateEditCategory(categoryId: Int) {
        setFragmentResultListener(Keys.KEY_ON_CATEGORY_CREATE_EDIT) { _, _ -> viewModel.refresh() }
        navController.navigate(
            CategoriesFragmentDirections.actionNavigationCategoriesToCategoryCreateEditFragment(categoryId)
        )
    }

    private fun showBottomSheet(category: Category) {
        EditDeleteBottomSheetDialogFragment.newInstance(category.name, R.drawable.ic_category).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = listenAndNavigateToCreateEditCategory(category.id)
                override fun onDelete() = displayDeleteConfirmationDialog(category.name) { viewModel.deleteCategory(category.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

}
