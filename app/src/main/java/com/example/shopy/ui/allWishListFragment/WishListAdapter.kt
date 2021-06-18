package com.example.shopy.ui.allWishListFragment

import android.util.Log
import com.example.shopy.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.datalayer.entity.itemPojo.Product
import kotlin.random.Random


class WishListAdapter(
    prouductList: List<Product>,
    intentTOProductDetails: MutableLiveData<Product>,
    deleteItem: MutableLiveData<Product>
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    val intentTOProductDetails: MutableLiveData<Product> = intentTOProductDetails
    val deleteItem: MutableLiveData<Product> = deleteItem
    private var lastPosition = -1
    var productList: List<Product> = ArrayList()
        set(value) {
            field = value
        }

    init {
        this.productList = prouductList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.wish_list_item, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setAnimation(holder.itemView,position)
        holder.binding(productList[position], position)
        holder.itemView.setOnClickListener {
            intentTOProductDetails.value = productList[position]

        }
        holder.deleteButton.setOnClickListener {
            deleteItem.value = productList[position]
        }
    }

    override fun getItemCount(): Int {
        return productList.count()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemViewa: View = itemView
        private val companyLogo = itemView.findViewById<ImageView>(R.id.itemImage)
        private val itemTitle = itemView.findViewById<TextView>(R.id.title)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.delete)
        fun binding(item: Product, position: Int) {
            Glide.with(companyLogo)
                .load(item.image.src ?: "")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .into(companyLogo)
            itemTitle.text = item.variants?.get(0)?.price.toString()+"EGP"


        }
    }
    protected fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration =
                Random.nextInt(501).toLong() //to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

}