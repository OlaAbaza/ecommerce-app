package com.example.shopy.ui.meScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.adapters.WishListAdaper
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.ui.StringsUtils
import com.example.shopy.ui.allWishListFragment.AllWishListFragment
import com.example.shopy.ui.productDetailsActivity.ProuductDetailsFragment


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

        withListAdapter= WishListAdaper(wishListData, meViewModel.intentTOProductDetails)
        bindingMeScreen.wishRecyclerView.apply{
            this.adapter = withListAdapter
            this.layoutManager =  GridLayoutManager(requireContext(), 2)
        }


        requireActivity().title = resources.getString(R.string.app_name)

        meViewModel.getFourWishList().observe(requireActivity(), {
            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })


        meViewModel.intentTOProductDetails.observe(requireActivity(), {
            if (it!= null) {
//                val nextFrag = ProuductDetailsFragment()
//                val bundle  =Bundle()
//                bundle.putLong(StringsUtils.OrderID, it.id)
//                nextFrag.arguments = bundle
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment, nextFrag)
//                    .addToBackStack(null)
//                    .commit()

                val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id)
                findNavController().navigate(action)
            }

        })

        bindingMeScreen.seeAllText.setOnClickListener {
            startAnotherFragment()
        }
        bindingMeScreen.seeAllArrow.setOnClickListener {
        startAnotherFragment()
        }


        return bindingMeScreen.root
    }

    override fun onStop() {
        super.onStop()
        meViewModel.intentTOProductDetails = MutableLiveData<Product>()
    }

    private fun startAnotherFragment() {

//        val nextFrag = AllWishListFragment()
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment, nextFrag)
//            .addToBackStack(null)
//            .commit()

//        val action = NavGraphDirections.actionGlobalHoursFragment(weather!!)
//        findNavController().navigate(action)

        val action = NavGraphDirections.actionGlobalAllWishListFragment()
        findNavController().navigate(action)
    }

}