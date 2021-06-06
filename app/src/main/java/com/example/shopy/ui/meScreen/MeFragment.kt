package com.example.shopy.ui.meScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.shopy.datalayer.localdatabase.sharedPrefrence.MeDataSharedPrefrenceReposatory
import kotlinx.android.synthetic.main.activity_main.*


class MeFragment : Fragment() {


    lateinit var bindingMeScreen: FragmentMeBinding
    lateinit var withListAdapter: WishListAdaper
    lateinit var wishListData: List<Product>
    lateinit var meViewModel: MeViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingMeScreen = FragmentMeBinding.inflate(inflater, container, false)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())


        handelVisability()

//        bindingMeScreen.regesterAndLogin.visibility=View.VISIBLE

        meViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MeViewModel::class.java]
        wishListData = ArrayList()

        requireActivity().toolbar_title.text = "Me"

        bindingMeScreen.regesterAndLogin.setOnClickListener {
            val action = NavGraphDirections.actionGlobalSignInFragment()
            findNavController().navigate(action)
        }


        withListAdapter = WishListAdaper(wishListData, meViewModel.intentTOProductDetails)
        bindingMeScreen.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }


        requireActivity().title = resources.getString(R.string.app_name)

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
            meDataSourceReo.saveUsertState(false)
            meDataSourceReo.saveUsertName("")
            meDataSourceReo.saveUsertId("")
            handelVisability()
//            AuthUI.getInstance().signOut(requireContext())
        }


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
    }

    private fun startAnotherFragment() {
        val action = NavGraphDirections.actionGlobalAllWishListFragment()
        findNavController().navigate(action)
    }

}