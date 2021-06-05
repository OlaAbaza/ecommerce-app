package com.example.shopy

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.customerAddress.AddressAdapter
import com.example.shopy.dataLayer.RemoteDataSource
import com.example.shopy.databinding.FragmentCartBinding
import com.example.shopy.databinding.FragmentSignInBinding
import com.example.shopy.databinding.FragmentSignUpBinding
import com.example.shopy.models.*
import com.example.shopy.signIn.SignInViewModel
import com.example.shopy.signIn.SignUpFragmentDirections
import com.google.gson.annotations.SerializedName
import timber.log.Timber


class CartFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var cartAdapter: CartAdapter
    private var customerID=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        binding = FragmentCartBinding.inflate(layoutInflater)
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSource()
        val viewModelFactory = ViewModelFactory(remoteDataSource,application)
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
        customerID = prefs.getString("customerID", "").toString()
        var customerOrder=CustomerOrder(customerID.toLong())
        var lineItem:MutableList<LineItem> = arrayListOf()
        lineItem.add(LineItem(1,39853306642630))
        var order= Order(customerOrder,"pending",lineItem)
        var orders= Orders(order)
        binding.checkoutButton.setOnClickListener {
            orderViewModel.createOrder(orders)
        }
        orderViewModel.getPostOrder().observe(viewLifecycleOwner, Observer<OrderResponse?>
        {

            if (it != null) {
                Timber.i("order+" + it)
            } else {
                Toast.makeText(context, "eror null", Toast.LENGTH_SHORT).show()
            }
        })
    }

}