package com.example.shopy.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.myapplication.FAQAdapter
import com.example.shopy.R
import com.example.shopy.databinding.FragmentFAQBinding
import com.example.shopy.datalayer.entity.settings.FAQModel
import kotlinx.android.synthetic.main.activity_main.*

class FAQFragment : Fragment() {
  lateinit var binding:FragmentFAQBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFAQBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeToolbar()
        var faqList = listOf(
            FAQModel("Is this app available on in Egypt?", "Yes, it's exclusive in Egypt."),
            FAQModel("How much are the delivery fees?", "It's based on your location."),
            FAQModel("What are the payment methods?", "You can pay either online or on delivery."),
            FAQModel(
                "What is the return policy?",
                "You have 14 days to return the product but it needs to a brand-new, or 30 days if there is any defect in it."
            )
        )
        binding.faqRecView.adapter=FAQAdapter(faqList)
    }
    fun changeToolbar() {
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = "FAQ"
    }
}