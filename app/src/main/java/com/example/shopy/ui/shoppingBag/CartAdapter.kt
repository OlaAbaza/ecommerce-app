package com.example.shopy.ui.shoppingBag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopy.R
import com.example.shopy.databinding.ItemCartBinding
import com.example.shopy.datalayer.entity.itemPojo.ProductCartModule
import com.example.shopy.models.Variants

class CartAdapter(
    var orderList: ArrayList<ProductCartModule>, var orderViewModel: OrderViewModel
) : RecyclerView.Adapter<CartAdapter.VH>() {

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
    }

    override fun getItemCount() = orderList.size

}