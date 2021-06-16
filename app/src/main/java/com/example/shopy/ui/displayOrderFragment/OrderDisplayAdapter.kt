package com.example.shopy.ui.displayOrderFragment

//import android.util.Log
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import java.lang.ref.WeakReference


class OrderDisplayAdapter(context: Context,
    list: List<GetOrders.Order?>, imagelist: List<List<String>>,
    private var payNowMutableData: MutableLiveData<GetOrders.Order>,
    private var showOrderDetails: MutableLiveData<Long>,
    private var cancelMutableData: MutableLiveData<GetOrders.Order>
) : RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder>() {
    var list:
            List<GetOrders.Order?> = list
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var imagelist:
            List<List<String>> = imagelist
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var positionA: Int = 0
    var context: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_display, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        positionA = position
        holder.binding(list[position])

        val adapterImages  = WeakReference(ProductsImageAdapter(emptyList())).get()!!
        if (imagelist.isNotEmpty()) {
            holder.bindingRecycler(context, adapterImages, imagelist[position])
            adapterImages.imageList=imagelist[position]
            adapterImages.notifyDataSetChanged()

        }else{
            holder.bindingRecycler(context, adapterImages, emptyList())
        }

        holder.itemView.setOnClickListener {
            showOrderDetails.value = list[position]?.id
        }


        holder.payNow.setOnClickListener {
            payNowMutableData.value = list[position]
        }
        holder.cancel.setOnClickListener {
            cancelMutableData.value = list[position]
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId = itemView.findViewById<TextView>(R.id.orderIdEditable)
        private val textPrice = itemView.findViewById<TextView>(R.id.totalPriceEditable)
        private val currencyCode = itemView.findViewById<TextView>(R.id.currency_code)
        private val cash = itemView.findViewById<TextView>(R.id.cashText)
        private val createdAt = itemView.findViewById<TextView>(R.id.createdAtEditable)
        val payNow: Button = itemView.findViewById(R.id.payButton)
        val cancel: Button = itemView.findViewById(R.id.cancelButton)
        val imageorderRecycler = itemView.findViewById<RecyclerView>(R.id.imageItemsRecycler)

        fun binding(order: GetOrders.Order?) {
            textId.text = "#" + order!!.id.toString()
            textPrice.text = order.total_price ?: "not known"
            currencyCode.text = order.presentment_currency ?: "not known"
            createdAt.text = order.created_at ?: "not known"

            if (order.note == "Cash") {
                payNow.visibility = View.GONE
                cash.visibility = View.VISIBLE

                if (order.financial_status == "paid") {
                    cancel.visibility = View.GONE
                    cash.visibility = View.GONE
                } else {
                    cancel.visibility = View.VISIBLE
                }

            } else {
                cash.visibility = View.GONE
                if (order.financial_status == "paid") {
                    payNow.visibility = View.GONE
                    cancel.visibility = View.GONE
                } else {
                    payNow.visibility = View.VISIBLE
                    cancel.visibility = View.VISIBLE
                }
            }


        }


        fun bindingRecycler(context: Context, adapter: ProductsImageAdapter, list: List<String>) {

            imageorderRecycler.apply {
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                this.layoutManager = layoutManager
                this.adapter = adapter
                adapter.imageList = list
            }
        }
    }


}