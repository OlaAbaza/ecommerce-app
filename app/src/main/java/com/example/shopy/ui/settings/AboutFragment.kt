package com.example.shopy.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.findNavController
import com.example.shopy.R
import com.example.shopy.databinding.FragmentAboutBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class AboutFragment : Fragment() {
   lateinit var binding:FragmentAboutBinding
   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      binding= FragmentAboutBinding.inflate(inflater,container,false)
      changeToolbar()
      return binding.root
   }
   fun changeToolbar() {
      requireActivity().findViewById<SearchView>(R.id.mainSearchView).visibility=View.GONE
      requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
      requireActivity().toolbar.setNavigationOnClickListener {
         view?.findNavController()?.popBackStack()
      }

      requireActivity().toolbar_title.text = "About Us"
   }

}