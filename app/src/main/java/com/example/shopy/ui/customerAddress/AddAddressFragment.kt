package com.example.shopy.ui.customerAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopy.NavGraphDirections
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAddAddressBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.Address
import com.example.shopy.models.Addresse
import com.example.shopy.models.CreateAddress
import com.example.shopy.models.UpdateAddresse
import com.example.shopy.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class AddAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private var customerID: String = ""
    private var addressID: String = ""
    private var isedit: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddAddressBinding.inflate(layoutInflater)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        val args: AddAddressFragmentArgs by navArgs()
        addressID = args.addressID
        customerID = args.customerID

        val application = requireNotNull(this.activity).application
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        addressViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AddressViewModel::class.java)

        if (customerID != "null" && addressID != "null") {
            isedit = true
            addressViewModel.getCustomerAddress(customerID, addressID)
        }
        addressViewModel.getEditAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            displayAddress(it)
        })
        addressViewModel.createCustomerAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            Timber.i("olaaaa address"+it)
            if(it!=null) {
                val action =
                    AddAddressFragmentDirections.actionAddAddressFragmentToAddressFragment()
                findNavController().navigate(action)
            }
            else{
                Toast.makeText(context, "The country or The state is  not valid ", Toast.LENGTH_SHORT).show()
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerID = meDataSourceReo.loadUsertId()

        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().bottom_nav.visibility = View.VISIBLE
        requireActivity().toolbar_title.text = "My Addresses"

        binding.saveBtn.setOnClickListener {
            if (!validateFields()) {
                if (Utils.validatePhone(binding.phoneEdt.text.toString())) {
                    Timber.i("ola customerID" + customerID)
                    if (isedit) {
                        var address = Addresse(
                            binding.addressEdt.text.toString().trim(),
                            binding.address2Edt.text.toString(),
                            binding.cityEdt.text.toString().trim(),
                            "null",
                            binding.countryEdt.text.toString().trim(),
                            "null",
                            "null",
                            customerID.toLong(),
                            binding.addressSwitch.isChecked,
                            binding.nameEdt.text.toString().trim(),
                            addressID.toLong(),
                            "null",
                            "null",
                            binding.phoneEdt.text.toString().trim(),
                            binding.stateEdt.text.toString().trim(),
                            "null",
                            binding.postCodeEdt.text.toString()
                        )
                        var createAddress = UpdateAddresse(address)

                        addressViewModel.updateCustomerAddresses(
                            customerID,
                            addressID,
                            createAddress
                        )

                    } else {
                        var address = Address(
                            binding.addressEdt.text.toString().trim(),
                            binding.address2Edt.text.toString(),
                            binding.cityEdt.text.toString().trim(),
                            "null",
                            binding.countryEdt.text.toString().trim(),
                            "null",
                            "null",
                            binding.nameEdt.text.toString().trim(),
                            "null",
                            "null",
                            binding.phoneEdt.text.toString().trim(),
                            binding.stateEdt.text.toString().trim(),
                            "null",
                            binding.postCodeEdt.text.toString(),
                            binding.addressSwitch.isChecked
                        )
                        var createAddress = CreateAddress(address)
                        addressViewModel.createCustomersAddress(customerID, createAddress)
                    }
                }
                else {
                    Toast.makeText(
                        context,
                        "incorrect phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "please fill all required fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun displayAddress(item: Addresse?) {
        item?.let {
            binding.apply {
                nameEdt.setText(item.firstName)
                cityEdt.setText(item.city)
                addressEdt.setText(item.address1)
                phoneEdt.setText(item.phone)
                countryEdt.setText(item.country)
                address2Edt.setText(item.address2.toString())
                postCodeEdt.setText(item.zip)
                stateEdt.setText(item.province)
                addressSwitch.isChecked = item.default == true

            }
        }
    }

    private fun validateFields(): Boolean {
        var isEmpty = false
        binding.apply {
            if (addressEdt.text.toString().trim().isEmpty()
                && cityEdt.text.toString().trim().isEmpty()
                && countryEdt.text.toString().trim().isEmpty()
                && nameEdt.text.toString().trim().isEmpty()
                && phoneEdt.text.toString().trim().isEmpty()
                && stateEdt.text.toString().trim().isEmpty()
                && postCodeEdt.text.toString().trim().isEmpty()
            ) {
                isEmpty = true
            }
        }
        return isEmpty
    }
}
