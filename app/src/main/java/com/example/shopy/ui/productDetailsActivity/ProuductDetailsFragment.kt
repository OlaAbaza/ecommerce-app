package com.example.shopy.ui.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.StringsUtils
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentProuductDetailsBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.cart_toolbar_view.view.*
import kotlinx.android.synthetic.main.list_toolbar_view.view.*
import java.lang.ref.WeakReference


class ProuductDetailsFragment : Fragment() {
    private lateinit var bindingProductDetailsFragment: FragmentProuductDetailsBinding
    private lateinit var productDetailsViewMode: ProductDetailsViewModel
    private lateinit var imageSliderAdaper: ImageSilderAdapter
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private var optionsSelected: String? = null
    var num = 1

    var id: Long? = null
    private var stored = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingProductDetailsFragment =
            FragmentProuductDetailsBinding.inflate(inflater, container, false)

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
        navBar.visibility = View.GONE

        bindingProductDetailsFragment.decreaseButton.setOnClickListener {
             num =((bindingProductDetailsFragment.itemCountText.text.toString().toInt())-1)
            if(num>0){
                bindingProductDetailsFragment.itemCountText.text=num.toString()
            }
            else{
                num = 1
            }
        }
        bindingProductDetailsFragment.increaseButton.setOnClickListener {
             num =((bindingProductDetailsFragment.itemCountText.text.toString().toInt())+1)
            bindingProductDetailsFragment.itemCountText.text=num.toString()
        }


