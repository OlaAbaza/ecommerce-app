package com.example.shopy.ui.shoppingBag

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopy.NavGraphDirections
import com.example.shopy.R
import com.example.shopy.base.NetworkChangeReceiver
import com.example.shopy.base.ViewModelFactory
import com.example.shopy.data.dataLayer.Repository
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders
import com.example.shopy.data.dataLayer.entity.priceRules.PriceRule
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl
import com.example.shopy.databinding.FragmentOrderConfirmationBinding
import com.example.shopy.datalayer.entity.ads_discount_codes.AllCodes
import com.example.shopy.datalayer.entity.ads_discount_codes.DiscountCode
import com.example.shopy.datalayer.localdatabase.room.RoomService
import com.example.shopy.datalayer.sharedprefrence.MeDataSharedPrefrenceReposatory
import com.example.shopy.models.*
import com.example.shopy.ui.payment.Checkout_Activity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_order_confirmation.*
import timber.log.Timber
import java.io.Serializable


class OrderConfirmationFragment : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var meDataSourceReo: MeDataSharedPrefrenceReposatory
    private lateinit var binding: FragmentOrderConfirmationBinding
    private lateinit var orderItemsAdapter: OrderItemsAdapter

    private var totalPrice = 0.0f
    private var totalDiscont = 0.0f
    private var discountAmount = 0.0f
    private var discountCode = ""
    private var customerID = ""
    private var paymentMethod = ""
    private var isDefaultAddress = false
    private var isDiscount = false
    private var priceRulesList: List<PriceRule> = arrayListOf()
    private var discountCodesList: List<DiscountCode> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        meDataSourceReo = MeDataSharedPrefrenceReposatory(requireActivity())
        binding = FragmentOrderConfirmationBinding.inflate(layoutInflater)
        val args: OrderConfirmationFragmentArgs by navArgs()
        totalPrice = args.totalPrice
        val application = requireNotNull(this.activity).application
        val repository = Repository(
            RemoteDataSourceImpl(),
            RoomDataSourceImpl(RoomService.getInstance(application))
        )
        val viewModelFactory = ViewModelFactory(repository, application)
        orderViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(OrderViewModel::class.java)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      changeToolbar()

        if (isLoged()) {
            customerID = meDataSourceReo.loadUsertId()
            Timber.i("olaaa" + customerID)
            if (NetworkChangeReceiver.isOnline) {
                orderViewModel.getCustomersAddressList(customerID)
            }
            else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.thereIsNoNetwork),
                    Toast.LENGTH_SHORT
                ).show()

            }

          //  orderViewModel.getPriceRulesList()
           // orderViewModel.fetchallDiscountCodeList()
        }

        orderItemsAdapter = OrderItemsAdapter(arrayListOf(), orderViewModel)
        binding.rvCartItems.apply {
            adapter = orderItemsAdapter
        }
        orderViewModel.getAllCartList().observe(viewLifecycleOwner, {
            orderItemsAdapter.addNewList(it)

        })

        orderViewModel.getAddressList().observe(viewLifecycleOwner, Observer<List<Addresse>?> {
            val dafultAddress: MutableList<Addresse> = arrayListOf()
            for (item in it) {
                if (item.default == true) {
                    dafultAddress.add(item)
                    Timber.i("olaada" + item.default)
                }
            }

            if (dafultAddress.isEmpty()) {
                binding.group.visibility = View.INVISIBLE
                binding.addAddressText.visibility = View.VISIBLE
                isDefaultAddress = false
            } else {
                isDefaultAddress = true
                binding.group.visibility = View.VISIBLE
                binding.addAddressText.visibility = View.INVISIBLE
                binding.fullNameTxt.text = dafultAddress.get(0).firstName.toString()
                binding.countryTxt.text = dafultAddress.get(0).country.toString()
                binding.addressTxt.text = dafultAddress.get(0).address1.toString()
            }

        })
        binding.addressBtn.setOnClickListener {
            val action = NavGraphDirections.actionGlobalAddressFragment()
            findNavController().navigate(action)
        }

        binding.discountBtn.setOnClickListener {
            binding.discountEdt.visibility = View.VISIBLE
            binding.discountBtn.visibility = View.INVISIBLE
            binding.discountBtnHide.visibility = View.VISIBLE
        }
        binding.discountBtnHide.setOnClickListener {
            discount_edt.visibility = View.INVISIBLE
            binding.discountBtnHide.visibility = View.INVISIBLE
            binding.discountBtn.visibility = View.VISIBLE
        }
        binding.placeOrderBtn.setOnClickListener {
            if (isDefaultAddress) {
                placeOrder()

            }
            else {
                Toast.makeText(context, "please, set your address", Toast.LENGTH_SHORT).show()
            }
        }
        orderViewModel.getPostOrder().observe(viewLifecycleOwner, Observer<GetOrders?> {
            if (it!=null) {
                Timber.i("order+" + it)
                orderViewModel.delAllItems()
//                val action = NavGraphDirections.actionGlobalShopTabFragment2()
//                findNavController().navigate(action)
                it?.let {
                   // findNavController().popBackStack(R.id.meFragment, true)
                    //val navOption = NavOptions.Builder().setPopUpTo(R.id.meFragment, false).build()
                    startActivity(
                        Intent(requireActivity(), Checkout_Activity::class.java).putExtra(
                            "amount",
                            totalPrice.toString()
                        )
                            .putExtra("order", it.orders as? Serializable)
                    )
                    findNavController().navigateUp()
                }

            } else {
                Toast.makeText(context, "error Try again please", Toast.LENGTH_SHORT).show()
            }
        })
