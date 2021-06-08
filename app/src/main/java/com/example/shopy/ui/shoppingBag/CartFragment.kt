package com.example.shopy.ui.shoppingBag

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentCartBinding
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.*
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class CartFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var cartAdapter: CartAdapter
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory

    private var customerID = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        binding = FragmentCartBinding.inflate(layoutInflater)
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSourceImpl()
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, remoteDataSource, application)
        orderViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(OrderViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartAdapter = CartAdapter(arrayListOf(), orderViewModel)
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
        // cartAdapter.addNewList(it)

        requireActivity().toolbar_title.text = "Card Bag"

        customerID = meDataSourceReo.loadUsertId()
//        var customerOrder = CustomerOrder(customerID.toLong())
//        var lineItem: MutableList<LineItem> = arrayListOf()
//        lineItem.add(LineItem(1, 39853306642630))
//        var order = Order(customerOrder, "pending", lineItem)
//        var orders = Orders(order)
//        binding.checkoutButton.setOnClickListener {
//            orderViewModel.createOrder(orders)
//        }
        orderViewModel.getPostOrder().observe(viewLifecycleOwner, Observer<OrderResponse?>
        {

            if (it != null) {
                Timber.i("order+" + it)
            } else {
                Toast.makeText(context, "eror null", Toast.LENGTH_SHORT).show()
            }
        })

        orderViewModel.getAllCartList().observe(viewLifecycleOwner, {
            cartAdapter.addNewList(it)
        })

    }


}