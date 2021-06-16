package com.example.shopy.ui.shopTab.shopTabCategories

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.ui.shopTab.ShopItemsAdapter
import com.example.shopy.ui.shopTab.ShopTabViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_kids_product.*
import kotlinx.android.synthetic.main.fragment_on_sale_product.*
import kotlinx.android.synthetic.main.fragment_woman_products.*
import kotlinx.android.synthetic.main.fragment_woman_products.ads
import kotlinx.android.synthetic.main.fragment_woman_products.codeTextView
import kotlinx.android.synthetic.main.fragment_woman_products.itemsRecView
import kotlinx.android.synthetic.main.fragment_woman_products.lin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OnSaleProductFragment : Fragment() {


    lateinit var  shopTabViewModel : ShopTabViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        shopTabViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ShopTabViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_sale_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (NetworkChangeReceiver.isOnline) {
            networkSaleView.visibility = View.GONE
            Sale_lin.visibility = View.VISIBLE
            shopTabViewModel.fetchOnSaleProductsList().observe(viewLifecycleOwner, {
                Log.i("output", it.toString() + "******************")
                if (it != null) {
                    bindWomanProductRecyclerView(
                        it.products,
                        shopTabViewModel.intentTOProductDetails
                    )
                }
                Log.i("output", it.products.get(0).toString())

            })

            shopTabViewModel.intentTOProductDetails.observe(requireActivity(), {
                shopTabViewModel.intentTOProductDetails = MutableLiveData()
                val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id.toLong())
                findNavController().navigate(action)
            })

            shopTabViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, {
                val allCodes = it
                if (allCodes != null) {
                    startbtn.setOnClickListener {
                        startbtn.visibility = View.GONE
                        Glide.with(this)
                            .load(R.drawable.sale_gif)
                            .into(ads)
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(1500)
                            lin.visibility = View.VISIBLE
                            codeTextView.text = allCodes.discountCodes[0].code
                        }
                    }

                }
            })
        }else{
            networkSaleView.visibility = View.VISIBLE
            Sale_lin.visibility = View.GONE
        }
        codeTextView.setOnClickListener {
            val clipboard = ContextCompat.getSystemService(requireContext(),ClipboardManager::class.java)
            clipboard?.setPrimaryClip(ClipData.newPlainText("",codeTextView.text))
            Toast.makeText(requireContext(),"Copied",Toast.LENGTH_SHORT).show()

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        shopTabViewModel.intentTOProductDetails = MutableLiveData()
    }

    private fun bindWomanProductRecyclerView(
        itemName: List<Product>,
        intentTOProductDetails: MutableLiveData<Product>
    ) {

        itemsRecView.adapter= ShopItemsAdapter(requireContext(), itemName, intentTOProductDetails)

    }

}