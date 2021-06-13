package com.example.shopy.ui.shopTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shopy.R
import com.example.shopy.ui.shopTab.shopTabCategories.KidsProductFragment
import com.example.shopy.ui.shopTab.shopTabCategories.MenProductsFragment
import com.example.shopy.ui.shopTab.shopTabCategories.OnSaleProductFragment
import com.example.shopy.ui.shopTab.shopTabCategories.WomanProductsFragment
import com.example.shopy.util.Utils
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shop_tab.*

class ShopTabFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_shop_tab, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        requireActivity().toolbar.visibility = View.VISIBLE
//        requireActivity().bottom_nav.visibility = View.VISIBLE
        if(Utils.toolbarImg.visibility == View.GONE){
            Utils.toolbarImg.visibility = View.VISIBLE
        }
        if(Utils.cartView.visibility == View.GONE){
            Utils.cartView.visibility = View.VISIBLE
        }
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        setUpTabBar()

    }

    private fun setUpTabBar() {
        val adapter = FragmentPagerItemAdapter(
            childFragmentManager,
            FragmentPagerItems.with(activity)
                .add("Woman", WomanProductsFragment::class.java)
                .add("Kids", KidsProductFragment::class.java)
                .add("Men", MenProductsFragment::class.java)
                .add("On Sale", OnSaleProductFragment::class.java)

                .create()
        )

        viewpager.adapter = adapter
        viewpagertab.setViewPager(viewpager)
    }




}