package com.s95ammar.budgetplanner.ui.appscreens.categories

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.s95ammar.budgetplanner.R
import com.s95ammar.budgetplanner.models.Resource
import com.s95ammar.budgetplanner.models.Result
import com.s95ammar.budgetplanner.models.data.Category
import com.s95ammar.budgetplanner.ui.appscreens.categories.adapter.CategoriesListAdapter
import com.s95ammar.budgetplanner.ui.base.BaseFragment
import com.s95ammar.budgetplanner.ui.common.Keys
import com.s95ammar.budgetplanner.ui.common.bottomsheet.EditDeleteBottomSheetDialogFragment
import com.s95ammar.budgetplanner.util.NO_ITEM
import com.s95ammar.budgetplanner.util.lifecycleutil.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_categories.*

@AndroidEntryPoint
class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {

    private val viewModel: CategoriesViewModel by viewModels()
    private val adapter by lazy { CategoriesListAdapter(viewModel::onCategoryItemClick, viewModel::onCategoryItemLongClick) }

    override fun setUpViews() {
        super.setUpViews()
        fab_categories.setOnClickListener { navigateToCreateEditCategory(Int.NO_ITEM) }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recycler_view_categories_list.adapter = adapter
        recycler_view_categories_list.setHasFixedSize(true)
        recycler_view_categories_list.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.allCategories.observe(viewLifecycleOwner) { handleAllCategoriesLoading(it) }
        viewModel.navigateToEditCategory.observeEvent(viewLifecycleOwner) { navigateToCreateEditCategory(it) }
        viewModel.showBottomSheet.observeEvent(viewLifecycleOwner) { showBottomSheet(it) }
        viewModel.displayDeleteResultState.observeEvent(viewLifecycleOwner) { displayDeleteResultState(it) }
    }

    private fun handleAllCategoriesLoading(allCategoriesResource: Resource<List<Category>>?) {
        when (allCategoriesResource) {
            is Resource.Loading -> showLoading()
            is Resource.Error -> {
                hideLoading()
                displayErrorDialog(allCategoriesResource.throwable)
            }
            is Resource.Success -> {
                hideLoading()
                adapter.submitList(allCategoriesResource.data) { recycler_view_categories_list?.scrollToPosition(0) }
            }
        }
    }

    private fun displayDeleteResultState(result: Result) {
        when (result) {
            is Result.Loading -> showLoading()
            is Result.Error -> {
                hideLoading()
                displayErrorDialog(result.throwable)
            }
            is Result.Success -> hideLoading()
        }
    }

    private fun navigateToCreateEditCategory(categoryId: Int) {
        navController.navigate(
            R.id.action_navigation_categories_to_categoryCreateEditFragment,
            bundleOf(Keys.KEY_CATEGORY_ID to categoryId)
        )
    }

    private fun showBottomSheet(category: Category) {
        EditDeleteBottomSheetDialogFragment.newInstance(category.name, R.drawable.ic_category).apply {
            listener = object : EditDeleteBottomSheetDialogFragment.Listener {
                override fun onEdit() = navigateToCreateEditCategory(category.id)
                override fun onDelete() = displayDeleteConfirmationDialog(category.name) { viewModel.onDeleteCategory(category.id) }
            }
        }.show(childFragmentManager, EditDeleteBottomSheetDialogFragment.TAG)
    }

}