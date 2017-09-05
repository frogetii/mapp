package ke.co.mapp.mapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketActivity extends AppCompatActivity {

    public int PLACE_PICKER_REQUEST = 1;
    private List<Product> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("M SHOPPING");
//        toolbar.setSubtitle("my drinks");

//        Start recyclerview

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final DBHandler db = new DBHandler(getApplicationContext());
        List<Product> productList = db.getAllProducts();

        mAdapter = new ProductAdapter(productList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

//        End recyclerview

        //start bottom navigation

//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.basket_bottom_navigation);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.done:
//                                startPlacePickerActivity();
//                                break;
//                        }
//                        return false;
//                    }
//                });

        //end bottom navigation

        //Floating Button
        FloatingActionButton sum = (FloatingActionButton) findViewById(R.id.ok);
        sum.setOnClickListener(new View.OnClickListener() {
            class OnClickListener implements DialogInterface.OnClickListener {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }

            @Override
            public void onClick(final View view) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                builder.setTitle("Total Cost");

                final DBHandler db = new DBHandler(view.getContext());
                List<Product> products = db.getAllProducts();
                Integer t_price = 0;
                Integer bottles = 0;
                for (Product p : products) {
                    t_price += Integer.valueOf(p.getQuantity()) * Integer.valueOf(p.getPrice());
                    bottles += p.getQuantity();
                }

                builder.setMessage(bottles + " Bottles" + "    " + " KES " + NumberFormat.getIntegerInstance().format(t_price));

                // add the buttons
//                builder.setPositiveButton("Continue", null);
                builder.setNegativeButton("Wait!!", null);

                builder.setPositiveButton("Good?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DBHandler db = new DBHandler(view.getContext());
                        List<Product> products = db.getAllProducts();
                        Integer t_price = 0;
                        Integer bottles = 0;
                        for (Product p : products) {
                            t_price += Integer.valueOf(p.getQuantity()) * Integer.valueOf(p.getPrice());
                            bottles += p.getQuantity();
                        }

                        if (t_price > 999.99) {
                            Toast.makeText(view.getContext(), "Searching for delivery address ...", Toast.LENGTH_LONG).show();
                            startPlacePickerActivity();
                        } else {
                            Toast.makeText(view.getContext(), "At least make a total purchase of KES 1,000 or more", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                //Toast.makeText(view.getContext(), " Floating button", Toast.LENGTH_SHORT).show();

            }
        });
        //Floating Button

    }


    //Start Place picker

    private void startPlacePickerActivity() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent;
        try {
            intent = builder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                CharSequence p_address = place.getAddress();
                String p_id = place.getId();
                CharSequence p_name = place.getName();
                LatLng p_latlng = place.getLatLng();
                CharSequence contact = "";
                CharSequence house_no = "";
                String toastMsg = String.format("Place: %s", p_address);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                if (p_address != null) {

                    createNewAddress(p_address, p_id, p_name, p_latlng, contact, house_no);

                    Intent intent = new Intent(BasketActivity.this, FinishActivity.class);
                    BasketActivity.this.startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(this, "place not found, try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    //End place picker

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BasketActivity.this, MainActivity.class);
        BasketActivity.this.startActivity(intent);
        finish();
    }

    public void createNewAddress(final CharSequence place_address, final String p_id,
                                 final CharSequence p_name, final LatLng p_latlng,
                                 final CharSequence contact, final CharSequence house_no) {

        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://m-shopping.herokuapp.com/api/address/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject objAddress = null;
                        try {
                            objAddress = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String resAddressId    = null;
                        try {
                            resAddressId    = objAddress.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String newAddressId = resAddressId;
                        Toast.makeText(BasketActivity.this, newAddressId, Toast.LENGTH_LONG).show();
                        createNewOrder(newAddressId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
//                        Toast.makeText(BasketActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {         // Adding parameters
                Map<String, String> params = new HashMap<String, String>();

                params.put("place_address", String.valueOf(place_address));
                params.put("place_id", p_id);
                params.put("name", String.valueOf(p_name));
                params.put("latlng", String.valueOf(p_latlng));
                params.put("contact", String.valueOf(contact));
                params.put("house_no", String.valueOf(house_no));

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "admin").getBytes(), Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void createNewOrder(final String newAddressId) {

        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://m-shopping.herokuapp.com/api/orders/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject orderObj = null;
                        try {
                            orderObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String resOrderId = null;
                        try {
                            resOrderId = orderObj.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String newOrderId = resOrderId;
                        Toast.makeText(BasketActivity.this, newOrderId, Toast.LENGTH_LONG).show();
                        getNewOrderItemDetails(newOrderId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
//                        Toast.makeText(BasketActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {         // Adding parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("address", newAddressId);
                params.put("fee", "0");
                params.put("status", "ACTIVE");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "admin").getBytes(), Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getNewOrderItemDetails(final String newOrderId) {

        final DBHandler db = new DBHandler(this.getApplication());
        List<Product> products = db.getAllProducts();
        for (Product p : products) {
            String productId = p.getIdremote();
            String orderId = newOrderId;
            String quantity = String.valueOf(p.getQuantity());
            String price = String.valueOf(p.getPrice());

            createNewOrderItem(productId, orderId, quantity, price);

            }

    }

    public void createNewOrderItem(final String productId, final String orderId, final String quantity, final String price ) {

        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://m-shopping.herokuapp.com/api/order_items/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(BasketActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error));
//                        Toast.makeText(BasketActivity.this, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {         // Adding parameters
                Map<String, String> params = new HashMap<String, String>();

                params.put("product", productId);
                params.put("order", orderId);
                params.put("quantity", quantity);
                params.put("price", price);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "admin", "admin").getBytes(), Base64.DEFAULT)));
                return params;
            }
        };
        queue.add(postRequest);
    }
}
