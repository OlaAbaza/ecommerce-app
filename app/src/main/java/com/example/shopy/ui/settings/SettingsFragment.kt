package com.example.shopy.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.FAQAdapter
import com.example.myapplication.SettingsAdapter
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentSettingsBinding
import com.example.shopy.datalayer.entity.settings.DeveloperModel
import com.example.shopy.datalayer.entity.settings.FAQModel
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var settingsViewModel: SettingsViewModel
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
        changeToolbar()
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())

        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
        )
        val viewModelFactory = ViewModelFactory(repository, requireActivity().application)
        settingsViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[SettingsViewModel::class.java]



        binding.cvDevelopedBy.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToDevelopedByFragment())
        }
        binding.cvAddress.setOnClickListener {
            if(meDataSourceReo.loadUsertstate())
            findNavController().navigate(NavGraphDirections.actionGlobalAddressFragment())
            else
            findNavController().navigate( NavGraphDirections.actionGlobalSignInFragment())
        }
        binding.cvProfile.setOnClickListener {
            if(meDataSourceReo.loadUsertstate())
                findNavController().navigate(NavGraphDirections.actionGlobalProfileFragment())
            else
                findNavController().navigate( NavGraphDirections.actionGlobalSignInFragment())
        }

        binding.cvAbout.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutFragment())
        }

        binding.cvFAQ.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToFAQFragment())

        }
        binding.signout.setOnClickListener {

            meDataSourceReo.saveUsertState(false)
            meDataSourceReo.saveUsertName("")
            meDataSourceReo.saveUsertId("")
            if (NetworkChangeReceiver.isOnline) {
                settingsViewModel.clearRoom()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.thereIsNoNetwork),
                    Toast.LENGTH_SHORT
                ).show()

            }

            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMeFragment())

        }
    }

     fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.cartView.visibility = View.INVISIBLE
        requireActivity().toolbar.favourite.visibility = View.INVISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = "Settings"
    }

}