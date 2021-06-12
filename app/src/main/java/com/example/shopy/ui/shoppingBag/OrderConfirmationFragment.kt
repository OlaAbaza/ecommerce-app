package com.example.shopy.ui.shoppingBag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopy.NavGraphDirections
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentOrderConfirmationBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_order_confirmation.*
import timber.log.Timber


class OrderConfirmationFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private lateinit var binding: FragmentOrderConfirmationBinding
    private lateinit var orderItemsAdapter: OrderItemsAdapter

    private var totalPrice = 0.0
    private var customerID = ""
    private var paymentMethod = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        binding = FragmentOrderConfirmationBinding.inflate(layoutInflater)
        val args: OrderConfirmationFragmentArgs by navArgs()
        totalPrice = args.totalPrice.toDouble()
        val application = requireNotNull(this.activity).application
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        orderViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(OrderViewModel::class.java)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().toolbar_title.text = "Order Confirmation"
        if (isLoged()) {
            customerID = meDataSourceReo.loadUsertId()
            Timber.i("olaaa" + customerID)
            orderViewModel.getCustomersAddressList(customerID)
        }
        orderItemsAdapter = OrderItemsAdapter(arrayListOf(), orderViewModel)
        binding.rvCartItems.apply {
            adapter = orderItemsAdapter
        }
        orderViewModel.getAllCartList().observe(viewLifecycleOwner, {
            orderItemsAdapter.addNewList(it)

        })

        orderViewModel.getAddressList().observe(viewLifecycleOwner, Observer<List<Addresse>?> {
            val dafultAddress: MutableList<Addresse> = arrayListOf()
            for(item in it ){
                if(item.default==true) {
                    dafultAddress.add(item)
                    Timber.i("olaada" + item.default)
                }
                Timber.i("olaahh" + item.default)
            }
            Timber.i("olaamm" + dafultAddress)

            if (dafultAddress.isEmpty()){
                binding.group.visibility=View.INVISIBLE
                binding.addAddressText.visibility=View.VISIBLE
                Toast.makeText(context, "you do not have an account", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.group.visibility=View.VISIBLE
                binding.addAddressText.visibility=View.INVISIBLE
                binding.fullNameTxt.text = dafultAddress.get(0).firstName.toString()
                binding.countryTxt.text = dafultAddress.get(0).country.toString()
                binding.addressTxt.text = dafultAddress.get(0).address1.toString()
            }

        })
        binding.cvAddress.setOnClickListener {
            val action = NavGraphDirections.actionGlobalAddressFragment()
            findNavController().navigate(action)
//            val action = NavGraphDirections.actionGlobalProfileFragment()
//            findNavController().navigate(action)
        }
        binding.cvVoucher.setOnClickListener {
            discount_edt.visibility = View.VISIBLE
        }
        binding.placeOrderBtn.setOnClickListener {
            var customerOrder = CustomerOrder(customerID.toLong())
            var lineItem: MutableList<LineItem> = arrayListOf()

            val items = orderItemsAdapter.orderList.map {
               it.variants?.get(0)
            }
            Timber.i("itemss"+items)
            for(item in items){
                lineItem.add(LineItem(item?.inventory_quantity, item?.id))
            }
            getPaymentMethod()
            var order = Order(customerOrder, "pending", lineItem,paymentMethod)
            var orders = Orders(order)
            orderViewModel.createOrder(orders)
        }
        orderViewModel.getPostOrder().observe(viewLifecycleOwner, Observer<OrderResponse?>
        {

            if (it != null) {
                Timber.i("order+" + it)
                orderViewModel.delAllItems()
                val action = NavGraphDirections.actionGlobalShopTabFragment2()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "eror null", Toast.LENGTH_SHORT).show()
            }
        })
        binding.totalPriceTxt.text = totalPrice.toString() + "EGP"
        binding.totalItemTxt.text = totalPrice.toString() + "EGP"

    }

    private fun getPaymentMethod() {
        if(binding.radioCash.isChecked)
            paymentMethod="Cash"
        else if(binding.radioCredit.isChecked)
            paymentMethod="Card"
    }


    private fun isLoged(): Boolean {
        return meDataSourceReo.loadUsertstate()
    }

}