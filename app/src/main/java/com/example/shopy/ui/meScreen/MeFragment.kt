package com.example.shopy.ui.meScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_main.*


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
        val viewModelFactory = ViewModelFactory(Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
            ,remoteDataSource,requireActivity().application)
        meViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MeViewModel::class.java]


        wishListData = ArrayList()


        requireActivity().toolbar_title.text = getString(R.string.me)

        bindingMeScreen.regesterAndLogin.setOnClickListener {
            val action = NavGraphDirections.actionGlobalSignInFragment()
            findNavController().navigate(action)
        }


        withListAdapter = WishListAdaper(wishListData, meViewModel.intentTOProductDetails,meViewModel.deleteItem)
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
//            val action = NavGraphDirections.actionGlobalDisplayOrderFragment()
            findNavController().navigate(NavGraphDirections.actionGlobalDisplayOrderFragment())
//            meDataSourceReo.saveUsertState(false)
//            meDataSourceReo.saveUsertName("")
//            meDataSourceReo.saveUsertId("")
//            handelVisability()
//            AuthUI.getInstance().signOut(requireContext())
        }


        meViewModel.deleteItem.observe(viewLifecycleOwner,{
            deleteAlert(it.id)
        })


        return bindingMeScreen.root
    }

    @SuppressLint("LogNotTimber")
    private fun handelVisability() {
        if (isLoged()){
            bindingMeScreen.hiText.text = meDataSourceReo.loadUsertName()
            bindingMeScreen.hiText.visibility = View.VISIBLE
            bindingMeScreen.wishRecyclerView.visibility= View.VISIBLE
            bindingMeScreen.regesterAndLogin.visibility=View.INVISIBLE
            Log.d("TAG","VISIBLE")
        }else{
            Log.d("TAG","GONE")
            bindingMeScreen.regesterAndLogin.visibility=View.VISIBLE
            bindingMeScreen.hiText.visibility = View.INVISIBLE
            bindingMeScreen.wishRecyclerView.visibility= View.INVISIBLE
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

    private fun deleteAlert(id: Long){


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.Delete_Item_From_Wish_List))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){_, _ ->
            meViewModel.deleteOneItemFromWishList(id)
                    meViewModel.deleteItem = MutableLiveData<Product>()
        }

        builder.setNegativeButton("No"){_, _ ->
            meViewModel.deleteItem = MutableLiveData<Product>()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}