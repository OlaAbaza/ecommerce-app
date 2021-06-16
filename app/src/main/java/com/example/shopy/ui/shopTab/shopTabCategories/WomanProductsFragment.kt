package com.example.shopy.ui.shopTab.shopTabCategories

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.example.shopy.ui.shoppingBag.OrderViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.fragment_woman_products.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*
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

       // changeToolbar()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_woman_products, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        if (NetworkChangeReceiver.isOnline) {
            networkView.visibility = View.GONE
            woman_linear.visibility = View.VISIBLE
            shopTabViewModel.fetchWomanProductsList().observe(viewLifecycleOwner, {
                if (it != null) {
                    bindWomanProductRecyclerView(
                        it.products,
                        shopTabViewModel.intentTOProductDetails
                    )
                }
            })

            //product details fragment
            shopTabViewModel.intentTOProductDetails.observe(requireActivity(), {
                shopTabViewModel.intentTOProductDetails = MutableLiveData()
                val action = NavGraphDirections.actionGlobalProuductDetailsFragment(it.id.toLong())
                findNavController().navigate(action)
            })


            shopTabViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, {
                val allCodes = it
                if (allCodes != null) {
                    play.setOnClickListener {
                        play.visibility = View.GONE
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
        }else{
            networkView.visibility = View.VISIBLE
            woman_linear.visibility = View.GONE
        }

        codeTextView.setOnClickListener {
            val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
            clipboard?.setPrimaryClip(ClipData.newPlainText("",codeTextView.text))
            Toast.makeText(requireContext(),"Copied", Toast.LENGTH_SHORT).show()

        }

    }

    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.VISIBLE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.cartView).cartButton.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().settingIcon.setColorFilter(getResources().getColor(R.color.black))
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).visibility = View.VISIBLE
        requireActivity().toolbar_title.setTextColor(Color.BLACK)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(null)
    }
    private fun bindWomanProductRecyclerView(
        itemName: List<Product>,
        intentTOProductDetails:MutableLiveData<Product>
    ) {

        itemsRecView.adapter = ShopItemsAdapter(requireContext(), itemName,intentTOProductDetails)

    }
}