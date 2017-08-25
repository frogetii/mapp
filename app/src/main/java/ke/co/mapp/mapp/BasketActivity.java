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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
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

                builder.setMessage(bottles + " Bottles"+ "    " + " KES " + NumberFormat.getIntegerInstance().format(t_price));

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
                        }
                        else {
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
                CharSequence addr = place.getAddress();
                String toastMsg = String.format("Place: %s", addr);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                if (addr != null) {
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


    public void createNewOrder(){

    }



}
