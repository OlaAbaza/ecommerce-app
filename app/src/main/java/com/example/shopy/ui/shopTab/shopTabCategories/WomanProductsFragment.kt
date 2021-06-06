package com.example.shopy.ui.shopTab.shopTabCategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.datalayer.entity.custom_product.Product
import com.example.shopy.ui.shopTab.ShopItemsAdapter
import com.example.shopy.ui.shopTab.ShopTabViewModel
import kotlinx.android.synthetic.main.fragment_woman_products.*
import kotlinx.coroutines.*


class WomanProductsFragment : Fragment() {

    lateinit var  shopTabViewModel : ShopTabViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_woman_products, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shopTabViewModel = ViewModelProvider(this).get(ShopTabViewModel::class.java)
        shopTabViewModel.fetchWomanProductsList().observe(viewLifecycleOwner, {
            if (it != null) {
                bindWomanProductRecyclerView(it.products,shopTabViewModel.intentTOProductDetails)
            }
        })

        shopTabViewModel.intentTOProductDetails.observe(requireActivity(),{
            shopTabViewModel.intentTOProductDetails= MutableLiveData()
            val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id.toLong())
            findNavController().navigate(action)
        })


        shopTabViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, {
            val allCodes = it
            if (allCodes != null) {
                ads.setOnClickListener {
                    Glide.with(this@WomanProductsFragment)
                        .load(R.drawable.woman_three)
                        .into(ads)
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(1500)
                        lin.visibility = View.VISIBLE
                        codeTextView.text = allCodes.discountCodes[3].code
                    }
                }

            }
        })

    }


    private fun bindWomanProductRecyclerView(
        itemName: List<Product>,
        intentTOProductDetails:MutableLiveData<Product>
    ) {

        itemsRecView.adapter = ShopItemsAdapter(requireContext(), itemName,intentTOProductDetails)

    }
}