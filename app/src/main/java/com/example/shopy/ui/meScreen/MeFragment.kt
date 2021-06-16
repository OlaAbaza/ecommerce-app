package com.example.shopy.ui.meScreen

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.favouriteButton


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

        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
        )
        val viewModelFactory = ViewModelFactory(repository, requireActivity().application)
        meViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[MeViewModel::class.java]

        handelViability()

        wishListData = ArrayList()

//        requireActivity().toolbar.visibility = View.VISIBLE
//        requireActivity().bottom_nav.visibility = View.VISIBLE
//        if(Utils.toolbarImg.visibility == View.GONE){
//            Utils.toolbarImg.visibility = View.VISIBLE
//        }
//        if(Utils.cartView.visibility == View.GONE){
//            Utils.cartView.visibility = View.VISIBLE
//        }

        bindingMeScreen.regesterAndLogin.setOnClickListener {
            val action = NavGraphDirections.actionGlobalSignInFragment()
            findNavController().navigate(action)
        }


        withListAdapter =
            WishListAdapter(
                wishListData,
                meViewModel.intentTOProductDetails,
                meViewModel.deleteItem
            )
        bindingMeScreen.wishRecyclerView.apply {
            this.adapter = withListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        bindingMeScreen.goHomeBtn.setOnClickListener {
            val action = NavGraphDirections.actionGlobalShopTabFragment2()
            findNavController().navigate(action)
        }
        meViewModel.getFourWishList().observe(requireActivity(), {
            if (it.isEmpty() && (isLoged())) {
                bindingMeScreen.emptyStateGroup.visibility = View.VISIBLE
                bindingMeScreen.emptyStateGroup1.visibility = View.VISIBLE
            } else if (!(isLoged())) {
                bindingMeScreen.emptyStateGroup1.visibility = View.VISIBLE
                bindingMeScreen.tvLogged.visibility = View.VISIBLE
            } else {
                bindingMeScreen.emptyStateGroup.visibility = View.GONE
                bindingMeScreen.emptyStateGroup1.visibility = View.GONE
            }

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
                    if (it == 0)
                        bindingMeScreen.paidNumbers.visibility = View.INVISIBLE
                    else {
                        bindingMeScreen.paidNumbers.text = it.toString()
                        bindingMeScreen.paidNumbers.visibility = View.VISIBLE
                    }
                })
                meViewModel.unPaidOrders.observe(viewLifecycleOwner, {
                    if (it == 0)
                        bindingMeScreen.UnPaidNumbers.visibility = View.INVISIBLE
                    else {
                        bindingMeScreen.UnPaidNumbers.text = it.toString()
                        bindingMeScreen.UnPaidNumbers.visibility = View.VISIBLE
                    }

                })
            } else {
                Toast.makeText(requireContext(), "There is no network", Toast.LENGTH_SHORT).show()
            }

            bindingMeScreen.hiText.text = "Hi, ${meDataSourceReo.loadUsertName()}"
            bindingMeScreen.hiText.visibility = View.VISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.VISIBLE
            bindingMeScreen.regesterAndLogin.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.VISIBLE
            bindingMeScreen.seeAllText.visibility = View.VISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.VISIBLE
            bindingMeScreen.tvLogged.visibility = View.INVISIBLE
            bindingMeScreen.UnPaidNumbers.visibility = View.VISIBLE
            bindingMeScreen.paidNumbers.visibility = View.VISIBLE
            bindingMeScreen.emptyStateGroup.visibility = View.INVISIBLE
            bindingMeScreen.emptyStateGroup1.visibility = View.INVISIBLE
        } else {
            bindingMeScreen.regesterAndLogin.visibility = View.VISIBLE
            bindingMeScreen.hiText.visibility = View.INVISIBLE
            bindingMeScreen.wishRecyclerView.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.INVISIBLE
            bindingMeScreen.seeAllText.visibility = View.INVISIBLE
            bindingMeScreen.seeAllArrow.visibility = View.INVISIBLE
            bindingMeScreen.emptyStateGroup.visibility = View.INVISIBLE
            bindingMeScreen.emptyAnimationView.visibility = View.VISIBLE
            bindingMeScreen.emptyStateGroup1.visibility = View.VISIBLE
            bindingMeScreen.tvLogged.visibility = View.VISIBLE
            bindingMeScreen.UnPaidNumbers.visibility = View.INVISIBLE
            bindingMeScreen.paidNumbers.visibility = View.INVISIBLE
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        bindingMeScreen.points.setOnClickListener {
//            findNavController().navigate(MeFragmentDirections.actionMeFragmentToSettingsFragment())
//        }
        requireActivity().findViewById<View>(R.id.settingIcon).setOnClickListener {
            findNavController().navigate(MeFragmentDirections.actionMeFragmentToSettingsFragment())
        }
        changeToolbar()
    }

    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.VISIBLE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.setColorFilter(
            getResources().getColor(R.color.black)
        )
        requireActivity().findViewById<View>(R.id.cartView).cartButton.setColorFilter(
            getResources().getColor(
                R.color.black
            )
        )
        requireActivity().settingIcon.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.BLACK)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(null)
        requireActivity().toolbar_title.text = ""

    }

    private fun deleteAlert(id: Long) {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.Delete_Item_From_Wish_List))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setPositiveButton("Delete") { _, _ ->
            meViewModel.deleteOneItemFromWishList(id)
            meViewModel.deleteItem = MutableLiveData<Product>()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            meViewModel.deleteItem = MutableLiveData<Product>()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK)
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY)
    }
}