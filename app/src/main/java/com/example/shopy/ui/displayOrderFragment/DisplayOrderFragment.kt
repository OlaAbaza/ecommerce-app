package com.example.shopy.ui.displayOrderFragment

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.adapters.OrderDisplayAdapter
import com.example.shopy.base.StringsUtils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentDisplayOrderBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference


class DisplayOrderFragment : Fragment() {

    private var tabId: Int = 0
    private var userID: Long = 0
    private lateinit var displayOrderViewModel: DisplayOrderViewModel
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


        val remoteDataSource = RemoteDataSourceImpl()
        val viewModelFactory = ViewModelFactory(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
            ), remoteDataSource, requireActivity().application
        )


         displayOrderViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[DisplayOrderViewModel::class.java]


        val meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        userID = meDataSourceReo.loadUsertId().toLong()

//        tabId =//
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
        callOrders(displayOrderViewModel,view)


        val adapter = WeakReference(
            OrderDisplayAdapter(
                ArrayList(),
                displayOrderViewModel.payNowMutableData,
                displayOrderViewModel.cancelMutableData
            )
        ).get()
        view.orderRecycler.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }


        requireActivity().toolbar_title.text = getString(R.string.my_orders)




        displayOrderViewModel.orders.observe(viewLifecycleOwner, {
            adapter!!.list = it
            view.progressPar.visibility = View.GONE
            if (it.isEmpty()){
                view.emptyAnimationView.visibility=View.VISIBLE
            }else{
                view.emptyAnimationView.visibility=View.GONE
            }
        })



        view.tapLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        displayOrderViewModel.getPaidOrders(userID)
                        view.progressPar.visibility = View.VISIBLE
                    }
                    1 -> {
                        displayOrderViewModel.getUnPaidOrders(userID)
                        view.progressPar.visibility = View.VISIBLE
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        displayOrderViewModel.error.observe(viewLifecycleOwner,{
            if (it){
                Toast.makeText(requireContext(),getString(R.string.some_error_accurated),Toast.LENGTH_SHORT).show()
                displayOrderViewModel.error.value=false
            }
        })

        displayOrderViewModel.cancelMutableData.observe(viewLifecycleOwner,{
            deleteAlert(displayOrderViewModel,it.id?:0)
        })

        displayOrderViewModel.deleteOrder.observe(viewLifecycleOwner,{
            Toast.makeText(requireContext(),getString(R.string.order_canceld),Toast.LENGTH_SHORT).show()

            //make new call to update view with the new data after cancel order
            callOrders(displayOrderViewModel,view)
        })



        return view.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(StringsUtils.tabID, tabId)
        outState.putLong(StringsUtils.userID, userID)
    }

    private fun callOrders(displayOrderViewModel: DisplayOrderViewModel, view: FragmentDisplayOrderBinding){

        if (tabId == 0) {
            displayOrderViewModel.getPaidOrders(userID)
            view.progressPar.visibility = View.VISIBLE

        } else {
            displayOrderViewModel.getUnPaidOrders(userID)
            view.progressPar.visibility = View.VISIBLE
        }
    }


    private fun deleteAlert(displayOrderViewModel: DisplayOrderViewModel, order_id : Long){

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cancel_order))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){_, _ ->
            displayOrderViewModel.deleteOrder(order_id)
        }

        builder.setNegativeButton("No"){_, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displayOrderViewModel.deleteOrder = MutableLiveData()
    }


}