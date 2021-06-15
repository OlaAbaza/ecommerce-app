package com.example.shopy.ui.shopTab

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.shopy.domainLayer.Utils
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.fragment_shop_tab.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*

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
        requireActivity().toolbar_title.text = "Home"

        setUpTabBar()
        changeToolbar()

    }


    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.VISIBLE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.cartView).cartButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().settingIcon.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.BLACK)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(null)
//        requireActivity().toolbar.setNavigationOnClickListener {
//            view?.findNavController()?.popBackStack()
//        }
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