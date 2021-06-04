package com.example.shopy.ui.meScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.adapters.WishListAdaper
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.ui.productDetailsActivity.ProductDetailsActivity


class MeFragment : Fragment() {
lateinit var bindingMeScreen : FragmentMeBinding
lateinit var withListAdapter : WishListAdaper
lateinit var wishListData : List<Product>
lateinit var meViewModel : MeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingMeScreen= FragmentMeBinding.inflate(inflater, container, false)
        meViewModel =  ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MeViewModel::class.java]
        wishListData = ArrayList()
        withListAdapter= WishListAdaper(wishListData,meViewModel)
        bindingMeScreen.wishRecyclerView.apply{
            this.adapter = withListAdapter
            this.layoutManager =  GridLayoutManager(requireContext(), 2)
        }

        meViewModel.getAllData().observe(requireActivity(),{
            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })


        meViewModel.intentTOProductDetails.observe(requireActivity(),{
             val intent = Intent(activity, ProductDetailsActivity::class.java)
             intent.putExtra("ID",it.id)
            startActivity(intent)
        })



        return bindingMeScreen.root
    }

}