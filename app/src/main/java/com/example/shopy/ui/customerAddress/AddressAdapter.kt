package com.example.shopy.ui.customerAddress

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.databinding.ItemAddressBinding
import com.example.shopy.models.Addresse
import kotlin.collections.ArrayList
import kotlin.random.Random


class AddressAdapter(
    var addressList: ArrayList<Addresse>,
    var addressViewModel: AddressViewModel,
    var context: Context
) : RecyclerView.Adapter<AddressAdapter.VH>() {

    private var lastPosition = -1
    fun addNewList(addressNewList: List<Addresse>) {
            addressList.clear()
            addressList.addAll(addressNewList)
            notifyDataSetChanged()

    }
    fun delItem(pos: Int) {
        addressList.removeAt(pos);
        notifyItemRemoved(pos);

    }
    class VH(var myView: ItemAddressBinding) : RecyclerView.ViewHolder(myView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewBinding =
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(viewBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView,position)

        holder.myView.fullNameTxt.text = addressList[position].firstName
        holder.myView.countryTxt.text = addressList[position].country
        holder.myView.addressTxt.text = addressList[position].address1
        addressList[position].default?.let {
            holder.myView.rbtnAddress.isChecked = it
        }
//        if( addressList[position].default==true){
//            holder.myView.btnDel.visibility=View.INVISIBLE
//        }
        holder.myView.itemLayout.setOnClickListener {
          addressViewModel.onItemClick(addressList[position])
        }
        holder.myView.btnDel.setOnClickListener {
            addressViewModel.delItem(addressList[position], position)
        }
        holder.myView.rbtnAddress.setOnClickListener{
             addressViewModel.onCheckBoxClick(addressList[position])
        }

    }

    override fun getItemCount() = addressList.size
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