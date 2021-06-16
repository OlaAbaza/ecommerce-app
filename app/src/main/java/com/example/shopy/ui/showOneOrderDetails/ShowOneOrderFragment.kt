package com.example.shopy.ui.showOneOrderDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.shopy.R
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentShowOneOrderBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.ui.displayOrderFragment.DisplayOrderFragmentArgs
import com.example.shopy.ui.displayOrderFragment.DisplayOrderViewModel
import java.lang.ref.WeakReference

class ShowOneOrderFragment : Fragment() {

    private lateinit var fragmentShowOneOrderBinding:FragmentShowOneOrderBinding
    private lateinit var showOrderViewModel:ShowOneOrderDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowOneOrderBinding = FragmentShowOneOrderBinding.inflate(
            inflater,
            container,
            false
        )
        val args: ShowOneOrderFragmentArgs by navArgs()

        val viewModelFactory = ViewModelFactory(
            Repository(RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))),
            requireActivity().application)

        showOrderViewModel = WeakReference(ViewModelProvider(requireActivity(), viewModelFactory)[ShowOneOrderDetailsViewModel::class.java]).get()!!


        showOrderViewModel.getOneOrders(args.productId).observe(viewLifecycleOwner,{
            Toast.makeText(requireActivity(),"$it",Toast.LENGTH_SHORT).show()
            it.line_items
        })

       return fragmentShowOneOrderBinding.root
    }

}