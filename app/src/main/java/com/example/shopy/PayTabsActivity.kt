package com.example.shopy


import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cards.pay.paycardsrecognizer.sdk.BuildConfig
import cards.pay.paycardsrecognizer.sdk.BuildConfig.VERSION_NAME
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startAlternativePaymentMethods
import com.payment.paymentsdk.PaymentSdkActivity.Companion.startCardPayment
import com.payment.paymentsdk.PaymentSdkConfigBuilder
import com.payment.paymentsdk.integrationmodels.*
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface
import kotlinx.android.synthetic.main.activity_pay_tabs.*
import kotlinx.android.synthetic.main.activity_pay_tabs.view.*

class PayTabsActivity : AppCompatActivity(), CallbackPaymentInterface {
    private var token: String? = null
    private var transRef: String? = null
    // private lateinit var b: PaytabsPaymentActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // b = PaytabsPaymentActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_pay_tabs)
        // val view = b.root
        version.text = "Version: " + BuildConfig.VERSION_NAME
        pay.setOnClickListener {
            val configData = generatePaytabsConfigurationDetails()
            startCardPayment(this, configData, this)
        }
        apm_pay.setOnClickListener {
            val configData = generatePaytabsConfigurationDetails()
            startAlternativePaymentMethods(this, configData, this)
        }
//        findViewById<View>(R.id.sam_pay).setOnClickListener { v: View? ->
//            SamsungPayActivity.start(this, generatePaytabsConfigurationDetails())
//        }

        amount.setText("20")
        currency.selectedIndex = 7
        language.selectedIndex = 1
        tokeniseType.selectedIndex = 1

        street.setText("address street")
        city.setText("Dubai")
        state.setText("3510")
        country.setText("AE")
        postal_code.setText("54321")
        shipping_email.setText("email1@domain.com")
        shipping_name.setText("name1 last1")
        shipping_phone.setText("1234")

        street_billing.setText("street2")
        city_billing.setText("Dubai")
        state_billing.setText("3510")
        country_billing.setText("AE")
        postal_code.setText("12345")
        billing_email.setText("email1@domain.com")
        billing_name.setText("first last")
        billing_phone.setText("45")
        (findViewById<View>(R.id.screen_density) as TextView).text =
            "Screen Density " + resources.displayMetrics.density
    }


    private fun generatePaytabsConfigurationDetails(): PaymentSdkConfigurationDetails {
        val profileId = mid.text.toString()
        val serverKey = serverKey.text.toString()
        val clientKey = client_key.text.toString()
        val locale = getLocale()
        val transactionTitle = transactionTitle.text.toString()
        val orderId = cartId.text.toString()
        val productName = productName.text.toString()
        val cartDesc = cartDesc.text.toString()
        val currency = currency.text.toString()
        val amount = amount.text?.toString()?.toDoubleOrNull() ?: 0.0
        val billingData = PaymentSdkBillingDetails(
            city_billing.text.toString(), country_billing.text.toString(),
            billing_email.text.toString(), billing_name.text.toString(),
            billing_phone.text.toString(), state_billing.text.toString(),
            street_billing.text.toString(), postal_code_billing.text.toString()
        )
        val shippingData = PaymentSdkShippingDetails(
            city.text.toString(), country.text.toString(),
            shipping_email.text.toString(), shipping_name.text.toString(),
            shipping_phone.text.toString(), state.text.toString(),
            street.text.toString(), postal_code.text.toString()
        )
        val configData = PaymentSdkConfigBuilder(profileId, serverKey, clientKey, amount, currency)
            .setCartDescription(cartDesc)
            .setLanguageCode(locale)
            .setBillingData(billingData)
            .setMerchantCountryCode(merchant_country.text.toString())
            .setShippingData(shippingData)
            .setTransactionType(if (transaction_type.selectedIndex == 0) PaymentSdkTransactionType.SALE else PaymentSdkTransactionType.AUTH)
            .setTransactionClass(PaymentSdkTransactionClass.ECOM)
            .setCartId(orderId)
            .setAlternativePaymentMethods(getSelectedApms())
            .setTokenise(getTokeniseType())
            .setTokenisationData(token, transRef)
            .showBillingInfo(complete_billing_info.isChecked)
            .showShippingInfo(complete__shipping_info.isChecked)
            .forceShippingInfo(force_shipping_validation.isChecked)
            .setScreenTitle(transactionTitle)
        if (showMerchantLogo.isChecked) {
            configData.setMerchantIcon(resources.getDrawable(R.drawable.payment_sdk_adcb_logo))
        }


        return configData.build()
    }


    private fun getSelectedApms(): List<PaymentSdkApms> {
        val apms = mutableListOf<PaymentSdkApms>()
        addApmToList(apms, PaymentSdkApms.STC_PAY, apm_stcPay.isChecked)
        addApmToList(apms, PaymentSdkApms.UNION_PAY, apm_unionpay.isChecked)
        addApmToList(apms, PaymentSdkApms.VALU, apm_valu.isChecked)
        addApmToList(apms, PaymentSdkApms.KNET_DEBIT, apm_knet_debit.isChecked)
        addApmToList(apms, PaymentSdkApms.FAWRY, apm_fawry.isChecked)
        addApmToList(apms, PaymentSdkApms.OMAN_NET, apm_omannet.isChecked)
        addApmToList(apms, PaymentSdkApms.MEEZA_QR, apm_meeza_qr.isChecked)
        addApmToList(apms, PaymentSdkApms.MADA, apm_mada.isChecked)
        return apms
    }

    private fun addApmToList(
        list: MutableList<PaymentSdkApms>,
        apm: PaymentSdkApms,
        checked: Boolean
    ) {
        if (checked)
            list.add(apm)
    }

    private fun getTokeniseType(): PaymentSdkTokenise {
        return when (tokeniseType.selectedIndex) {
            1 -> PaymentSdkTokenise.NONE
            2 -> PaymentSdkTokenise.MERCHANT_MANDATORY
            3 -> PaymentSdkTokenise.USER_MANDATORY
            4 -> PaymentSdkTokenise.USER_OPTIONAL
            else -> PaymentSdkTokenise.NONE
        }
    }

    private fun getLocale() =
        when (language.selectedIndex) {
            1 -> PaymentSdkLanguageCode.EN
            2 -> PaymentSdkLanguageCode.AR
            else -> PaymentSdkLanguageCode.DEFAULT
        }

    override fun onError(error: PaymentSdkError) {
        Toast.makeText(this, "${error.msg}", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentCancel() {
        Toast.makeText(this, "onPaymentCancel", Toast.LENGTH_SHORT).show()

    }

    override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
        token = paymentSdkTransactionDetails.token
        transRef = paymentSdkTransactionDetails.transactionReference
        Toast.makeText(
            this,
            "${paymentSdkTransactionDetails.paymentResult?.responseMessage ?: "PaymentFinish"}",
            Toast.LENGTH_SHORT
        ).show()
    }
}

