package ke.co.mapp.mapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


import org.w3c.dom.Text;

import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // User Session Manager Class
    UserSessionManager session;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        //Tabs code
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }
           @Override
           public void onTabReselected(TabLayout.Tab tab){
               viewPager.setCurrentItem(tab.getPosition());
           }
        });
        //Tabs code

        // Session class instance
        session = new UserSessionManager(getApplicationContext());

//        if(session.checkLogin())
//            finish();


        //start bottom navigation

//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.bottom_navigation);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action:
//                                Intent i = new Intent(MainActivity.this, DrinksActivity.class);
//                                MainActivity.this.startActivity(i);
//                                finish();
//                                break;
//                            case R.id.action_:
//
//                                break;
//                            case R.id.action_shop:
//                                Intent intent = new Intent(MainActivity.this, DrinksActivity.class);
//                                MainActivity.this.startActivity(intent);
//                                finish();
//                                break;
//                        }
//                        return false;
//                    }
//                });

        //end bottom navigation

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //      Start next floating bar
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.done);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
//                MainActivity.this.startActivity(intent);
//                finish();
//            }
//        });
//      End next floating bar
        FloatingActionButton location = (FloatingActionButton) findViewById(R.id.basket);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DBHandler db = new DBHandler(view.getContext());
                List<Product> products = db.getAllProducts();
                Integer t_price = 0;
                Integer bottles = 0;
                for (Product p : products) {
                    t_price += Integer.valueOf(p.getQuantity()) * Integer.valueOf(p.getPrice());
                    bottles += p.getQuantity();
                }

                if (bottles > 0) {
                    Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                    MainActivity.this.startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(view.getContext(), "Atleast select 1 or more drinks to proceed", Toast.LENGTH_LONG).show();
                }


            }
        });

//        FloatingActionButton trytivity= (FloatingActionButton) findViewById(R.id.trytivity);
//        trytivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent tryIntent = new Intent(MainActivity.this, TryActivity.class);
//                MainActivity.this.startActivity(tryIntent);
//            }
//        });
    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments [] = {"Drinks"}; //, "Vegetables"
        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return  new DrinksFragment();
//                case 1:
//                    return new FruitsFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount(){
            return fragments.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(UserSessionManager.KEY_NAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        String phone = user.get(UserSessionManager.KEY_PHONE);

        TextView tvname = (TextView) findViewById(R.id.tv_name);
        if (tvname !=null){
            tvname.setText(name);
        }
        TextView tvemail = (TextView) findViewById(R.id.tv_email);
        if (tvemail != null){
            tvemail.setText(email);
        }
        TextView tvphone = (TextView) findViewById(R.id.tv_phone);
        if (tvphone !=null){
            tvphone.setText(phone);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } if (id == R.id.action_logout){
            session.logoutUser();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.payment) {

            Toast.makeText(this, "cash on delivery", Toast.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.orders) {

            Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.twitter) {

            Intent intent = null;
            try {
                // get the Twitter app if possible
                this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=881619281452072960"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mshoppingke"));
            }
            this.startActivity(intent);

        } else if (id == R.id.facebook) {

            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);

        } else if (id == R.id.settings) {

            Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.terms) {


            Intent intent = new Intent(this,TermsActivity.class);
            this.startActivity(intent);
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static String FACEBOOK_URL = "https://www.facebook.com/mshopingKE";
    public static String FACEBOOK_PAGE_ID = "664521996980501";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

}
