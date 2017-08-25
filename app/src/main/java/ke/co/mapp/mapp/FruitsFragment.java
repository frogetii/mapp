package ke.co.mapp.mapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;


public class FruitsFragment extends Fragment
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{
    SliderLayout sliderLayout ;

    HashMap<String, String> HashMapForURL ;

    HashMap<String, Integer> HashMapForLocalRes ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the single_item for this fragment
        View view = inflater.inflate(R.layout.fragment_fruits, container, false);

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        // Get ListView object from xml
        final ListView listView = (ListView) view.findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[]{"KSH 100",
                "KSH 250",
                "KSH 240",
                "KSH 70",
                "KSH 95",
                "KSH 200",
                "KSH 230",
                "KSH 300",
                "KSH 140",
                "KSH 90",
                "KSH 400",
        };


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.single_item, R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listeneritems
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getActivity(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

        //Call this method if you want to add images from URL .
        AddImagesUrlOnline();

        //Call this method to add images from local drawable folder .
        //AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for (String name : HashMapForURL.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getActivity());

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
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

        sliderLayout.addOnPageChangeListener(FruitsFragment.this);

        return view;
    }

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

        Log.d("Slider ", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put("Tomatoes", "https://porcelainfacespa.com/blog/wp-content/uploads/2014/04/Nature___Plants____Juicy_tomatoes_064798_.jpg");
        HashMapForURL.put("Night Shades", "http://www.ucdintegrativemedicine.com/wp-content/uploads/July-7_Solenaceas_Illustrate6_Large3.png");
        HashMapForURL.put("Strawberry", "http://www.medicalnewstoday.com/content/images/articles/271/271285/three-strawberries.jpg");
        HashMapForURL.put("Onions", "http://www.jiva.org/wp-content/uploads/2015/06/onion.jpg");
        HashMapForURL.put("Apple", "http://www.sodea-fruits.com/MTDsodea-fruit/gallery/slide-index/slide2.png");
    }

    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("CupCake", R.drawable.userimg);
        HashMapForLocalRes.put("Donut", R.drawable.logo2b);
        HashMapForLocalRes.put("Eclair", R.drawable.userimg);
        HashMapForLocalRes.put("Froyo", R.drawable.logo2b);
        HashMapForLocalRes.put("GingerBread", R.drawable.userimg);
    }
}