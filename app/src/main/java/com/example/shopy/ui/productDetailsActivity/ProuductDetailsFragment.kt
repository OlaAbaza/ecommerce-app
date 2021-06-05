package com.example.shopy.ui.productDetailsActivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.adapters.ImageSilderAdapter
import com.example.shopy.adapters.OptionsAdapter
import com.example.shopy.databinding.FragmentProuductDetailsBinding
import com.example.shopy.datalayer.entity.itemPojo.Images
import com.example.shopy.datalayer.entity.itemPojo.Product
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.ui.StringsUtils


class ProuductDetailsFragment : Fragment() {
    lateinit var bindingProductDetailsFragment: FragmentProuductDetailsBinding
    lateinit var images: List<Images>
    lateinit var productDetailsViewMode: ProductDetailsViewModel
    lateinit var imageSliderAdaper: ImageSilderAdapter
    lateinit var product: Product
    var id: Long? = null
    var stored = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingProductDetailsFragment =
            FragmentProuductDetailsBinding.inflate(inflater, container, false)

        images = ArrayList()
        productDetailsViewMode = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProductDetailsViewModel::class.java]


        val activity = getActivity();

        if (activity != null && isAdded) {

            if (savedInstanceState != null) {
                id = savedInstanceState.getLong(StringsUtils.OrderID)
                stored = savedInstanceState.getBoolean(StringsUtils.STORED)
                setStoredButton(stored)

            } else {
                //todo from Intent
                 val args: ProuductDetailsFragmentArgs by navArgs()
                val bundle = arguments
//                id = bundle!!.getLong(StringsUtils.OrderID, 0)
//            id = requireActivity().intent?.getLongExtra(StringsUtils.OrderID,0)
                id = args.productID
                checkWishListStored(id ?: 0)
                Log.d("TAG", "id = $id")
            }




            productDetailsViewMode.getProductByIdFromNetwork(id = id ?: 0)

            imageSliderAdaper = ImageSilderAdapter(images)
            bindingProductDetailsFragment.viewPagerMain.adapter = imageSliderAdaper



            productDetailsViewMode.observeProductDetails().observe(activity, {
                product = it.product
                updateUI(product,activity)
                productDetailsViewMode.progressPar.value = true


            })

            productDetailsViewMode.progressPar.observe(activity, {
                if (productDetailsViewMode.progressPar.value == true)
                    bindingProductDetailsFragment.progressPar.visibility = View.GONE
            })



            bindingProductDetailsFragment.addToWishList.setOnClickListener {
                if (stored) {
                    productDetailsViewMode.deleteOneWishItem(id = id ?: 0)
                    stored = false
                } else {
                    productDetailsViewMode.saveWishList(product)
                    stored = true
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

    private fun updateUI(product: Product,activity : Activity) {
        activity.title = product.title ?: "null"

        imageSliderAdaper.images = product.images ?: ArrayList()
        imageSliderAdaper.notifyDataSetChanged()


        bindingProductDetailsFragment.prise.text = product.variants?.get(0)?.price.toString()

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
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(1)?.values?.let { OptionsAdapter(it) }
        }


        bindingProductDetailsFragment.sizeEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(0)?.values?.let { OptionsAdapter(it) }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        id?.let { outState.putLong(StringsUtils.OrderID, it) }
        outState.putBoolean(StringsUtils.STORED, stored)
    }

    fun setStoredButton(stored: Boolean) {
        if (stored) {
            bindingProductDetailsFragment.addToWishList.setImageResource(R.drawable.ic_favorite_24)
        } else {
            bindingProductDetailsFragment.addToWishList.setImageResource(R.drawable.ic__favorite_border_24)
        }
    }


    fun createAlertToSignIn(context: Context) {
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


    override fun onDestroy() {
        super.onDestroy()
        productDetailsViewMode.progressPar.value = false
    }


}