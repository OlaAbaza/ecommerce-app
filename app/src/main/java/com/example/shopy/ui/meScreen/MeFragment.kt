package com.example.shopy.ui.meScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.adapters.WishListAdaper
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product


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
        withListAdapter= WishListAdaper(wishListData)
        bindingMeScreen.wishRecyclerView.apply{
            this.adapter = withListAdapter
            this.layoutManager =  GridLayoutManager(requireContext(), 2)
        }

        meViewModel.getAllData().observe(requireActivity(),{
            wishListData = it
            withListAdapter.jobList = wishListData
            withListAdapter.notifyDataSetChanged()
        })

        return bindingMeScreen.root
    }

}