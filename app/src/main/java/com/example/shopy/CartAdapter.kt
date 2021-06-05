package com.example.shopy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.ItemCartBinding
import com.example.shopy.models.Order
import com.example.shopy.models.Variants

class CartAdapter(
    var orderList: ArrayList<Variants>, var orderViewModel: OrderViewModel
) : RecyclerView.Adapter<CartAdapter.VH>() {

    fun updateOrder(newOrder: Variants?) {
        newOrder?.let {
            orderList.add(it)
            notifyDataSetChanged()  }

    }
    fun addNewList(orderNewList: List<Variants>) {
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
        holder.myView.itemCartTitle.text = " Disney Donald Duck T-Shirt"
        holder.myView.itemCountText.text = "2"
        holder.myView.itemCartPrice.text = "US$6.00"

        holder.myView.decreaseButton.setOnClickListener {
            holder.myView.itemCountText.text=((holder.myView.itemCountText.text.toString().toInt())-1).toString()
        }
        holder.myView.increaseButton.setOnClickListener {
            holder.myView.itemCountText.text=((holder.myView.itemCountText.text.toString().toInt())+1).toString()
        }
    }

    override fun getItemCount() = orderList.size

}