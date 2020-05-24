package io.github.mikolasan.imperialrussia

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException

const val CONVERTER_PAGE_ID = 0
const val UNIT_LIST_PAGE_ID = 1

class ImperialPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val pagesCreators: Map<Int, () -> Fragment> = mapOf(
            CONVERTER_PAGE_ID to { ConverterFragment() },
            UNIT_LIST_PAGE_ID to {
                val f = UnitListFragment()
                f.listAdapter = (fragmentActivity as MainActivity).listAdapter
                f
            }
    )

    override fun getItemCount(): Int = pagesCreators.size

    override fun createFragment(position: Int): Fragment {
        return pagesCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}