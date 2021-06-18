package com.example.shopy.ui.shoppingBag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.databinding.ItemCartBinding
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import kotlin.random.Random


class CartAdapter(
    var orderList: ArrayList<ProductCartModule>, var orderViewModel: OrderViewModel
) : RecyclerView.Adapter<CartAdapter.VH>() {
    private var lastPosition = -1
    fun addNewList(orderNewList: List<ProductCartModule>) {
        orderList.clear()
        orderList.addAll(orderNewList)
        notifyDataSetChanged()

    }
    fun delItem(pos:Int) {
        orderList.removeAt(pos);
        notifyItemRemoved(pos);

    }
    class VH(var myView: ItemCartBinding) : RecyclerView.ViewHolder(myView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(viewBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView,position)
        holder.myView.itemCartTitle.text = orderList[position].title
        holder.myView.itemCountText.text = orderList[position].variants?.get(0)?.inventory_quantity.toString()
        holder.myView.itemCartPrice.text = orderList[position].variants?.get(0)?.price.toString()+"EGP"


        Glide.with(holder.myView.itemCartImage)
            .load(orderList[position].image.src)
            .fitCenter()
            .placeholder(R.drawable.ic_loading)
            .into(holder.myView.itemCartImage)


        holder.myView.decreaseButton.setOnClickListener {
            var num =((holder.myView.itemCountText.text.toString().toInt())-1)
            if(num>0){
            orderList[position].variants?.get(0)?.inventory_quantity = num
            holder.myView.itemCountText.text=num.toString()
                orderViewModel.onChangeQuntity()
            }
            else{
                orderViewModel.onDelClick( orderList[position].id)
            }
        }
        holder.myView.increaseButton.setOnClickListener {
            var num =((holder.myView.itemCountText.text.toString().toInt())+1)
            orderList[position].variants?.get(0)?.inventory_quantity = num
            holder.myView.itemCountText.text=num.toString()
            orderViewModel.onChangeQuntity()
        }
        holder.myView.btnFav.setOnClickListener {
            orderViewModel.onFavClick( orderList[position])
        }
        holder.myView.itemCartImage.setOnClickListener {

            orderViewModel.onImgClick( orderList[position].id)
        }
    }

    override fun getItemCount() = orderList.size
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