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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.util.Utils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentAddAddressBinding
import com.example.shopy.databinding.FragmentAddressBinding
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.models.*
import kotlinx.android.synthetic.main.fragment_add_address.view.*
import timber.log.Timber

class AddressFragment : Fragment() {
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var binding: FragmentAddressBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var dialog: Dialog
    private lateinit var bindingDialog: FragmentAddAddressBinding
    private lateinit var prefs: SharedPreferences
    private var customerID: String = ""
    private var isedit: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddressBinding.inflate(layoutInflater)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSourceImpl()
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(application)))
        val viewModelFactory = ViewModelFactory(repository,remoteDataSource,application)
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
        customerID = prefs.getString("customerID", "").toString()
        addressViewModel.getCustomersAddressList(customerID)


        addressViewModel.getAddressList().observe(viewLifecycleOwner, Observer<List<Addresse>?> {
            addressAdapter.addNewList(it)
        })
        addressViewModel.getAddress().observe(viewLifecycleOwner, Observer<Pair<Addresse?, Int>> {
            isedit = true
            showAddAddressDialog(it.first)

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
            showAddAddressDialog(null)
        }

    }

    private fun showAddAddressDialog(item: Addresse?) {
        dialog = Dialog(this.requireContext())
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        // change dialog size
        lp.copyFrom(dialog.getWindow()?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        bindingDialog = FragmentAddAddressBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        if (isedit) {
            item?.let {
                bindingDialog.apply {
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

        bindingDialog.saveBtn.setOnClickListener {
            if(validateFields()){
            if (Utils.validatePhone(bindingDialog.phoneEdt.text.toString())){
            Timber.i("ola customerID" + customerID)
            if (isedit) {
                item?.let {
                    var address = Addresse(
                        bindingDialog.addressEdt.text.toString(),
                        bindingDialog.address2Edt.text.toString(),
                        bindingDialog.cityEdt.text.toString(),
                        "null",
                        bindingDialog.countryEdt.text.toString(),
                        "null",
                        "null",
                        customerID.toLong(),
                        bindingDialog.addressSwitch.isChecked,
                        bindingDialog.nameEdt.text.toString(),
                        item.id,
                        "null",
                        "null",
                        bindingDialog.phoneEdt.text.toString(),
                        bindingDialog.stateEdt.text.toString(),
                        "null",
                        bindingDialog.postCodeEdt.text.toString()
                    )
                    var createAddress = UpdateAddresse(address)

                    addressViewModel.updateCustomerAddresses(
                        customerID,
                        item.id.toString(),
                        createAddress
                    )
                }
            } else {
                var address = Address(
                    bindingDialog.addressEdt.text.toString(),
                    bindingDialog.address2Edt.text.toString(),
                    bindingDialog.cityEdt.text.toString(),
                    "null",
                    bindingDialog.countryEdt.text.toString(),
                    "null",
                    "null",
                    bindingDialog.nameEdt.text.toString(),
                    "null",
                    "null",
                    bindingDialog.phoneEdt.text.toString(),
                    bindingDialog.stateEdt.text.toString(),
                    "null",
                    bindingDialog.postCodeEdt.text.toString(),
                    bindingDialog.addressSwitch.isChecked
                )
                var createAddress = CreateAddress(address)
                addressViewModel.createCustomersAddress(customerID, createAddress)
            }
            dialog.dismiss()
            isedit = false
        }
            else{
                Toast.makeText(
                    context,
                    "incorrect phone number",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
            else{
                Toast.makeText(
                    context,
                    "please fill all required fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        bindingDialog.toolbar1.btn_back.setOnClickListener {
            Timber.i("backkk")
            dialog.dismiss()
            isedit = false
        }


        dialog.show()
        dialog.getWindow()?.setAttributes(lp)
    }
    private fun validateFields():Boolean{
        var isEmpty=false
        bindingDialog.apply {
            if (addressEdt.text.toString().trim().isEmpty()
                &&cityEdt.text.toString().trim().isEmpty()
                &&countryEdt.text.toString().trim().isEmpty()
                &&nameEdt.text.toString().trim().isEmpty()
                &&phoneEdt.text.toString().trim().isEmpty()
                &&stateEdt.text.toString().trim().isEmpty()
                &&postCodeEdt.text.toString().trim().isEmpty()
                &&addressEdt.text.toString().trim().isEmpty()
            )
            {
                isEmpty=true
            }
        }
return  isEmpty
    }
}
