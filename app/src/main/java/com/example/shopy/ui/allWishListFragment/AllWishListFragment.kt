package com.example.shopy.ui.allWishListFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.R
import com.example.shopy.adapters.WishListAdaper
import com.example.shopy.databinding.FragmentAllWishListBinding
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.ui.StringsUtils
import com.example.shopy.ui.meScreen.MeViewModel
import com.example.shopy.ui.productDetailsActivity.ProductDetailsActivity
import com.example.shopy.ui.productDetailsActivity.ProuductDetailsFragment

class AllWishListFragment : Fragment() {

    private lateinit var bindingAllWishListFragment: FragmentAllWishListBinding
    private lateinit var allWishListFragmentViewModel: AllWishListViewModel
    lateinit var withListAdapter: WishListAdaper
    lateinit var wishListData: List<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_wish_list, container, false)


        bindingAllWishListFragment = FragmentAllWishListBinding.inflate(inflater, container, false)

        allWishListFragmentViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AllWishListViewModel::class.java]


        wishListData = ArrayList()

        withListAdapter =
            WishListAdaper(wishListData, allWishListFragmentViewModel.intentTOProductDetails)
        bindingAllWishListFragment.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }


        allWishListFragmentViewModel.getAllWishList().observe(requireActivity(), {
            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })

        allWishListFragmentViewModel.intentTOProductDetails.observe(requireActivity(), {

            val nextFrag = ProuductDetailsFragment()
            val bundle  =Bundle()
            bundle.putLong(StringsUtils.OrderID, it.id)
            nextFrag.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHost, nextFrag)
                .addToBackStack(null)
                .commit()
        })

        return bindingAllWishListFragment.root
    }


    override fun onStop() {
        super.onStop()
        allWishListFragmentViewModel.intentTOProductDetails = MutableLiveData<Product>()
    }
}