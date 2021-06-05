package com.example.shopy.ui.shopTab.shopTabCategories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.ui.shopTab.ShopItemsAdapter
import com.example.shopy.ui.shopTab.ShopTabViewModel
import kotlinx.android.synthetic.main.fragment_woman_products.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnSaleProductFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_sale_product, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val shopTabViewModel= ViewModelProvider(this).get(ShopTabViewModel::class.java)
        shopTabViewModel.fetchOnSaleProductsList().observe(viewLifecycleOwner,{
            Log.i("output",it.toString()+"******************")
            if (it != null){
                bindWomanProductRecyclerView(it.products)
            }
            Log.i("output",it.products.get(0).toString())

        })

        shopTabViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, {
            val allCodes = it
            if (allCodes != null) {
                ads.setOnClickListener {
                    Glide.with(this)
                        .load(R.drawable.gif_discount)
                        .into(ads)
                    GlobalScope.launch(Dispatchers.Main) { delay(1500)
                        lin.visibility = View.VISIBLE
                        codeTextView.text = allCodes.discountCodes[0].code}
                }

            }
        })
    }


    private fun bindWomanProductRecyclerView( itemName:List<Product>) {

        itemsRecView.adapter= ShopItemsAdapter(requireContext(),itemName)

    }

}