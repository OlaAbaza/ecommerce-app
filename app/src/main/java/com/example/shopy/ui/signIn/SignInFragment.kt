package com.example.shopy.ui.signIn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentSignInBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.Customer
import com.facebook.CallbackManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class SignInFragment : Fragment() {
    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var signinViewModel: SignInViewModel
    private lateinit var binding: FragmentSignInBinding
    private var email = ""
    private var isLoginWithfirebase = false
    lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application

        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        signinViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(SignInViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar.visibility = View.GONE
        requireActivity().bottom_nav.visibility = View.GONE
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())

        signinViewModel.getCustomerList().observe(viewLifecycleOwner, Observer<List<Customer>?> {
            Timber.i("we lodged")
            Timber.i("olaa sigin in email"+email)
            val customer: List<Customer> =
                it.filter {
                    it.email?.toLowerCase() ?: 0 == email
                        .toLowerCase()
                }

            if (customer.isEmpty()) {
                Toast.makeText(context, "you do not have an account", Toast.LENGTH_SHORT).show()
            } else {
                if (customer.get(0).note == binding.passwordEdt.text.toString()||isLoginWithfirebase) {

                    meDataSourceReo.saveUsertState(true)
                    meDataSourceReo.saveUsertId(customer[0].id.toString())
                    meDataSourceReo.saveUsertName(customer[0].firstName.toString())
                    Timber.i("we lodged")
                 ///   view.findNavController().currentBackStackEntry
                    view.findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.loginBtn.setOnClickListener {
            email = binding.emailEdt.text.toString().trim()
            signinViewModel.getAllCustomers()
        }

        binding.tvSignup.setOnClickListener {
            view.findNavController()
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.googleButton.setOnClickListener {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            launchSignInFlow(providers)
        }

        binding.facebookButton.setOnClickListener {
            val providers = arrayListOf(AuthUI.IdpConfig.FacebookBuilder().build())
            launchSignInFlow(providers)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Timber.i("Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Timber.i("Sign in unsuccessful ${response?.error?.errorCode}")
            }
            observeAuthenticationState()
        }
    }

    private fun launchSignInFlow(provider: List<AuthUI.IdpConfig>) {
        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
               // .setIsSmartLockEnabled(false)
                .build(), SIGN_IN_RESULT_CODE
        )
    }

    private fun observeAuthenticationState() {
        signinViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    SignInViewModel.AuthenticationState.AUTHENTICATED -> {
                        email = FirebaseAuth.getInstance().currentUser?.email.toString()
                        signinViewModel.getAllCustomers()
                        isLoginWithfirebase=true
                    }
                    else -> {
                        meDataSourceReo.saveUsertState(false)
                    }
                }
            })
    }

}