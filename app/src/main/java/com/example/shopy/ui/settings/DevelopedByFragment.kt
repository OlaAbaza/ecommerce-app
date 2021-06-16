package com.example.shopy.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.myapplication.SettingsAdapter
import com.example.shopy.R
import com.example.shopy.databinding.FragmentDevelopedByBinding
import com.example.shopy.datalayer.entity.settings.DeveloperModel
import kotlinx.android.synthetic.main.activity_main.*

class DevelopedByFragment : Fragment() {
   lateinit var binding:FragmentDevelopedByBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDevelopedByBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeToolbar()
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
    }
    fun changeToolbar() {
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = "Developed By"
    }
}