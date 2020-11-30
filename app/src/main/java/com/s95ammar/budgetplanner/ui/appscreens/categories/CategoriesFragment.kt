package com.s95ammar.budgetplanner.ui.appscreens.categories

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.databinding.FragmentCategoriesBinding
import com.s95ammar.budgetplanner.models.view.CategoryViewEntity
import com.s95ammar.budgetplanner.ui.appscreens.auth.common.LoadingState
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesListAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.ui.common.viewbinding.ViewBinder
import com.s95ammar.budgetplanner.util.NO_ITEM
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
        binding.fabCategories.setOnClickListener { navigateToCreateEditCategory(Int.NO_ITEM) }
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
        viewModel.displayLoadingState.observeEvent(viewLifecycleOwner) { handleLoadingState(it) }
        viewModel.navigateToEditCategory.observeEvent(viewLifecycleOwner) { navigateToCreateEditCategory(it) }
        viewModel.showBottomSheet.observeEvent(viewLifecycleOwner) { showBottomSheet(it) }
        observeResultLiveData<Boolean>(Keys.KEY_ON_CATEGORY_CREATE_EDIT) { viewModel.refresh() }
    }

    override fun showLoading() {
        binding.swipeToRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeToRefreshLayout.isRefreshing = false
    }

    private fun setAllCategories(categories: List<CategoryViewEntity>) {
        adapter.submitList(categories) { binding.recyclerView.scrollToPosition(0) }
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

    private fun navigateToCreateEditCategory(categoryId: Int) {
        navController.navigate(
            CategoriesFragmentDirections.actionNavigationCategoriesToCategoryCreateEditFragment(categoryId)
        )
    }

    private fun showBottomSheet(category: CategoryViewEntity) {
        EditDeleteBottomSheetDialogFragment.newInstance(category.name, R.drawable.ic_category).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = navigateToCreateEditCategory(category.id)
                override fun onDelete() = displayDeleteConfirmationDialog(category.name) { viewModel.onDeleteCategory(category.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

}
