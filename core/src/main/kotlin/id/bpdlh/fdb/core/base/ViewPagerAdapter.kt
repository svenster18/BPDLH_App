package id.bpdlh.fdb.core.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter<T : Fragment>(activity: FragmentActivity, private val items: List<T>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = items[position]
}