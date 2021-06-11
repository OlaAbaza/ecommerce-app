package com.example.shopy.ui.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopy.R
import com.paypal.android.sdk.payments.*
import kotlinx.android.synthetic.main.activity_pay_pal.*
import java.math.BigDecimal as JavaMathBigDecimal


class PayPalActivity : AppCompatActivity() {
    var m_configuration: PayPalConfiguration? = null

    //the id is the link to paypal account,we have to create an app and get its id
    var m_PaypalClientId = "AXExM_ysFYecA99EfnPQDY8XjcQqs89-eDb5p4EDJoOuStfiabv4bfZkEIB7EVMYqpO_EDWsQQYFjLWA"
    var m_service: Intent? = null
    var m_paypalRequestCode = 999 //or any number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_pal)


        m_configuration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) //sandbox for test,production for real
            .clientId(m_PaypalClientId)
        m_service = Intent(this, PayPalService::class.java)
        m_service!!.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration)
        startService(m_service)

        //pay button
        pay.setOnClickListener {
            val payment = PayPalPayment(
                JavaMathBigDecimal(10.0), "USD",
                "Test Payment with Paypal", PayPalPayment.PAYMENT_INTENT_SALE
            )

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
            startActivityForResult(intent, m_paypalRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == m_paypalRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                //we have to confirm that payment worked
                val confirmation: PaymentConfirmation = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)!!
                if (confirmation != null) {
                    val state = confirmation.proofOfPayment.state
                    if (state == "approved") {
                        //if the payment worked state will be approved
                        Toast.makeText(this, "Approved :)", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error in payment :(", Toast.LENGTH_LONG).show()
                    }
                } else Toast.makeText(this, "Confirmation is Null :(", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, " :(", Toast.LENGTH_LONG).show()
        }
    }
}