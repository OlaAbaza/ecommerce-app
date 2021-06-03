package com.example.shopy.ui.productDetailsActivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.adapters.ImageSilderAdapter
import com.example.shopy.adapters.OptionsAdapter
import com.example.shopy.databinding.ActivityProductDetailsBinding
import com.example.shopy.datalayer.entity.itemPojo.Image
import com.example.shopy.datalayer.entity.itemPojo.Images
import com.example.shopy.datalayer.entity.itemPojo.Product

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var bindingProductDetailsActivity: ActivityProductDetailsBinding
    lateinit var images: List<Images>
    lateinit var productDetailsViewMode: ProductDetailsViewModel
    lateinit var imageSliderAdaper: ImageSilderAdapter
    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProductDetailsActivity =
            DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        images = ArrayList()
        productDetailsViewMode = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ProductDetailsViewModel::class.java]

        productDetailsViewMode.getProductByIdFromNetwork(6687367364806)
        productDetailsViewMode.observeProductDetails().observe(this, {
            product = it.product
            updateUI(product)
        })



        imageSliderAdaper = ImageSilderAdapter(images)
        bindingProductDetailsActivity.viewPagerMain.adapter = imageSliderAdaper

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
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(1)?.values?.let { OptionsAdapter(it) }
        }


        bindingProductDetailsActivity.sizeEditable.apply {
            this.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.adapter = product.options?.get(0)?.values?.let { OptionsAdapter(it) }
        }

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
                true
            }
            R.id.shopping_bag -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}