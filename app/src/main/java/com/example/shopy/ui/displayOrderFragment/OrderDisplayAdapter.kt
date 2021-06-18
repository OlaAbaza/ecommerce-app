package com.example.shopy.ui.displayOrderFragment

//import android.util.Log
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopy.R
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import kotlinx.android.synthetic.main.item_order_display.view.*
import java.lang.ref.WeakReference
import kotlin.random.Random


class OrderDisplayAdapter(context: Context,
    list: List<GetOrders.Order?>, imagelist: List<List<String>>,
    private var payNowMutableData: MutableLiveData<GetOrders.Order>,
    private var showOrderDetails: MutableLiveData<Long>,
    private var cancelMutableData: MutableLiveData<GetOrders.Order>
) : RecyclerView.Adapter<OrderDisplayAdapter.ViewHolder>() {
    private var lastPosition = -1
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
        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView,position)
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

        holder.itemView.cv_order.setOnClickListener {
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
        private val textPrice = itemView.findViewById<TextView>(R.id.totalPriceEditable)
        private val currencyCode = itemView.findViewById<TextView>(R.id.currency_code)
        private val tvPay = itemView.findViewById<TextView>(R.id.tv_pay)
       // private val cash = itemView.findViewById<TextView>(R.id.cashText)
         val seaAllDetails = itemView.findViewById<LinearLayout>(R.id.seeAllDeatails)
      //  private val createdAt = itemView.findViewById<TextView>(R.id.createdAtEditable)
        private val paymentMethod = itemView.findViewById<TextView>(R.id.paymentTypeEditable)
        val payNow: Button = itemView.findViewById(R.id.payButton)
        val cancel: Button = itemView.findViewById(R.id.cancelButton)
        val circle:LinearLayout = itemView.findViewById(R.id.circle)
        val imageorderRecycler = itemView.findViewById<RecyclerView>(R.id.imageItemsRecycler)

        @SuppressLint("SetTextI18n")
        fun binding(order: GetOrders.Order?) {
            textPrice.text = order!!.total_price ?: "not known"
            currencyCode.text = order.presentment_currency ?: "not known"
          //  createdAt.text = order.created_at ?: "not known"

            if (order.note == "Cash") {
                payNow.visibility = View.GONE
                paymentMethod.text="Cash on delivery"
               // cash.visibility = View.VISIBLE

                if (order.financial_status == "paid") {
                    cancel.visibility = View.GONE
                    circle.setBackgroundResource(R.drawable.state_paid)
                    tvPay.text = "Paid Order"
                    //cash.visibility = View.GONE
                } else {
                    tvPay.text = "waiting for payment"
                    cancel.visibility = View.VISIBLE
                    circle.setBackgroundResource(R.drawable.state_circle_shape)
                }

            } else {
                paymentMethod.text="Credit/Debit Card"
              //  cash.visibility = View.GONE
                if (order.financial_status == "paid") {
                    payNow.visibility = View.GONE
                    cancel.visibility = View.GONE
                    tvPay.text = "Paid Order"
                    circle.setBackgroundResource(R.drawable.state_paid)
                } else {
                    tvPay.text = "waiting for payment"
                    payNow.visibility = View.VISIBLE
                    cancel.visibility = View.VISIBLE
                    circle.setBackgroundResource(R.drawable.state_circle_shape)
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