package com.example.shopy.ui.allWishListFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.adapters.WishListAdapter
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAllWishListBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import kotlinx.android.synthetic.main.activity_main.*

class AllWishListFragment : Fragment() {

    private lateinit var bindingAllWishListFragment: FragmentAllWishListBinding
    private lateinit var allWishListFragmentViewModel: AllWishListViewModel
    lateinit var withListAdapter: WishListAdapter
    lateinit var wishListData: List<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_wish_list, container, false)


        bindingAllWishListFragment = FragmentAllWishListBinding.inflate(inflater, container, false)


        val remoteDataSource = RemoteDataSourceImpl()
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
        val viewModelFactory = ViewModelFactory(repository,requireActivity().application)

        allWishListFragmentViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AllWishListViewModel::class.java]


        wishListData = ArrayList()

        withListAdapter =
            WishListAdapter(wishListData, allWishListFragmentViewModel.intentTOProductDetails,allWishListFragmentViewModel.deleteItem)
        bindingAllWishListFragment.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        requireActivity().toolbar_title.text = "All Wish List"

        allWishListFragmentViewModel.getAllWishList().observe(requireActivity(), {
            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })
//        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
//        val navController = navHostFragment.navController

        allWishListFragmentViewModel.intentTOProductDetails.observe(requireActivity(), {

            if (it != null) {
                Log.d("TAG","${it.id}")
                val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id)
                findNavController().navigate(action)

            }
        })

        return bindingAllWishListFragment.root
    }


    override fun onStop() {
        super.onStop()
        allWishListFragmentViewModel.intentTOProductDetails = MutableLiveData<Product>()
    }
}