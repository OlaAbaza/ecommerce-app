package com.example.shopy.ui.displayOrderFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentDisplayOrderBinding
import com.example.shopy.datalayer.localdatabase.room.RoomService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler

class DisplayOrderFragment : Fragment() {



    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentDisplayOrderBinding.inflate(inflater, container, false)

        val remoteDataSource = RemoteDataSourceImpl()
        val viewModelFactory = ViewModelFactory(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
            ), remoteDataSource, requireActivity().application
        )
        val displayOrderViewModel: DisplayOrderViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[DisplayOrderViewModel::class.java]

        displayOrderViewModel.getAllOrders()
        displayOrderViewModel.orders.observe(viewLifecycleOwner,{
            it.count()
        })



        return view.root
    }



}