package com.example.shopy.ui.displayOrderFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.StringsUtils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentDisplayOrderBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.domainLayer.FilterData
import com.example.shopy.ui.payment.Checkout_Activity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.Serializable
import java.lang.ref.WeakReference


class DisplayOrderFragment : Fragment() {

    private var tabId: Int = 0
    private var userID: Long = 0
    private lateinit var displayOrderViewModel: DisplayOrderViewModel
    private var imagesOrders: MutableList<List<String>> = arrayListOf()

    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: FragmentDisplayOrderBinding = FragmentDisplayOrderBinding.inflate(
            inflater,
            container,
            false
        )


        val viewModelFactory = ViewModelFactory(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
            ), requireActivity().application
        )


        displayOrderViewModel = WeakReference(
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[DisplayOrderViewModel::class.java]
        ).get()!!


        val meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        userID = meDataSourceReo.loadUsertId().toLong()


        if (savedInstanceState == null) {
            userID = meDataSourceReo.loadUsertId().toLong()
            val args: DisplayOrderFragmentArgs by navArgs()
            tabId = args.tabID
        } else {

            userID = savedInstanceState.getLong(StringsUtils.userID)
            tabId = savedInstanceState.getInt(StringsUtils.tabID)
        }

        view.tapLayout.getTabAt(tabId)?.select()

        //make new call to update view with the new data
        callOrders(displayOrderViewModel, view)
        var adapterImages  = WeakReference(ProductsImageAdapter(emptyList())).get()!!
        val adapter = WeakReference(
            OrderDisplayAdapter(adapterImages,requireContext(),
                ArrayList(), listOf(),
                displayOrderViewModel.payNowMutableData,
                displayOrderViewModel.cancelMutableData
            )
        ).get()


        view.orderRecycler.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        changeToolbar()




        displayOrderViewModel.orders.observe(viewLifecycleOwner, { it ->

            adapter?.list = it
            view.progressPar.visibility = View.GONE

            if (it.isEmpty()) {
                view.emptyGroup.visibility = View.VISIBLE
            } else {
                view.emptyGroup.visibility = View.GONE
            }
            val prouductsId = FilterData.ProductsIDs(it)
            displayOrderViewModel.getProductAllProuducts().observe(viewLifecycleOwner, {
             val data  = FilterData.getListOfImage(prouductsId,it.products)

                adapter!!.imagelist = data
            })

        })



        view.tapLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if (NetworkChangeReceiver.isOnline) {
                    view.progressPar.visibility = View.VISIBLE
                    when (tab.position) {
                        0 -> {
                            displayOrderViewModel.getPaidOrders(userID)
                        }
                        1 -> {
                            displayOrderViewModel.getUnPaidOrders(userID)
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.thereIsNoNetwork),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        displayOrderViewModel.error.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.some_error_accurated),
                    Toast.LENGTH_SHORT
                ).show()
                displayOrderViewModel.error.value = false
            }
        })

        displayOrderViewModel.cancelMutableData.observe(viewLifecycleOwner, {
            deleteAlert(displayOrderViewModel, it.id ?: 0)

        })

        //make new call to update view with the new data after cancel order

        displayOrderViewModel.observeDeleteOrder().observe(viewLifecycleOwner, {
            if (it) {
                callOrders(displayOrderViewModel, view)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.order_canceld),
                    Toast.LENGTH_SHORT
                ).show()
                displayOrderViewModel.observeDeleteOrder().value = false
            }

        })


        //payment
        displayOrderViewModel.payNowMutableData.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "amount = " + it.total_price, Toast.LENGTH_SHORT)
                .show()

            startActivity(
                Intent(requireActivity(), Checkout_Activity::class.java).putExtra(
                    "amount",
                    it.total_price
                )
                    .putExtra("order", it as Serializable)
            )
        })

        return view.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(StringsUtils.tabID, tabId)
        outState.putLong(StringsUtils.userID, userID)
    }

    private fun callOrders(
        displayOrderViewModel: DisplayOrderViewModel,
        view: FragmentDisplayOrderBinding
    ) {
        view.progressPar.visibility = View.VISIBLE
        if (NetworkChangeReceiver.isOnline) {
            if (tabId == 0) {
                displayOrderViewModel.getPaidOrders(userID)

            } else {
                displayOrderViewModel.getUnPaidOrders(userID)
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereIsNoNetwork),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun deleteAlert(displayOrderViewModel: DisplayOrderViewModel, order_id: Long) {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cancel_order))
        builder.setMessage(getString(R.string.are_you_sure))

        builder.setPositiveButton("Delete") { _, _ ->
            displayOrderViewModel.deleteOrder(order_id)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
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


    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.GONE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.GONE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = getString(R.string.my_orders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displayOrderViewModel.deleteOrder = MutableLiveData()
        displayOrderViewModel.cancelMutableData = MutableLiveData()
        displayOrderViewModel.payNowMutableData = MutableLiveData()

    }


}