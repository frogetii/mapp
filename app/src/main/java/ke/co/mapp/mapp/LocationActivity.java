package ke.co.mapp.mapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class LocationActivity extends AppCompatActivity {

    private TextView tv_get_place;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_get_place = (TextView) findViewById(R.id.get_place);
        tv_get_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlacePickerActivity();
            }

        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//
//                Intent intent = new Intent(LocationActivity.this, FruitsActivity.class);
//                LocationActivity.this.startActivity(intent);
//                finish();

                Intent intent2 = new Intent(LocationActivity.this, DrinksActivity.class);
                LocationActivity.this.startActivity(intent2);
                finish();
            }
        });
    }

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
            if (requestCode == RESULT_OK) {
                displaySelectedData(data);
            }
        }
    }
    private void displaySelectedData(Intent data) {

        Place place = PlacePicker.getPlace(data, this);
        String thisaddress = place.getName().toString();
        TextView tv_address = (TextView) findViewById(R.id.address);
        tv_address.setText(thisaddress);
        Log.d("Data", thisaddress);
    }
    }


