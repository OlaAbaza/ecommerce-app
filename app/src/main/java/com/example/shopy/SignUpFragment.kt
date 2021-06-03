package com.example.shopy

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.shopy.RemoteDataSource

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SignUpFragment : Fragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var signinViewModel : SignInViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        editor = prefs.edit()
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        val application = requireNotNull(this.activity).application
        val remoteDataSource = RemoteDataSource()
        val viewModelFactory = SignInViewModelFactory(remoteDataSource, application)
        signinViewModel =
                ViewModelProvider(
                        this, viewModelFactory
                ).get(SignInViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignin.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_signInFragment))

        binding.registerBtn.setOnClickListener{
            signinViewModel.createCustomers(binding.nameEdt.text.toString(),binding.emailEdt.text.toString(),binding.passwordEdt.text.toString())
        }

        signinViewModel.getPostResult().observe(viewLifecycleOwner, Observer<String> {
            Timber.i("isLogged+"+ it)
         if (it=="true") {
             editor.putBoolean("isLogged", true)
             editor.commit()
             view.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
         }
         else { Toast.makeText(context,  it , Toast.LENGTH_SHORT).show()} })
        binding.googleButton.setOnClickListener {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            launchSignInFlow(providers) }
        binding.facebookButton.setOnClickListener{
            val providers = arrayListOf(  AuthUI.IdpConfig.FacebookBuilder().build())
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

    private fun launchSignInFlow( provider: List<AuthUI.IdpConfig>) {
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
                        editor.putBoolean("isLogged", true)
                        editor.commit()
                        Timber.i("isLogged+"+FirebaseAuth.getInstance().currentUser?.displayName+FirebaseAuth.getInstance().currentUser?.email)
                        FirebaseAuth.getInstance().currentUser?.displayName?.let {
                            FirebaseAuth.getInstance().currentUser?.email?.let { it1 ->
                                signinViewModel.createCustomers(
                                    it, it1,"123"
                                )
                            }
                        }
                    }
                    else -> {
                        editor.putBoolean("isLogged", false)
                        editor.commit()
                    }
                }
            })
    }
}

