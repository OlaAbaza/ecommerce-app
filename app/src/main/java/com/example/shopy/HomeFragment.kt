package com.example.shopy

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.shopy.databinding.FragmentHomeBinding
import com.example.shopy.databinding.FragmentSignInBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        editor = prefs.edit()

        binding.loginBtn.setOnClickListener {

            AuthUI.getInstance().signOut(requireContext())
            editor.putBoolean("isLogged", false)
            editor.commit()
            view?.findNavController()?.navigate(HomeFragmentDirections.actionHomeFragmentToSignUpFragment())
        }
        binding.addressBtn.setOnClickListener {


            view?.findNavController()?.navigate(HomeFragmentDirections.actionHomeFragmentToAddressFragment())
        }

        return binding.root
    }



}