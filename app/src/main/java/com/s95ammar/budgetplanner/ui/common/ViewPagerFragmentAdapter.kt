package com.s95ammar.budgetplanner.ui.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentAdapter(parentFragment: Fragment, private val fragmentsLazyList: List<Lazy<Fragment>>): FragmentStateAdapter(parentFragment) {
    override fun getItemCount() = fragmentsLazyList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsLazyList[position].value
    }

}