package com.example.shopy.ui.meScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.adapters.WishListAdaper
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory


class MeFragment : Fragment() {


    private lateinit var bindingMeScreen: FragmentMeBinding
    private lateinit var withListAdapter: WishListAdaper
    private lateinit var wishListData: List<Product>
    private lateinit var meViewModel: MeViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingMeScreen = FragmentMeBinding.inflate(inflater, container, false)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        handelVisability()
        val remoteDataSource = RemoteDataSourceImpl()
        val viewModelFactory = ViewModelFactory(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
            ), remoteDataSource, requireActivity().application
        )
        meViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MeViewModel::class.java]


        wishListData = ArrayList()



        bindingMeScreen.regesterAndLogin.setOnClickListener {
            val action = NavGraphDirections.actionGlobalSignInFragment()
            findNavController().navigate(action)
        }


        withListAdapter =
            WishListAdaper(wishListData, meViewModel.intentTOProductDetails, meViewModel.deleteItem)
        bindingMeScreen.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }


        meViewModel.getFourWishList().observe(requireActivity(), {
            wishListData = it
            withListAdapter.productList = wishListData
            withListAdapter.notifyDataSetChanged()
        })


        meViewModel.intentTOProductDetails.observe(requireActivity(), {
            if (it != null) {
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


        bindingMeScreen.unPaied.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalDisplayOrderFragment(1))
        }
        bindingMeScreen.paidLayout.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalDisplayOrderFragment(0))
        }


        meViewModel.deleteItem.observe(viewLifecycleOwner, {
            deleteAlert(it.id)
        })


        meViewModel.getPaidOrders(meDataSourceReo.loadUsertId().toLong())
        meViewModel.getUnPaidOrders(meDataSourceReo.loadUsertId().toLong())
        meViewModel.paidOrders.observe(viewLifecycleOwner, {
            bindingMeScreen.paidNumbers.text = it.toString()
        })
        meViewModel.unPaidOrders.observe(viewLifecycleOwner, {
            bindingMeScreen.UnPaidNumbers.text = it.toString()
        })


        return bindingMeScreen.root
    }

    @SuppressLint("LogNotTimber", "SetTextI18n")
    private fun handelVisability() {
        if (isLoged()) {
            bindingMeScreen.hiText.text = "Hi! ${meDataSourceReo.loadUsertName()}"
            bindingMeScreen.hiText.visibility = View.VISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.VISIBLE
            bindingMeScreen.regesterAndLogin.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.VISIBLE
            bindingMeScreen.seeAllText.visibility = View.VISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.VISIBLE
            bindingMeScreen.pleaseLogIn.visibility=View.INVISIBLE
        } else {
            bindingMeScreen.regesterAndLogin.visibility = View.VISIBLE
            bindingMeScreen.hiText.visibility = View.INVISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.INVISIBLE
            bindingMeScreen.seeAllText.visibility = View.INVISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.INVISIBLE
            bindingMeScreen.pleaseLogIn.visibility=View.VISIBLE

        }
    }

    private fun isLoged(): Boolean {
        return meDataSourceReo.loadUsertstate()
    }

    override fun onStop() {
        super.onStop()
        meViewModel.intentTOProductDetails = MutableLiveData<Product>()
        meViewModel.deleteItem = MutableLiveData<Product>()
    }

    private fun startAnotherFragment() {
        val action = NavGraphDirections.actionGlobalAllWishListFragment()
        findNavController().navigate(action)
    }

    private fun deleteAlert(id: Long) {


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.Delete_Item_From_Wish_List))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setIcon(android.R.drawable.ic_delete)

        builder.setPositiveButton("Yes") { _, _ ->
            meViewModel.deleteOneItemFromWishList(id)
            meViewModel.deleteItem = MutableLiveData<Product>()
        }

        builder.setNegativeButton("No") { _, _ ->
            meViewModel.deleteItem = MutableLiveData<Product>()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}