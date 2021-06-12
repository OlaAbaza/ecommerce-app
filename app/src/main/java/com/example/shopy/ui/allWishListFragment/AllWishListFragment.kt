package com.example.shopy.ui.allWishListFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.adapters.WishListAdapter
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAllWishListBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import kotlinx.android.synthetic.main.activity_main.*

class AllWishListFragment : Fragment() {

    private lateinit var bindingAllWishListFragment: FragmentAllWishListBinding
    private lateinit var allWishListFragmentViewModel: AllWishListViewModel
    private lateinit var withListAdapter: WishListAdapter
    private lateinit var wishListData: List<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_wish_list, container, false)


        bindingAllWishListFragment = FragmentAllWishListBinding.inflate(inflater, container, false)


        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
        val viewModelFactory = ViewModelFactory(repository,requireActivity().application)

        allWishListFragmentViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AllWishListViewModel::class.java]


        requireActivity().findViewById<View>(R.id.favourite).visibility = View.GONE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE

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

        allWishListFragmentViewModel.deleteItem.observe(viewLifecycleOwner,{
            deleteAlert(it.id)
        })

        return bindingAllWishListFragment.root
    }


    private fun deleteAlert(id: Long) {


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.Delete_Item_From_Wish_List))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setIcon(android.R.drawable.ic_delete)

        builder.setPositiveButton("Yes") { _, _ ->
            allWishListFragmentViewModel.deleteOneItemFromWishList(id)
            allWishListFragmentViewModel.deleteItem = MutableLiveData<Product>()
        }

        builder.setNegativeButton("No") { _, _ ->
            allWishListFragmentViewModel.deleteItem = MutableLiveData<Product>()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onStop() {
        super.onStop()
        allWishListFragmentViewModel.intentTOProductDetails = MutableLiveData<Product>()
    }
}