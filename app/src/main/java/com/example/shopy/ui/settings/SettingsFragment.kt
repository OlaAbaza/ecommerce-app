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

        binding.developedByBtn.setOnClickListener {
            binding.developedByBtn.visibility = View.GONE
            binding.developedByBtnHide.visibility = View.VISIBLE
            binding.devByRecView.visibility = View.VISIBLE


        }
        binding.developedByBtnHide.setOnClickListener {
            binding.developedByBtnHide.visibility = View.GONE
            binding.devByRecView.visibility = View.GONE
            binding.developedByBtn.visibility = View.VISIBLE


        }
        binding.addressBtn.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalAddressFragment())
        }
        binding.profileBtn.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalProfileFragment())
        }
        binding.aboutBtn.setOnClickListener {

            binding.aboutBtn.visibility = View.GONE
            binding.aboutBtnHide.visibility = View.VISIBLE

            binding.aboutTV.visibility = View.VISIBLE

        }
        binding.aboutBtnHide.setOnClickListener {
            binding.aboutBtnHide.visibility = View.GONE
            binding.aboutTV.visibility = View.GONE
            binding.aboutBtn.visibility = View.VISIBLE

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

        binding.faqBtn.setOnClickListener {
            binding.faqBtn.visibility = View.GONE
            binding.faqBtnHide.visibility = View.VISIBLE
            binding.faqRecView.visibility = View.VISIBLE

        }
        binding.faqBtnHide.setOnClickListener {
            binding.faqBtnHide.visibility = View.GONE
            binding.faqRecView.visibility = View.GONE
            binding.faqBtn.visibility = View.VISIBLE
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

    private fun changeToolbar() {
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