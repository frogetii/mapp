package ke.co.mapp.mapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinksFragment extends Fragment
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{
    SliderLayout sliderLayout ;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    HashMap<String, String> HashMapForURL ;
    HashMap<String, Integer> HashMapForLocalRes ;
    HashMap<String, String> HashMapForMappWeb;

    JSONArray resultsCat = null;

    String JsonURLProd = "http://m-shopping.herokuapp.com/products/?format=json";
    String JsonURLCat = "http://m-shopping.herokuapp.com/categories/?format=json";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the single_item for this fragment */
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);
        Context context = inflater.getContext();



        listView = (ExpandableListView) view.findViewById(R.id.lvExp);
        getCategories();
        initData();
        listAdapter = new ExpandableListAdapter(this.getActivity(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        //Call this method if you want to add images from URL .
        AddImagesUrlMappWeb();

        //Call this method to add images from local drawable folder .
        //AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for (String name : HashMapForMappWeb.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getActivity());

            textSliderView
                    .description(name)
                    .image(HashMapForMappWeb.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra", name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(DrinksFragment.this);

        return view;
    }
    //
    private void initData1(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("WHISKY");

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("Canadian");
        edmtDev.add("Manhattan");
        edmtDev.add("Blended");
        edmtDev.add("Bourbon");
        listHash.put(listDataHeader.get(0),edmtDev);
    }

    //

    public void getCategories(){
        RequestQueue requestQueueCat = Volley.newRequestQueue(getContext());
        JsonObjectRequest reqCat = new JsonObjectRequest(JsonURLCat, null ,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            resultsCat = response.getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Something went wrong, check your internet connection", Toast.LENGTH_SHORT).show();
            }

        }
        );
        requestQueueCat.add(reqCat);
    }

    private void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(JsonURLProd, null,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {

                        for (int c = 0; c < resultsCat.length(); c++) {
                            JSONObject jsonObjectCat = resultsCat.getJSONObject(c);
                            String urlcat = jsonObjectCat.getString("url");
                            listDataHeader.add(String.valueOf(jsonObjectCat));
                            //
                            List<String> myres = new ArrayList<>(100);

                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject jsonObject = results.getJSONObject(i);
                                String urlprod = jsonObject.getString("category");
                                if (urlprod.equals(urlcat)) {
                                    myres.add(String.valueOf(jsonObject));
                                }
                                listHash.put(listDataHeader.get(c), myres);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(getContext(), "Something went wrong, try again later" , Toast.LENGTH_SHORT).show();
            }
        }

        );
        requestQueue.add(req);
    }

    //

    @Override
    public void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

       Toast.makeText(getActivity(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put("Jack Daniels", "http://wallpaperpawn.us/wp-content/uploads/2016/07/alcoholic-drinks-in-bottles-alcohol-cocktail-drinks-hd-pictures-hd-wallpapers.jpg");
        HashMapForURL.put("Vana Tallinn", "http://alcogolizm.com/wp-content/uploads/2016/05/ffee8209870146f6aea02105eb0d65d4-1.jpg");
        HashMapForURL.put("Tatti's Tequila", "http://4.bp.blogspot.com/--UpMh_KXh34/UhrZeaU_i6I/AAAAAAAAQIw/sST3gBVfhJQ/s1600/Custom+Etched+Liquor+Bottles.jpg");
        HashMapForURL.put("Irish Mist", "http://wallpapercave.com/wp/wp1835931.jpg");
        HashMapForURL.put("Jennese Whiskey", "http://justsomething.co/wp-content/uploads/2014/08/honest-drinks-labels-6.jpg");
    }

    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("CupCake", R.drawable.userimg);
        HashMapForLocalRes.put("Donut", R.drawable.logo2b);
        HashMapForLocalRes.put("Eclair", R.drawable.userimg);
        HashMapForLocalRes.put("Froyo", R.drawable.logo2b);
        HashMapForLocalRes.put("GingerBread", R.drawable.userimg);

    }

    public void AddImagesUrlMappWeb() {

        HashMapForMappWeb = new HashMap<String, String>();
        HashMapForMappWeb.put("Jack Daniels", "http://wallpaperpawn.us/wp-content/uploads/2016/07/alcoholic-drinks-in-bottles-alcohol-cocktail-drinks-hd-pictures-hd-wallpapers.jpg");
        HashMapForMappWeb.put("Tatti's Tequila", "http://4.bp.blogspot.com/--UpMh_KXh34/UhrZeaU_i6I/AAAAAAAAQIw/sST3gBVfhJQ/s1600/Custom+Etched+Liquor+Bottles.jpg");
        HashMapForMappWeb.put("Irish Mist", "http://wallpapercave.com/wp/wp1835931.jpg");

    }

}