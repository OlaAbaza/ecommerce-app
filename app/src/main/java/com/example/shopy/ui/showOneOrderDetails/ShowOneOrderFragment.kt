package com.example.shopy.ui.showOneOrderDetails

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.orderGet.OneOrderResponce
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentShowOneOrderBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.domainLayer.FilterData
import com.example.shopy.ui.payment.Checkout_Activity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_show_one_order.*
import kotlinx.android.synthetic.main.fragment_woman_products.*
import java.io.Serializable
import java.lang.ref.WeakReference

class ShowOneOrderFragment : Fragment() {

    private lateinit var fragmentShowOneOrderBinding:FragmentShowOneOrderBinding
    private lateinit var showOrderViewModel:ShowOneOrderDetailsViewModel
    private lateinit var images : List<String>
    lateinit var order : GetOrders.Order


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
            RepositoryImpl(RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))),
            requireActivity().application)

        showOrderViewModel = WeakReference(ViewModelProvider(requireActivity(), viewModelFactory)[ShowOneOrderDetailsViewModel::class.java]).get()!!



        val ordersListAdapter = WeakReference(OrdersListAdapter(arrayListOf(), arrayListOf())).get()

        fragmentShowOneOrderBinding.imageItemsRecycler.apply {
            this.layoutManager =  LinearLayoutManager(requireContext())
            this.adapter = ordersListAdapter
        }

        fragmentShowOneOrderBinding.cancelButton.setOnClickListener {
            deleteAlert(showOrderViewModel,order.id!!)
        }

        var total_price : String = ""
        fragmentShowOneOrderBinding.payButton.setOnClickListener {
            startActivity(
                Intent(requireActivity(), Checkout_Activity::class.java).putExtra(
                    "amount",
                    total_price
                )
                    .putExtra("order", order as Serializable)
            )
        }

        showOrderViewModel.observeDeleteOrder().observe(viewLifecycleOwner,{
            if (it){
                view?.findNavController()?.popBackStack()
                showOrderViewModel.observeDeleteOrder().value = false
            }
        })



        changeToolbar()

        showOrderViewModel.getOneOrders(args.productId).observe(viewLifecycleOwner,{

            total_price= it.order?.total_price.toString()
            order = it.order!!

            ordersListAdapter!!.line_items= it.order?.line_items!!



            fragmentShowOneOrderBinding.totalPriceEditable.text = "EGP"+it.order.total_price
            fragmentShowOneOrderBinding.orderIdEditable.text = "# ${it.order.id}"
            var orderDate = it.order.created_at?.split("T")
            var orderTime = orderDate?.get(0)
            Log.i("output",orderTime!!)
            var time = orderDate?.get(1)?.split("+")
            Log.i("output",time?.get(0)!!)
            fragmentShowOneOrderBinding.createdAtEditable.text = orderDate?.get(0)
            fragmentShowOneOrderBinding.time.text = time?.get(0)
            copyOrderId.setOnClickListener {
                val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
                clipboard?.setPrimaryClip(ClipData.newPlainText("",orderIdEditable.text))
                Toast.makeText(requireContext(),"Copied", Toast.LENGTH_SHORT).show()

            }
            if(it.order.note == "Cash"){
                fragmentShowOneOrderBinding.paymentTypeEditable.text = it.order.note.toString()
                fragmentShowOneOrderBinding.payButton.visibility = View.GONE
                if (it.order.financial_status == "paid"){
                    fragmentShowOneOrderBinding.tvPay.text = "Paid Order"
                    fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_paid)
                    fragmentShowOneOrderBinding.cancelButton.visibility = View.GONE
                }else{
                    fragmentShowOneOrderBinding.tvPay.text = resources.getString(R.string.waiting_for_payment)
                    fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_circle_shape)
                    fragmentShowOneOrderBinding.cancelButton.visibility = View.VISIBLE
                }
            }else{
                fragmentShowOneOrderBinding.paymentTypeEditable.text = "Credit Card"
                if (it.order.financial_status == "paid"){
                    fragmentShowOneOrderBinding.tvPay.text = "Paid Order"
                    fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_paid)
                    fragmentShowOneOrderBinding.cancelButton.visibility = View.GONE
                    fragmentShowOneOrderBinding.payButton.visibility = View.GONE
                }else{
                    fragmentShowOneOrderBinding.tvPay.text = resources.getString(R.string.waiting_for_payment)
                    fragmentShowOneOrderBinding.line1.background = resources.getDrawable(R.drawable.state_circle_shape)
                    fragmentShowOneOrderBinding.cancelButton.visibility = View.VISIBLE
                    fragmentShowOneOrderBinding.payButton.visibility = View.VISIBLE
                }
            }


            val ids = FilterData.getProductsIDs(it.order)

            showOrderViewModel.getProductAllProuducts().observe(viewLifecycleOwner,{
                images= FilterData.getListOfImageForOneItem(ids,it.products)
                ordersListAdapter.images= images

            })


        })

       return fragmentShowOneOrderBinding.root
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

    private fun deleteAlert(displayOrderViewModel: ShowOneOrderDetailsViewModel, order_id: Long) {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.cancel_order))
        builder.setMessage(getString(R.string.are_you_sure))

        builder.setPositiveButton("Yes") { _, _ ->
            displayOrderViewModel.deleteOrder(order_id)
        }

        builder.setNegativeButton("No") { _, _ ->
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