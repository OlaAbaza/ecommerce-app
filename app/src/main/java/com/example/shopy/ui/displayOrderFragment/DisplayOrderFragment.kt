package com.example.shopy.ui.displayOrderFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


        val displayOrderViewModel: DisplayOrderViewModel = ViewModelProvider(
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
        if (tabId == 0) {
            displayOrderViewModel.getPaidOrders(userID)
            view.progressPar.visibility = View.VISIBLE

        } else {
            displayOrderViewModel.getUnPaidOrders(userID)
            view.progressPar.visibility = View.VISIBLE

        }

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
                Toast.makeText(requireContext(),"Some error accurate",Toast.LENGTH_SHORT).show()
                displayOrderViewModel.error.value=false
            }
        })



        return view.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(StringsUtils.tabID, tabId)
        outState.putLong(StringsUtils.userID, userID)
    }


}