package com.coop5.coopvirtual
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    // Lista de fragmentos que se mostrarán en el ViewPager
    private val fragmentList = mutableListOf<Fragment>()

    // Agrega un fragmento a la lista
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    // Retorna el fragmento en la posición dada
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // Retorna la cantidad de fragmentos en la lista
    override fun getCount(): Int {
        return fragmentList.size
    }
}
