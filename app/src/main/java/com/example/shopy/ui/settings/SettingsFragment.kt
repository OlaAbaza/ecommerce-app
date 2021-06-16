package com.example.shopy.ui.settings

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.FAQAdapter
import com.example.myapplication.SettingsAdapter
import com.example.shopy.NavGraphDirections
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentSettingsBinding
import com.example.shopy.datalayer.entity.settings.DeveloperModel
import com.example.shopy.datalayer.entity.settings.FAQModel
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var settingsViewModel:SettingsViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        if (meDataSourceReo.loadUsertstate()){
            binding.signout.visibility=View.VISIBLE
        }
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
        val viewModelFactory = ViewModelFactory(repository,requireActivity().application)
        settingsViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[SettingsViewModel::class.java]


        binding.developedByLabel.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDevelopedByFragment())
        }
        binding.editAddress.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalAddressFragment())
        }
        binding.editProfile.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalProfileFragment())
        }
        binding.aboutLabel.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutFragment())
        }

        binding.FAQlabel.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToFAQFragment())
        }
    binding.signout.setOnClickListener {

        meDataSourceReo.saveUsertState(false)
        meDataSourceReo.saveUsertName("")
        meDataSourceReo.saveUsertId("")

        settingsViewModel.clearRoom()

        findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMeFragment())

    }
    }

}