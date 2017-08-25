package ke.co.mapp.mapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TryActivity extends AppCompatActivity {

    String JsonURL = "http://m-shopping.herokuapp.com/products/?format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);

        final DBHandler db = new DBHandler(getApplicationContext());
        final TextView dataview = (TextView) findViewById(R.id.data);
        final TextView tvdispaly = (TextView) findViewById(R.id.textView7);
        final Button buttons = (Button) findViewById(R.id.button2);

        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest req2 = new JsonObjectRequest(JsonURL, null ,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        List<String> edmtDev5 = new ArrayList<>();

                        try {
                            JSONArray results2 = response.getJSONArray("results");
                            String names = "";
                            for(int i=0; i < results2.length(); i++){
                                JSONObject jsonObject = results2.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                names = names + name ;
                            dataview.setText(names);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                       }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "errorMessage:"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        );
        requestQueue3.add(req2);

        buttons.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {


                db.addProduct(new Product(11,"120","Tusker","250",50,"url"));

                List<Product> products = db.getAllProducts();
                String stproduct ="";
                for (Product p : products) {
                    stproduct += p.getId() + " " + p.getIdremote() + " " + " " + p.getName() + " ";
                tvdispaly.setText(stproduct);
//                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}

