package ke.co.mapp.mapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinksActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);


        //      Start next floating bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrinksActivity.this, VegetablesActivity.class);
                DrinksActivity.this.startActivity(intent);
                finish();
            }
        });
//      End next floating bar

    }
    private void initData(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("WHISKY");
        listDataHeader.add("WINE");
        listDataHeader.add("LIQUER");
        listDataHeader.add("VODKA");

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("Canadian");
        edmtDev.add("Manhattan");
        edmtDev.add("Blended");
        edmtDev.add("Bourbon");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("Red Wine");
        androidStudio.add("Zinfandel");
        androidStudio.add("Sangiovese");
        androidStudio.add("Barbera");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Benedictine");
        xamarin.add("Chartreuse");
        xamarin.add("Curacao");
        xamarin.add("Anisette");

        List<String> uwp = new ArrayList<>();
        uwp.add("Absolut");
        uwp.add("Stanley");
        uwp.add("Chopin");
        uwp.add("Luksusowa");

        listHash.put(listDataHeader.get(0),edmtDev);
        listHash.put(listDataHeader.get(1),androidStudio);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),uwp);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DrinksActivity.this, MainActivity.class);
        DrinksActivity.this.startActivity(intent);
        finish();
    }

}

