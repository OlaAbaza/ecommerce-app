package com.example.shopy.ui.customerAddress

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.NavGraphDirections
import com.example.shopy.util.Utils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAddAddressBinding
import com.example.shopy.databinding.FragmentAddressBinding
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.*
import com.example.shopy.ui.shoppingBag.OrderConfirmationFragmentArgs
import com.example.shopy.ui.signIn.SignInFragmentDirections
import kotlinx.android.synthetic.main.fragment_add_address.view.*
import timber.log.Timber

class AddressFragment : Fragment() {
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var binding: FragmentAddressBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private var customerID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddressBinding.inflate(layoutInflater)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        val application = requireNotNull(this.activity).application
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(application)))
        val viewModelFactory = ViewModelFactory(repository,application)
        addressViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddressViewModel::class.java)
        addressAdapter = AddressAdapter(arrayListOf(), addressViewModel)
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addressAdapter
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerID =meDataSourceReo.loadUsertId()
        addressViewModel.getCustomersAddressList(customerID)


        addressViewModel.getAddressList().observe(viewLifecycleOwner, Observer<List<Addresse>?> {
            Timber.i("olaaaaaaa"+it)
            addressAdapter.addNewList(it)
        })
        addressViewModel.getAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            view.findNavController()
                .navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment(it.customerId.toString(),it.id.toString()))

        })
        addressViewModel.getDelAddress()
            .observe(viewLifecycleOwner, Observer<Pair<Addresse?, Int>> {
                addressViewModel.delCustomerAddresses(customerID, it.first?.id.toString())
                addressAdapter.delItem(it.second)
                Timber.i("ola delll" + it.toString())

            })
        addressViewModel.getdafultAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            addressViewModel.setDafaultCustomerAddress(customerID, it.id.toString())

        })

        binding.addAddressBtn.setOnClickListener {
            view.findNavController()
                .navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment("null","null"))
        }

    }
}
