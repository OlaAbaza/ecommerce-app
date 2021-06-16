package com.example.shopy.ui.showOneOrderDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentShowOneOrderBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.domainLayer.FilterData
import com.example.shopy.ui.displayOrderFragment.DisplayOrderFragmentArgs
import com.example.shopy.ui.displayOrderFragment.DisplayOrderViewModel
import java.lang.ref.WeakReference

class ShowOneOrderFragment : Fragment() {

    private lateinit var fragmentShowOneOrderBinding:FragmentShowOneOrderBinding
    private lateinit var showOrderViewModel:ShowOneOrderDetailsViewModel
    private lateinit var images : List<String>


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowOneOrderBinding = FragmentShowOneOrderBinding.inflate(
            inflater,
            container,
            false
        )
        val args: ShowOneOrderFragmentArgs by navArgs()

        val viewModelFactory = ViewModelFactory(
            Repository(RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))),
            requireActivity().application)

        showOrderViewModel = WeakReference(ViewModelProvider(requireActivity(), viewModelFactory)[ShowOneOrderDetailsViewModel::class.java]).get()!!



        val ordersListAdapter = WeakReference(OrdersListAdapter(arrayListOf(), arrayListOf())).get()

        fragmentShowOneOrderBinding.imageItemsRecycler.apply {
            this.layoutManager =  LinearLayoutManager(requireContext())
            this.adapter = ordersListAdapter
        }




        showOrderViewModel.getOneOrders(args.productId).observe(viewLifecycleOwner,{


            ordersListAdapter!!.line_items= it.order?.line_items!!


            if (it.order.financial_status == "paid"){
                fragmentShowOneOrderBinding.tvPay.text = "Paid Order"
                fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_paid)
            }else{
                fragmentShowOneOrderBinding.tvPay.text = resources.getString(R.string.waiting_for_payment)
                fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_circle_shape)

            }
            fragmentShowOneOrderBinding.totalPriceEditable.text = it.order.total_price
            fragmentShowOneOrderBinding.orderIdEditable.text = "# ${it.order.id}"
            fragmentShowOneOrderBinding.createdAtEditable.text = it.order.created_at
            if(it.order.note == "Cash"){
                fragmentShowOneOrderBinding.paymentTypeEditable.text = it.order.note
            }else{
                fragmentShowOneOrderBinding.paymentTypeEditable.text = "Credit Card"
            }


            val ids = FilterData.getProductsIDs(it.order)

            showOrderViewModel.getProductAllProuducts().observe(viewLifecycleOwner,{
                images= FilterData.getListOfImageForOneItem(ids,it.products)
                ordersListAdapter.images= images

            })


        })

       return fragmentShowOneOrderBinding.root
    }


}