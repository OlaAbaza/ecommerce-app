package com.example.shopy.ui.signIn

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.shopy.databinding.FragmentSignUpBinding
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.domainLayer.Utils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.RepositoryImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.CustomerX

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class SignUpFragment : Fragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var signinViewModel: SignInViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var  meDataSourceReo : MeDataSharedPrefrenceReposatory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        editor = prefs.edit()
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        val application = requireNotNull(this.activity).application
        val repository = RepositoryImpl(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(application)))

        val viewModelFactory = ViewModelFactory(repository,application)
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

        binding.tvSignin.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_signInFragment)
        )

        binding.registerBtn.setOnClickListener {
            if (!(Utils.validateEmail(binding.emailEdt.text.toString())))
                Toast.makeText(
                    context,
                    "Please check your email and try again ",
                    Toast.LENGTH_SHORT
                ).show()
            else if (!(Utils.validatePassword(binding.passwordEdt.text.toString())))
                Toast.makeText(
                    context,
                    "Please check your password and try again ",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                if (NetworkChangeReceiver.isOnline) {
                    signinViewModel.createCustomers(
                        binding.nameEdt.text.toString(),
                        binding.emailEdt.text.toString(),
                        binding.passwordEdt.text.toString()
                    )
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
        binding.passwordEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                Timber.i("olaa passs")
                binding.group.setVisibility(View.VISIBLE)
            }
            else {
                Timber.i("olaa passsx")
                binding.group.setVisibility(View.GONE)
            }
        }

        signinViewModel.getPostResult().observe(viewLifecycleOwner, Observer<CustomerX?> {
            Log.d("Tag","signin")

            Timber.i("isLogged+" + it)
            if (it != null) {

                meDataSourceReo.saveUsertId(it.customer.id.toString())
                meDataSourceReo.saveUsertName(it.customer.firstName.toString())
                meDataSourceReo.saveUsertState(true)
                view.findNavController().popBackStack()
                Log.d("Tag","firstName != nul${it.customer.firstName.toString()}l")
            } else {
                Toast.makeText(context, "This mail is already exits", Toast.LENGTH_SHORT).show()
            }
        })
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
        if (requestCode == SignInFragment.SIGN_IN_RESULT_CODE) {
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
                .setIsSmartLockEnabled(false)
                .build(), SignInFragment.SIGN_IN_RESULT_CODE
        )
    }

    private fun observeAuthenticationState() {
        signinViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    SignInViewModel.AuthenticationState.AUTHENTICATED -> {
                        meDataSourceReo.saveUsertState(true)
                        Timber.i("isLogged+" + FirebaseAuth.getInstance().currentUser?.displayName + FirebaseAuth.getInstance().currentUser?.email)
                        FirebaseAuth.getInstance().currentUser?.displayName?.let {
                            FirebaseAuth.getInstance().currentUser?.email?.let { it1 ->
                                if (NetworkChangeReceiver.isOnline) {
                                    signinViewModel.createCustomers(
                                        it, it1, "123"
                                    )
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
                    else -> {
                        meDataSourceReo.saveUsertState(false)
                    }
                }
            })
    }
}