//        orderViewModel.getPriceRules().observe(viewLifecycleOwner, Observer<List<PriceRule>?> {
//            priceRulesList=it
//
//        })

        orderViewModel.fetchallDiscountCodeList().observe(viewLifecycleOwner, Observer<AllCodes> {
            discountCodesList = it.discountCodes

        })
        binding.discountEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Timber.i("olaa  afterTextChanged")
                checkDiscountCode()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Timber.i("olaa  beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.i("olaa  onTextChanged")
            }
        })

        binding.totalPriceTxt.text = totalPrice.toString() + " EGP"
        binding.totalItemTxt.text = totalPrice.toString() + " EGP"

    }

//    private fun checkDiscountCode() {
//        val discount: List<PriceRule> =
//            priceRulesList.filter {
//                it.title == binding.discountEdt.text.toString().trim()
//            }
//        if (discount.isEmpty()) {
//            Toast.makeText(context, "Sorry, this coupon is invalid", Toast.LENGTH_SHORT).show()
//        } else {
//            binding.totalDiscountTxt.text = discount.get(0).value+"%"
//            totalDiscont= discount.get(0).value?.toFloat() ?:0.0f
//            Timber.i("olaaa discountAmount"+discountAmount+"  "+totalDiscont+"  "+totalPrice)
//            discountAmount = (totalPrice*totalDiscont)/100
//            binding.totalPriceTxt.text=(totalPrice+totalDiscont).toString() + " EGP"
//
//
//        }
//    }
    private fun checkDiscountCode() {
        val discount: List<DiscountCode> =
            discountCodesList.filter {
                Timber.i("olaaa code "+it.code)
                it.code == binding.discountEdt.text.toString().trim()
            }
        if (discount.isEmpty()) {
            isDiscount=false
            binding.discountEdt.setError("Sorry, this coupon is invalid")
            binding.totalDiscountTxt.text = "---"
            binding.totalPriceTxt.text=(totalPrice).toString() + " EGP"
           // Toast.makeText(context, "Sorry, this coupon is invalid", Toast.LENGTH_SHORT).show()
        } else {
            isDiscount=true
            binding.totalDiscountTxt.text = "10%"
            discountAmount = ((totalPrice*10)/100)
            discountCode=discount.get(0).code
            Timber.i("olaaa discountAmount"+discountAmount+"  "+totalDiscont+"  "+totalPrice)
            totalPrice=(totalPrice-discountAmount)
            binding.totalPriceTxt.text=totalPrice.toString() + " EGP"

        }
    }


    private fun placeOrder() {
        var customerOrder = CustomerOrder(customerID.toLong())
        var lineItem: MutableList<LineItem> = arrayListOf()
        var discount: MutableList<DiscountCodes>? = arrayListOf()
        val items = orderItemsAdapter.orderList.map {
            it.variants?.get(0)
        }
        Timber.i("itemss" + items)
        for (item in items) {
            lineItem.add(LineItem(item?.inventory_quantity, item?.id))
        }
        getPaymentMethod()
        if(isDiscount)
            discount?.add(DiscountCodes(discountAmount.toString(),discountCode))
        else
            discount=null

        var order = Order(customerOrder, "pending", lineItem, paymentMethod,discount)
        var orders = Orders(order)
        if (NetworkChangeReceiver.isOnline) {
            orderViewModel.createOrder(orders)
        }
        else {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereIsNoNetwork),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun getPaymentMethod() {
        if (binding.radioCash.isChecked)
            paymentMethod = "Cash"
        else if (binding.radioCredit.isChecked)
            paymentMethod = "Card"
    }

    private fun isLoged(): Boolean {
        return meDataSourceReo.loadUsertstate()
    }
    private fun changeToolbar() {
        requireActivity().bottom_nav.visibility = View.GONE
        requireActivity().toolbar.visibility = View.VISIBLE
        requireActivity().toolbar.searchIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.cartView.visibility = View.INVISIBLE
        requireActivity().toolbar.settingIcon.visibility = View.INVISIBLE
        requireActivity().toolbar.favourite.visibility = View.INVISIBLE
        requireActivity().toolbar_title.setTextColor(Color.WHITE)

        requireActivity().toolbar.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        requireActivity().toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        requireActivity().toolbar.setNavigationOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        requireActivity().toolbar_title.text = "Order Confirmation"
    }
}