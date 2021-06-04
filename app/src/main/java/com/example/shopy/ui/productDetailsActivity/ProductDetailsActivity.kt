package com.example.shopy.ui.productDetailsActivity

import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopy.R
import com.example.shopy.adapters.ImageSilderAdapter
import com.example.shopy.adapters.OptionsAdapter
import com.example.shopy.databinding.ActivityProductDetailsBinding
import com.example.shopy.datalayer.entity.itemPojo.*




class ProductDetailsActivity : AppCompatActivity() {
    lateinit var bindingProductDetailsActivity: ActivityProductDetailsBinding
    lateinit var images: List<Images>
    lateinit var productDetailsViewMode: ProductDetailsViewModel
    lateinit var imageSliderAdaper: ImageSilderAdapter
    lateinit var product: Product
    var id: Long? = null
    var stored = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProductDetailsActivity =
            DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        images = ArrayList()
        productDetailsViewMode = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ProductDetailsViewModel::class.java]

        if (savedInstanceState != null) {
            id = savedInstanceState.getLong("ID")
            stored = savedInstanceState.getBoolean("STORED")
            setStoredButton(stored)

        } else {
            //todo from Intent
            id = intent?.getLongExtra("ID",0)
            checkWishListStored(id ?: 0)
        }


        productDetailsViewMode.getProductByIdFromNetwork(id = id ?: 0)

        imageSliderAdaper = ImageSilderAdapter(images)
        bindingProductDetailsActivity.viewPagerMain.adapter = imageSliderAdaper



        productDetailsViewMode.observeProductDetails().observe(this, {
            product = it.product
            updateUI(product)
            productDetailsViewMode.progressPar.value = true


        })

        productDetailsViewMode.progressPar.observe(this,{
            if (productDetailsViewMode.progressPar.value == true)
            bindingProductDetailsActivity.progressPar.visibility = View.GONE
        })



        bindingProductDetailsActivity.addToWishList.setOnClickListener {
            if (stored) {
                productDetailsViewMode.deleteOneWishItem(id = id ?: 0)
                stored = false
            } else {
                productDetailsViewMode.saveWishList(product)
                stored = true
            }
            setStoredButton(stored)
        }


        bindingProductDetailsActivity.addToCart.setOnClickListener {
            productDetailsViewMode.saveCartList(
                ProductCartModule(
                    product.id,
                    product.title,
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
            Toast.makeText(this, getString(R.string.item_saved_in_cart), Toast.LENGTH_SHORT).show()
        }

        productDetailsViewMode.signInBoolesn.observe(this, {
            createAlertToSignIn(this)
        })

    }

    private fun checkWishListStored(id: Long) {
        productDetailsViewMode.getOneWithItemFromRoom(id).observe(this, {
            if (it != null) {
                stored = true
                setStoredButton(stored)
            }
        })
    }

    private fun updateUI(product: Product) {
        title = product.title ?: "null"

        imageSliderAdaper.images = product.images ?: ArrayList()
        imageSliderAdaper.notifyDataSetChanged()


        bindingProductDetailsActivity.prise.text = product.variants?.get(0)?.price.toString()

        bindingProductDetailsActivity.descriptionEditable.text = product.body_html
        bindingProductDetailsActivity.stateEditable.text = product.status ?: "not known"

        if (product.status.equals("active")) {
            bindingProductDetailsActivity.stateEditable.setTextColor(
                AppCompatResources.getColorStateList(
                    this,
                    R.color.green
                )
            )
        }

        bindingProductDetailsActivity.vendorEditable.text = product.vendor

        bindingProductDetailsActivity.colorEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(1)?.values?.let { OptionsAdapter(it) }
        }


        bindingProductDetailsActivity.sizeEditable.apply {
            this.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(0)?.values?.let { OptionsAdapter(it) }
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        id?.let { outState.putLong("ID", it) }
        outState.putBoolean("STORED", stored)
    }

    fun setStoredButton(stored: Boolean) {
        if (stored) {
            bindingProductDetailsActivity.addToWishList.setImageResource(R.drawable.ic_favorite_24)
        } else {
            bindingProductDetailsActivity.addToWishList.setImageResource(R.drawable.ic__favorite_border_24)
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.product_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.home -> {
                // todo for intent
                // val intent = Intent(context, Home::class.java)
                // startActivity(intent)
                true
            }
            R.id.shopping_bag -> {
                //todo for intent
                // val intent = Intent(context, Home::class.java)
                // startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailsViewMode.progressPar.value=false
    }
}