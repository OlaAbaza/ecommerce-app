package com.example.shopy.ui.meScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.ui.allWishListFragment.WishListAdapter
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentMeBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.domainLayer.Utils
import kotlinx.android.synthetic.main.activity_main.*


class MeFragment : Fragment() {


    private lateinit var bindingMeScreen: FragmentMeBinding
    private lateinit var withListAdapter: WishListAdapter
    private lateinit var wishListData: List<Product>
    private lateinit var meViewModel: MeViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingMeScreen = FragmentMeBinding.inflate(inflater, container, false)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())

        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
        val viewModelFactory = ViewModelFactory(repository,requireActivity().application)
        meViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MeViewModel::class.java]

        handelViability()

        wishListData = ArrayList()

//        requireActivity().toolbar.visibility = View.VISIBLE
//        requireActivity().bottom_nav.visibility = View.VISIBLE
        if(Utils.toolbarImg.visibility == View.GONE){
            Utils.toolbarImg.visibility = View.VISIBLE
        }
        if(Utils.cartView.visibility == View.GONE){
            Utils.cartView.visibility = View.VISIBLE
        }
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.text = getString(R.string.me)
        bindingMeScreen.regesterAndLogin.setOnClickListener {
            val action = NavGraphDirections.actionGlobalSignInFragment()
            findNavController().navigate(action)
        }


        withListAdapter =
            WishListAdapter(wishListData, meViewModel.intentTOProductDetails, meViewModel.deleteItem)
        bindingMeScreen.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }


        meViewModel.getFourWishList().observe(requireActivity(), {
            if (it.isEmpty())
                bindingMeScreen.emptyAnimationView.visibility=View.VISIBLE
            else
                bindingMeScreen.emptyAnimationView.visibility=View.GONE

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

        meViewModel.deleteItem.observe(viewLifecycleOwner, {
            deleteAlert(it.id)
        })


        return bindingMeScreen.root
    }

    @SuppressLint("LogNotTimber", "SetTextI18n")
    private fun handelViability() {
        if (isLoged()) {
            bindingMeScreen.unPaied.setOnClickListener {
                findNavController().navigate(NavGraphDirections.actionGlobalDisplayOrderFragment(1))
            }
            bindingMeScreen.paidLayout.setOnClickListener {
                findNavController().navigate(NavGraphDirections.actionGlobalDisplayOrderFragment(0))
            }


            if (NetworkChangeReceiver.isOnline) {
                meViewModel.getPaidOrders(meDataSourceReo.loadUsertId().toLong())
                meViewModel.getUnPaidOrders(meDataSourceReo.loadUsertId().toLong())

                meViewModel.paidOrders.observe(viewLifecycleOwner, {
                    bindingMeScreen.paidNumbers.text = it.toString()
                })
                meViewModel.unPaidOrders.observe(viewLifecycleOwner, {
                    bindingMeScreen.UnPaidNumbers.text = it.toString()
                })
            }else{
                Toast.makeText(requireContext(),"There is no network",Toast.LENGTH_SHORT).show()
            }

            bindingMeScreen.hiText.text = "Hi! ${meDataSourceReo.loadUsertName()}"
            bindingMeScreen.hiText.visibility = View.VISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.VISIBLE
            bindingMeScreen.regesterAndLogin.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.VISIBLE
            bindingMeScreen.seeAllText.visibility = View.VISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.VISIBLE
            bindingMeScreen.pleaseLogIn.visibility=View.INVISIBLE
            bindingMeScreen.UnPaidNumbers.visibility=View.VISIBLE
            bindingMeScreen.paidNumbers.visibility=View.VISIBLE
        } else {
            bindingMeScreen.regesterAndLogin.visibility = View.VISIBLE
            bindingMeScreen.hiText.visibility = View.INVISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.INVISIBLE
            bindingMeScreen.seeAllText.visibility = View.INVISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.INVISIBLE
            bindingMeScreen.pleaseLogIn.visibility=View.VISIBLE
            bindingMeScreen.UnPaidNumbers.visibility=View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility=View.INVISIBLE
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingMeScreen.points.setOnClickListener {
            findNavController().navigate(MeFragmentDirections.actionMeFragmentToSettingsFragment())
        }
    }

}