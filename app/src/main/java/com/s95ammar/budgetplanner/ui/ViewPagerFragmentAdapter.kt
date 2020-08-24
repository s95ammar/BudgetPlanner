package com.s95ammar.budgetplanner.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentAdapter(parentFragment: Fragment, private val childFragmentsProviders: List<FragmentProvider>): FragmentStateAdapter(parentFragment) {
    override fun getItemCount() = childFragmentsProviders.size

    override fun createFragment(position: Int): Fragment {
        return childFragmentsProviders[position].provideFragment()
    }

}