package com.example.shopy.ui.customerAddress

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAddressBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.Addresse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
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
        val repository = RepositoryImpl(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(application)))
        val viewModelFactory = ViewModelFactory(repository,application)
        addressViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddressViewModel::class.java)
        addressAdapter = AddressAdapter(arrayListOf(), addressViewModel,requireContext())
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = addressAdapter
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeToolbar()

        if(meDataSourceReo.loadUsertstate())
           customerID =meDataSourceReo.loadUsertId()
//        else
//         findNavController().navigate( NavGraphDirections.actionGlobalSignInFragment())

        if (NetworkChangeReceiver.isOnline) {
            addressViewModel.getCustomersAddressList(customerID)

        }
        else {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereIsNoNetwork),
                Toast.LENGTH_SHORT
            ).show()

        }

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
                if(it.first?.default==true){
                    Toast.makeText(
                        requireContext(), "you Can not delete your dafault address ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                   deleteAlert(it.first?.id.toString(),it.second)

            })
        addressViewModel.getdafultAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            addressViewModel.setDafaultCustomerAddress(customerID, it.id.toString())

        })

        binding.addAddressBtn.setOnClickListener {
            view.findNavController()
                .navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment("null","null"))
        }

    }

    private fun deleteAlert(addressID: String,pos:Int) {

        val builder = AlertDialog.Builder(requireContext())
        //  builder.setTitle(De)
        builder.setMessage(getString(R.string.alert_msg))

        builder.setPositiveButton("Delete") { dialogInterface, which ->
            //  Toast.makeText(requireContext(), "clicked yes", Toast.LENGTH_LONG).show()
            if (NetworkChangeReceiver.isOnline) {
                addressViewModel.delCustomerAddresses(customerID, addressID)
            }
            else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.thereIsNoNetwork),
                    Toast.LENGTH_SHORT
                ).show()

            }
            addressAdapter.delItem(pos)
        }

        builder.setNegativeButton("Cancel") { dialogInterface, which ->
            //  Toast.makeText(requireContext(),"clicked No",Toast.LENGTH_LONG).show()
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
        requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.GONE
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.cartView.visibility = View.INVISIBLE
        requireActivity().toolbar.favourite.visibility = View.INVISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = "My Address"
    }
}
