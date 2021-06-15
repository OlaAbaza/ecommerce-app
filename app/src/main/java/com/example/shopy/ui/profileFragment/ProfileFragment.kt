package com.example.shopy.ui.profileFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentProfileBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.Customer
import com.example.shopy.models.CustomerProfile
import com.example.shopy.models.CustomerX
import com.example.shopy.models.CustomerXXX
import com.example.shopy.domainLayer.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import timber.log.Timber

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private var customerID: String = ""
    private var customerPassword: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        val application = requireNotNull(this.activity).application
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(application)))
        val viewModelFactory = ViewModelFactory(repository,application)
        profileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ProfileViewModel::class.java)
        if(meDataSourceReo.loadUsertstate())
            customerID =meDataSourceReo.loadUsertId()
        else {
            findNavController().navigate(NavGraphDirections.actionGlobalSignInFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      changeToolbar()
        if (NetworkChangeReceiver.isOnline) {
            profileViewModel.getCustomer(customerID)
        }
        else {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereIsNoNetwork),
                Toast.LENGTH_SHORT
            ).show()

        }
        profileViewModel.getCustomerInfo().observe(viewLifecycleOwner, Observer<Customer?>{
            if (it!=null){
                binding.nameEdt.setText(it.firstName)
                binding.phoneEdt.setText(it.phone)
                customerPassword = it.note.toString()
            }
        })
        profileViewModel.getPostResult().observe(viewLifecycleOwner, Observer<CustomerX?>{
            if (it!=null){
                Timber.i("olaa update profile "+it)
                view.findNavController().popBackStack()
            }
            else(
                    Timber.i("olaa update profile null")
            )

        })
        binding.passBtnShow.setOnClickListener {
            binding.passGroup.visibility=View.VISIBLE
            binding.passBtnShow.visibility=View.GONE
            binding.passBtnHide.visibility=View.VISIBLE

        }
        binding.passBtnHide.setOnClickListener {
            binding.passGroup.visibility=View.GONE
            binding.passBtnHide.visibility=View.GONE
            binding.passBtnShow.visibility=View.VISIBLE

        }
        binding.saveBtn.setOnClickListener {
            var name=""
            var phone=""
            var flag=false
            if(validateName()&&validatePhone()){
                name=binding.nameEdt.text.toString()
                phone=binding.phoneEdt.text.toString()
                flag=true
                if(!(validatePassFields())){
                    if(validateCurrentPassField()&&validateNewPassField()){
                        customerPassword=binding.passwordEdt.text.toString()
                        flag=true
                    }
                    else{
                        flag=false
                    }

                }

            }
            if(flag) {
                var cust = CustomerXXX(name, customerID.toLong(), customerPassword, phone)
                var customerProfile = CustomerProfile(cust)
                if (NetworkChangeReceiver.isOnline) {
                    profileViewModel.UpdateCustomers(customerID, customerProfile)
                }
                else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.thereIsNoNetwork),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }
    private fun validatePassFields(): Boolean {
        var isEmpty = false
        binding.apply {
            if (passwordEdt.text.toString().trim().isEmpty()
                && confirmPasswordEdt.text.toString().trim().isEmpty()
                && currentPasswordEdt.text.toString().trim().isEmpty()
            ) {
                isEmpty = true
            }
        }
        return isEmpty
    }
    private fun validateCurrentPassField(): Boolean {
        var isCorrect = true
        binding.apply {
            if (currentPasswordEdt.text.toString()!=customerPassword) {
                binding.currentPasswordEdt.setError("Your old Password is wrong")
               // Toast.makeText(context, "Your old Password is wrong", Toast.LENGTH_SHORT).show()
                isCorrect = false
            }
        }
        return isCorrect
    }
    private fun validateNewPassField(): Boolean {
        var isCorrect = true
        binding.apply {
             if(!(Utils.validatePassword(binding.passwordEdt.text.toString()))) {
                Toast.makeText(
                    context,
                    "Your Password must contain at least one letter , one number and 8 characters minimum",
                    Toast.LENGTH_SHORT
                ).show()
                 binding.passwordEdt.setError("")
                isCorrect = false
            }
             else if (passwordEdt.text.toString()!=confirmPasswordEdt.text.toString()) {
                 binding.confirmPasswordEdt.setError("Wrong password")
                //Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show()
                isCorrect = false
            }
        }
        return isCorrect
    }
    private fun validateName(): Boolean {
        var isEmpty = true
        binding.apply {
            if (nameEdt.text.toString().trim().isEmpty()) {
                isEmpty = false
                nameEdt.setError("This faild is required")
             //   Toast.makeText(context, "Please Enter Your name", Toast.LENGTH_SHORT).show()
            }
        }
        return isEmpty
    }
    private fun validatePhone(): Boolean {
        var isEmpty = true
        binding.apply {
            if (!(phoneEdt.text.toString().trim().isEmpty())) {
                if (!(Utils.validatePhone(binding.phoneEdt.text.toString()))){
                    Timber.i("olaa phone")
                    binding.phoneEdt.setError("Please enter valid formate")
                   // Toast.makeText(context, "incorrect phone number", Toast.LENGTH_SHORT).show()
                    isEmpty=false
                }
                Timber.i("olaa phone44-")
            }
        }
        return isEmpty
    }
    private fun changeToolbar() {
        requireActivity().bottom_nav.visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.cartView.visibility = View.INVISIBLE
        requireActivity().toolbar.favourite.visibility = View.INVISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        requireActivity().toolbar_title.text = "Edit Profile"
    }
}