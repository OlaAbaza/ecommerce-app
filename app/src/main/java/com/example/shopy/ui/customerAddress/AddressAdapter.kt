package com.example.shopy.ui.customerAddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.models.Addresse
import com.example.shopy.databinding.ItemAddressBinding

class AddressAdapter(
    var addressList: ArrayList<Addresse>,  var addressViewModel: AddressViewModel
) : RecyclerView.Adapter<AddressAdapter.VH>() {

    fun updateAddress(newAddress: Addresse?) {
        newAddress?.let {
            addressList.add(newAddress)
            notifyDataSetChanged()  }

    }
    fun addNewList(addressNewList: List<Addresse>) {
            addressList.clear()
            addressList.addAll(addressNewList)
            notifyDataSetChanged()

    }
    fun delItem(pos:Int) {
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
        holder.myView.fullNameTxt.text = addressList[position].firstName
        holder.myView.countryTxt.text = addressList[position].country
        holder.myView.addressTxt.text = addressList[position].address1
        addressList[position].default?.let {
            holder.myView.rbtnAddress.isChecked = it
        }
        holder.myView.itemLayout.setOnClickListener {
          addressViewModel.onItemClick(addressList[position])
        }
        holder.myView.btnDel.setOnClickListener {
            addressViewModel.delItem(addressList[position],position)
        }
        holder.myView.rbtnAddress.setOnClickListener{
             addressViewModel.onCheckBoxClick(addressList[position])
        }
    }

    override fun getItemCount() = addressList.size

}