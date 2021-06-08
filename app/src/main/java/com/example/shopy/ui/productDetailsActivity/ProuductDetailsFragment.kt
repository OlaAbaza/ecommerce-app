package com.example.shopy.ui.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.adapters.ImageSilderAdapter
import com.example.shopy.adapters.OptionsAdapter
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.dataLayer.Repository
import com.example.shopy.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentProuductDetailsBinding
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.base.StringsUtils
import kotlinx.android.synthetic.main.activity_main.*


class ProuductDetailsFragment : Fragment() {
    private lateinit var bindingProductDetailsFragment: FragmentProuductDetailsBinding
    private lateinit var productDetailsViewMode: ProductDetailsViewModel
    private lateinit var imageSliderAdaper: ImageSilderAdapter
    var id: Long? = null
    private var stored = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingProductDetailsFragment =
            FragmentProuductDetailsBinding.inflate(inflater, container, false)

        val remoteDataSource = RemoteDataSourceImpl()

        val viewModelFactory = ViewModelFactory(Repository(RemoteDataSourceImpl(), RoomDataSourceImpl(RoomService.getInstance(requireActivity().application)))
            ,remoteDataSource,requireActivity().application)
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
                //todo from Intent
                 val args: ProuductDetailsFragmentArgs by navArgs()
                id = args.productID
                checkWishListStored(id ?: 0)
            }




            productDetailsViewMode.getProductByIdFromNetwork(id = id ?: 0)

            imageSliderAdaper = ImageSilderAdapter(ArrayList())
            bindingProductDetailsFragment.viewPagerMain.adapter = imageSliderAdaper



            productDetailsViewMode.observeProductDetails().observe(activity, {
                product = it.product
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
                stored = if (stored) {
                    productDetailsViewMode.deleteOneWishItem(id = id ?: 0)
                    false
                } else {
                    productDetailsViewMode.saveWishList(product)
                    true
                }
                setStoredButton(stored)
            }


            bindingProductDetailsFragment.addToCart.setOnClickListener {
                productDetailsViewMode.saveCartList(
                    ProductCartModule(
                        product.id,
                        product.title,
                        order_state = StringsUtils.Unpaid,
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
                        product.variants,
                        product.options,
                        product.images,
                        product.image
                    )
                )
                Toast.makeText(
                    activity,
                    getString(R.string.item_saved_in_cart),
                    Toast.LENGTH_SHORT
                ).show()
            }

            productDetailsViewMode.signInBoolesn.observe(activity, {
                createAlertToSignIn(activity)
            })

          //  Toast.makeText(requireContext(), "Show message", Toast.LENGTH_SHORT).show();
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

        activity.toolbar_title.text = product.title ?: "null"
//        toolbar.title =product.title ?: "null"
        imageSliderAdaper.images = product.images ?: ArrayList()
        imageSliderAdaper.notifyDataSetChanged()



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
            this.adapter = product.options?.get(1)?.values?.let { OptionsAdapter(it) }
        }


        bindingProductDetailsFragment.sizeEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = product.options?.get(0)?.values?.let { OptionsAdapter(it) }
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
            //todo for intent
//            val intent = Intent(context, SignIn::class.java)
//            startActivity(intent)
        }
        alertDialogBuilder.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        productDetailsViewMode.progressPar.value = false

    }


}