package com.s95ammar.budgetplanner.ui.appscreens.categories

import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCategoriesBinding
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesListAdapter
import com.s95ammar.budgetplanner.ui.appscreens.categories.common.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.data.CategoriesUiEvent
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.LoadingState
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BaseFragment(R.layout.fragment_categories), ViewBinder<FragmentCategoriesBinding> {

    override val binding: FragmentCategoriesBinding
        get() = getBinding()

    private val viewModel: CategoriesViewModel by viewModels()
    private val adapter by lazy { CategoriesListAdapter(viewModel::onCategoryItemClick, viewModel::onCategoryItemLongClick) }

    override fun initViewBinding(view: View): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.bind(view)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.fab.setOnClickListener { viewModel.onNavigateToCreateCategory() }
        binding.swipeToRefreshLayout.setOnRefreshListener { viewModel.refresh() }
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

    override fun showLoading() {
        binding.swipeToRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeToRefreshLayout.isRefreshing = false
    }

    private fun setAllCategories(categories: List<Category>) {
        adapter.submitList(categories) { binding.recyclerView.scrollToPosition(0) }
    }

    private fun performUiEvent(uiEvent: CategoriesUiEvent) {
        when (uiEvent) {
            is CategoriesUiEvent.DisplayLoadingState -> handleLoadingState(uiEvent.loadingState)
            is CategoriesUiEvent.OnNavigateToEditCategory -> onNavigateToCreateEditCategory(uiEvent.categoryId)
            is CategoriesUiEvent.ShowBottomSheet -> showBottomSheet(uiEvent.category)
        }
    }

    private fun handleLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            is LoadingState.Cold,
            is LoadingState.Success -> hideLoading()
            is LoadingState.Loading -> showLoading()
            is LoadingState.Error -> {
                hideLoading()
                showErrorToast(loadingState.throwable)
            }
        }
    }

    private fun onNavigateToCreateEditCategory(categoryId: Int) {
        setFragmentResultListener(Keys.KEY_ON_CATEGORY_CREATE_EDIT) { _, _ -> viewModel.refresh() }
        navController.navigate(
            CategoriesFragmentDirections.actionNavigationCategoriesToCategoryCreateEditFragment(categoryId)
        )
    }

    private fun showBottomSheet(category: Category) {
        EditDeleteBottomSheetDialogFragment.newInstance(category.name, R.drawable.ic_category).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = onNavigateToCreateEditCategory(category.id)
                override fun onDelete() = displayDeleteConfirmationDialog(category.name) { viewModel.deleteCategory(category.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

}
