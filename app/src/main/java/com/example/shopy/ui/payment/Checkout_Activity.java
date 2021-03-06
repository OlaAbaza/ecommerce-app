package com.example.shopy.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shopy.R;
import com.example.shopy.base.ViewModelFactory;
import com.example.shopy.data.dataLayer.RepositoryImpl;
import com.example.shopy.data.dataLayer.entity.orderGet.GetOrders;
import com.example.shopy.data.dataLayer.remoteDataLayer.RemoteDataSourceImpl;
import com.example.shopy.data.dataLayer.room.RoomDataSourceImpl;
import com.example.shopy.datalayer.localdatabase.room.RoomService;
import com.example.shopy.ui.mainActivity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

    public class Checkout_Activity extends AppCompatActivity {
        // 10.0.2.2 is the Android emulator's alias to localhost
        //private static final String BACKEND_URL = "http://10.0.2.2:4242/";
        private static final String BACKEND_URL = "https://strip-payment-backend.herokuapp.com/";
        String amount;
        private static Context mContext;
        public static GetOrders.Order order;
        public static RepositoryImpl repositoryImpl;
        //https://strip-payment-backend.herokuapp.com/
        private OkHttpClient httpClient = new OkHttpClient();
        private String paymentIntentClientSecret;
        private Stripe stripe;
        public static PaymentViewModel paymentViewModel;
        ImageButton img1;
        ImageButton img2;
        CircularProgressButton payButton;
        CardInputWidget cardInputWidget;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_checkout_);
            img1 = findViewById(R.id.btn);
            mContext=this;
            img2 = findViewById(R.id.btn_hide);
             payButton = findViewById(R.id.payButton);
             cardInputWidget = findViewById(R.id.cardInputWidget);


            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.VISIBLE);
                    payButton.setVisibility(View.VISIBLE);
                    cardInputWidget.setVisibility(View.VISIBLE);

                }
            });


            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.GONE);
                payButton.setVisibility(View.GONE);
                cardInputWidget.setVisibility(View.GONE);
                }
            });

            repositoryImpl = new RepositoryImpl(new RemoteDataSourceImpl(), new RoomDataSourceImpl(RoomService.Companion.getInstance(getApplication())));

            ViewModelFactory viewModelFactory = new ViewModelFactory(repositoryImpl, getApplication());
            paymentViewModel =  new ViewModelProvider(this, viewModelFactory).get(PaymentViewModel.class);
            amount = getIntent().getStringExtra("amount");
            order = (GetOrders.Order) getIntent().getSerializableExtra("order");
            // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
            stripe = new Stripe(
                    getApplicationContext(),
                    Objects.requireNonNull("pk_test_51J0wUlCvjNQPIjCLpBSsARNPFduF1xrgZp5dFG5ekZQBIsPfzIOERByyqlrKjKHgYBL3yTKuhon1hjD7Nyhtdfk8006sfOPu0U")
            );
            startCheckout();
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

        private void startCheckout() {
            // Create a PaymentIntent by calling the server's endpoint.
            MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//            String json = "{"
//                    + "\"currency\":\"usd\","
//                    + "\"items\":["
//                    + "{\"id\":\"photo_subscription\"}"
//                    + "]"
//                    + "}";

            //Double.parseDouble()
            double amountt = Double.parseDouble(amount)*100;
            Log.i("help",amountt+"********");
            Map<String,Object> payMap = new HashMap<>();
            Map<String,Object> itemMap = new HashMap<>();
            List<Map<String,Object>> itemList = new ArrayList<>();
            payMap.put("currency","usd");
            itemMap.put("id","photo_subscription");
            itemMap.put("amount",amountt);
            itemList.add(itemMap);
            payMap.put("items",itemList);
            String json = new Gson().toJson(payMap);
            RequestBody body = RequestBody.create(json, mediaType);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "create-payment-intent")
                    .post(body)
                    .build();
            httpClient.newCall(request)
                    .enqueue(new PayCallback(this));

            // Hook up the pay button to the card widget and stripe instance
            payButton.setOnClickListener((View view) -> {
                payButton.startAnimation();

                //[do some async task. When it finishes]
                //You can choose the color and the image after the loading is finished
                Bitmap bitmap =
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_check_24);
                if (bitmap != null) {
                    payButton.doneLoadingAnimation(Color.WHITE, bitmap);
                }
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                if (params != null) {
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe.confirmPayment(this, confirmParams);


                }
            });
        }

        private void displayAlert(@NonNull String title,
                                  @Nullable String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message);

            builder.setPositiveButton("Ok", null);
            builder.create().show();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Handle the result of stripe.confirmPayment
            stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
        }

        private void onPaymentSuccess(@NonNull final Response response) throws IOException {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> responseMap = gson.fromJson(
                    Objects.requireNonNull(response.body()).string(),
                    type
            );

            paymentIntentClientSecret = responseMap.get("clientSecret");
        }

        private static final class PayCallback implements Callback {
            @NonNull private final WeakReference<Checkout_Activity> activityRef;

            PayCallback(@NonNull Checkout_Activity activity) {
                activityRef = new WeakReference<>(activity);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                final Checkout_Activity activity = activityRef.get();
                if (activity == null) {
                    return;
                }

                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Some Error Happened, Please try again...", Toast.LENGTH_LONG
                        ).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response)
                    throws IOException {
                final Checkout_Activity activity = activityRef.get();
                if (activity == null) {
                    return;
                }

                if (!response.isSuccessful()) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(
                                    activity, "Some Error Happened, Please try again...", Toast.LENGTH_LONG
                            ).show()
                    );
                } else {
                    activity.onPaymentSuccess(response);
                }
            }
        }

        private static final class PaymentResultCallback
                implements ApiResultCallback<PaymentIntentResult> {
            @NonNull private final WeakReference<Checkout_Activity> activityRef;

            PaymentResultCallback(@NonNull Checkout_Activity activity) {
                activityRef = new WeakReference<>(activity);
            }

            @Override
            public void onSuccess(@NonNull PaymentIntentResult result) {
                final Checkout_Activity activity = activityRef.get();
                if (activity == null) {
                    return;
                }

                PaymentIntent paymentIntent = result.getIntent();
                PaymentIntent.Status status = paymentIntent.getStatus();
                if (status == PaymentIntent.Status.Succeeded) {
                    // Payment completed successfully
 //                   Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    activity.displayAlert(
//                            "Payment completed",
//                            gson.toJson(paymentIntent)
//                    );

                    Toast.makeText(activity,"Ordered Successfully",Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1500);
                       // Toast.makeText(activity,"Open Home Activity",Toast.LENGTH_LONG).show();
                       paymentViewModel.cancelOrder(order.getId());
                        paymentViewModel.createOrderInPayment(order);
                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                    // Payment failed ??? allow retrying using a different payment method
                    activity.displayAlert(
                            "Payment failed",
                            Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                    );
                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                final Checkout_Activity activity = activityRef.get();
                if (activity == null) {
                    return;
                }

                // Payment request failed ??? allow retrying using the same payment method
                activity.displayAlert("Error", e.toString());
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            payButton.dispose();
        }
        //        var customerOrder = CustomerOrder(customerID.toLong())
//        var lineItem: MutableList<LineItem> = arrayListOf()
//
//        val items = orderItemsAdapter.orderList.map {
//            it.variants?.get(0)
//        }
//            Timber.i("itemss"+items)
//                for(item in items){
//            lineItem.add(LineItem(item?.inventory_quantity, item?.id))
//        }
//        getPaymentMethod()
//        var order = Order(customerOrder, "pending", lineItem,paymentMethod)
//        var orders = Orders(order)
//            orderViewModel.createOrder(orders)



    }