        changeToolbar()
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())

        val repository = WeakReference(
            Repository(
                RemoteDataSourceImpl(),
                RoomDataSourceImpl(RoomService.getInstance(requireActivity().application))
            )
        ).get()

        val viewModelFactory = ViewModelFactory(repository!!, requireActivity().application)
        productDetailsViewMode = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[ProductDetailsViewModel::class.java]


        lateinit var product: Product
        val activity = activity

        if (activity != null && isAdded) {

            if (savedInstanceState != null) {
                id = savedInstanceState.getLong(StringsUtils.OrderID)
                stored = savedInstanceState.getBoolean(StringsUtils.STORED)
                setStoredButton(stored)

            } else {
                val args: ProuductDetailsFragmentArgs by navArgs()
                id = args.productID
                checkWishListStored(id ?: 0)
            }




            if (NetworkChangeReceiver.isOnline)
                productDetailsViewMode.getProductByIdFromNetwork(id = id ?: 0)
            else
                Toast.makeText(requireContext(), "There is no network", Toast.LENGTH_SHORT).show()



            imageSliderAdaper = ImageSilderAdapter(ArrayList())
            bindingProductDetailsFragment.viewPagerMain.adapter = imageSliderAdaper


            var values : List<String> = emptyList()
            productDetailsViewMode.observeProductDetails().observe(activity, {
                product = it.product
                values  = product.options?.get(0)?.values!!
                updateUI(product, activity)
                productDetailsViewMode.progressPar.value = true


            })

            productDetailsViewMode.progressPar.observe(activity, {
                if (productDetailsViewMode.progressPar.value == true)
                    bindingProductDetailsFragment.progressPar.visibility = View.GONE
                else
                    bindingProductDetailsFragment.progressPar.visibility = View.VISIBLE

            })



            bindingProductDetailsFragment.addToWishList.setOnClickListener {
                if (isLoged()) {
                    stored = if (stored) {
                        productDetailsViewMode.deleteOneWishItem(id = id ?: 0)
                        false
                    } else {
                        productDetailsViewMode.saveWishList(product)
                        true
                    }
                    setStoredButton(stored)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.logInToCanAddToWishList),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }


            bindingProductDetailsFragment.addToCart.setOnClickListener {
                if (isLoged()) {

                    if (optionsSelected == null) {
                        Toast.makeText(
                            requireContext(),
                            "Please chose the size frist",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val variants = product.variants
                        if (variants != null) {
                            variants[0].inventory_quantity = num
                        }

                        val options = product.options
                        options?.get(0)?.values = listOf(optionsSelected!!)
                        productDetailsViewMode.saveCartList(
                            ProductCartModule(
                                product.id,
                                product.title,
                                StringsUtils.Unpaid,
                                product.body_html,
                                product.vendor,
                                product.product_type,
                                product.created_at,
                                product.handle,
                                product.updated_at,
                                product.published_at,
                                product.template_suffix,
                                product.status,
                                product.published_scope,
                                product.tags,
                                product.admin_graphql_api_id,
                                variants,
                                options,
                                product.images,
                                product.image
                            )
                        )
                        productDetailsViewMode.optionsMutableLiveData.value = -1

                        Toast.makeText(activity, getString(R.string.item_saved_in_cart), Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.logInToCanAddToCart),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
            productDetailsViewMode.optionsMutableLiveData.observe(activity,{
                if (it !=-1){
                    optionsSelected = values[it]
                }
            })

            productDetailsViewMode.signInBoolesn.observe(activity, {
                createAlertToSignIn(activity)
            })

        }
        return bindingProductDetailsFragment.root
    }

    private fun checkWishListStored(id: Long) {
        productDetailsViewMode.getOneWithItemFromRoom(id).observe(viewLifecycleOwner, {
            if (it != null) {
                stored = true
                setStoredButton(stored)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(product: Product, activity: Activity) {
        //activity.title = product.title ?: "null"

//        toolbar.title =product.title ?: "null"
        imageSliderAdaper.images = product.images ?: ArrayList()
        imageSliderAdaper.notifyDataSetChanged()



        bindingProductDetailsFragment.prise.text =
            "${product.variants?.get(0)?.price.toString()} EGP"
        bindingProductDetailsFragment.productTitle.text = product.title ?: "null"
        bindingProductDetailsFragment.prise.text = "${product.variants?.get(0)?.price.toString()} EGP"

        bindingProductDetailsFragment.descriptionEditable.text = product.body_html
        bindingProductDetailsFragment.stateEditable.text = product.status ?: "not known"

        if (product.status.equals("active")) {
            bindingProductDetailsFragment.stateEditable.setTextColor(
                AppCompatResources.getColorStateList(
                    activity,
                    R.color.green
                )
            )
        }

        bindingProductDetailsFragment.vendorEditable.text = product.vendor

        bindingProductDetailsFragment.colorEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = product.options?.get(1)?.values?.let {
                OptionsAdapter(
                    it,
                    productDetailsViewMode.optionsMutableLiveData
                )
            }
        }


        bindingProductDetailsFragment.sizeEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = product.options?.get(0)?.values?.let {
                OptionsAdapter(it, productDetailsViewMode.optionsMutableLiveData)
            }
        }

        bindingProductDetailsFragment.descriptionBtn.setOnClickListener {
            if (bindingProductDetailsFragment.descriptionEditable.visibility == View.GONE) {
                bindingProductDetailsFragment.descriptionEditable.visibility = View.VISIBLE
                bindingProductDetailsFragment.descriptionBtn.setBackgroundResource(R.drawable.ic_arrow_down_24)
            }else{
                bindingProductDetailsFragment.descriptionEditable.visibility = View.GONE
                bindingProductDetailsFragment.descriptionBtn.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_ios_24)
            }
        }

        bindingProductDetailsFragment.stateBtn.setOnClickListener {
            if (bindingProductDetailsFragment.stateEditable.visibility == View.GONE) {
                bindingProductDetailsFragment.stateEditable.visibility = View.VISIBLE
                bindingProductDetailsFragment.stateBtn.setBackgroundResource(R.drawable.ic_arrow_down_24)
            }else{
                bindingProductDetailsFragment.stateEditable.visibility = View.GONE
                bindingProductDetailsFragment.stateBtn.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_ios_24)

            }
        }
        bindingProductDetailsFragment.vendorBtn.setOnClickListener {
            if (bindingProductDetailsFragment.vendorEditable.visibility == View.GONE) {
                bindingProductDetailsFragment.vendorEditable.visibility = View.VISIBLE
                bindingProductDetailsFragment.vendorBtn.setBackgroundResource(R.drawable.ic_arrow_down_24)
            }else{
                bindingProductDetailsFragment.vendorEditable.visibility = View.GONE
                bindingProductDetailsFragment.vendorBtn.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_ios_24)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        id?.let { outState.putLong(StringsUtils.OrderID, it) }
        outState.putBoolean(StringsUtils.STORED, stored)
    }

    private fun setStoredButton(stored: Boolean) {
        if (stored) {
            bindingProductDetailsFragment.addToWishList.setImageResource(R.drawable.ic_favorite_24)
        } else {
            bindingProductDetailsFragment.addToWishList.setImageResource(R.drawable.ic__favorite_border_24)
        }
    }


    private fun createAlertToSignIn(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(getString(R.string.you_are_not_signed))
        alertDialogBuilder.setMessage(getString(R.string.sign_in_and_try_again))
        alertDialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ ->
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.signin)) { _, _ ->
//            val intent = Intent(context, SignIn::class.java)
//            startActivity(intent)
        }
        alertDialogBuilder.show()
    }


    private fun changeToolbar() {
        requireActivity().findViewById<View>(R.id.bottom_nav).visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.searchIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.settingIcon).visibility = View.INVISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteButton.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.favourite).favouriteItems.visibility = View.INVISIBLE

        requireActivity().findViewById<View>(R.id.cartView).cartButton.visibility = View.VISIBLE
        requireActivity().findViewById<View>(R.id.cartView).cartItems.visibility = View.INVISIBLE

        requireActivity().toolbar_title.setTextColor(Color.BLACK)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.black_arrow))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        requireActivity().toolbar_title.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productDetailsViewMode.progressPar.value = false
        productDetailsViewMode.optionsMutableLiveData.value = -1


    }

    private fun isLoged() = meDataSourceReo.loadUsertstate()


}