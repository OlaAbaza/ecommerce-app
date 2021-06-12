package com.example.shopy.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
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
        val repository = Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
        val viewModelFactory = ViewModelFactory(repository,requireActivity().application)
        settingsViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[SettingsViewModel::class.java]
        binding.devByRecView.adapter = SettingsAdapter(
            listOf(
                DeveloperModel(
                    "AbdElRahman Nabil",
                    "https://github.com/AbdelrahmanNabil77",
                    "https://www.linkedin.com/in/abdelrahman-nabil-060124111/"
                ),
                DeveloperModel(
                    "Esraa Fathy",
                    "https://github.com/EsraaFathy/EsraaFathy",
                    "https://www.linkedin.com/in/esraafathy1998/"
                ),
                DeveloperModel(
                    "Ola Abaza",
                    "https://github.com/OlaAbaza",
                    "https://www.linkedin.com/in/ola-abaza/"
                ),
                DeveloperModel(
                    "Fayza ElShorbagy",
                    "https://github.com/fayza55elshorbagy",
                    "https://www.linkedin.com/in/fayza-elshorbagy/"
                )
            )
        )

        binding.developedByLabel.setOnClickListener {

            if (binding.devByRecView.visibility == View.GONE) {
                binding.devByRecView.visibility = View.VISIBLE
            } else {
                binding.devByRecView.visibility = View.GONE
            }

        }
        binding.editAddress.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalAddressFragment())
        }
        binding.editProfile.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalProfileFragment())
        }
        binding.aboutLabel.setOnClickListener {

            if (binding.aboutTV.visibility == View.GONE) {

                binding.aboutTV.visibility = View.VISIBLE
            } else {
                binding.aboutTV.visibility = View.GONE
            }
        }

        var faqList = listOf(
            FAQModel("Is this app available on in Egypt?", "Yes, it's exclusive in Egypt."),
            FAQModel("How much are the delivery fees?", "It's based on your location."),
            FAQModel("What are the payment methods?", "You can pay either online or on delivery."),
            FAQModel(
                "What is the return policy?",
                "You have 14 days to return the product but it needs to a brand-new, or 30 days if there is any defect in it."
            )
        )
        binding.faqRecView.adapter = FAQAdapter(faqList)
        binding.FAQlabel.setOnClickListener {
            if (binding.faqRecView.visibility == View.GONE) {

                binding.faqRecView.visibility = View.VISIBLE
            } else {
                binding.faqRecView.visibility = View.GONE
            }
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