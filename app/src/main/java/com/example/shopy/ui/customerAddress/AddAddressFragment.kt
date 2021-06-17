package com.example.shopy.ui.customerAddress

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAddAddressBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.domainLayer.Utils
import com.example.shopy.models.Address
import com.example.shopy.models.Addresse
import com.example.shopy.models.CreateAddress
import com.example.shopy.models.UpdateAddresse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
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
        val repository = RepositoryImpl(
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
            if (NetworkChangeReceiver.isOnline) {
                addressViewModel.getCustomerAddress(customerID, addressID)
            }
            else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.thereIsNoNetwork),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        addressViewModel.getEditAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            displayAddress(it)
        })
        addressViewModel.createCustomerAddress().observe(viewLifecycleOwner, Observer<Addresse?> {
            Timber.i("olaaaa address" + it)
            if (it != null) {
//                val action =
//                    AddAddressFragmentDirections.actionAddAddressFragmentToAddressFragment()
//                findNavController().navigate(action)
                view?.findNavController()?.popBackStack()
            } else {
                Toast.makeText(
                    context,
                    "The country or The state is  not valid ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerID = meDataSourceReo.loadUsertId()

        changeToolbar()

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
                        if (NetworkChangeReceiver.isOnline) {
                            addressViewModel.updateCustomerAddresses(
                                customerID,
                                addressID,
                                createAddress
                            )
                        }
                        else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.thereIsNoNetwork),
                                Toast.LENGTH_SHORT
                            ).show()

                        }


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
                        if (NetworkChangeReceiver.isOnline) {
                            addressViewModel.createCustomersAddress(customerID, createAddress)
                        }
                        else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.thereIsNoNetwork),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                } else {
                    binding.phoneEdt.setError("Please enter valid formate")
                }
            }

        }
    }

    private fun changeToolbar() {
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

        requireActivity().toolbar_title.text = "Add Address"
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
              if (item.default==true)
                  addressLayout.visibility=View.INVISIBLE
                  else
                  addressSwitch.isChecked = item.default == true
            }
        }
    }

    private fun validateFields(): Boolean {
        var isEmpty = false
        binding.apply {
            if (nameEdt.text.toString().trim().isEmpty()) {
                nameEdt.setError("This faild is required")
                isEmpty = true
            } else if (phoneEdt.text.toString().trim().isEmpty()) {
                phoneEdt.setError("This faild is required")
                isEmpty = true
            } else if (cityEdt.text.toString().trim().isEmpty()) {
                cityEdt.setError("This faild is required")
                isEmpty = true
            } else if (stateEdt.text.toString().trim().isEmpty()) {
                stateEdt.setError("This faild is required")
                isEmpty = true
            } else if (postCodeEdt.text.toString().trim().isEmpty()) {
                postCodeEdt.setError("This faild is required")
                isEmpty = true
            } else if (addressEdt.text.toString().trim().isEmpty()) {
                addressEdt.setError("This faild is required")
                isEmpty = true
            } else if (countryEdt.text.toString().trim().isEmpty()) {
                countryEdt.setError("This faild is required")
                isEmpty = true
            }
        }
        return isEmpty
    }
}
