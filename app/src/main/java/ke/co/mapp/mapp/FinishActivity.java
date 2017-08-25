package ke.co.mapp.mapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("M SHOPPING");
//        toolbar.setSubtitle("Your number one party partner");

        //start bottom navigation

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.finish_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_call:
                                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0790331936"));
                                startActivity(i);
                                break;
                            case R.id.action_exit:
                                Toast.makeText(FinishActivity.this, "Sit back, relax and wait for your delivery.", Toast.LENGTH_LONG).show();
                                finish();
                                break;
                        }
                        return false;
                    }
                });

        //end bottom navigation

    }
    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Sit back, relax and wait for your delivery.", Toast.LENGTH_LONG).show();
        finish();

//        Intent intent = new Intent(FinishActivity.this, BasketActivity.class);
//        FinishActivity.this.startActivity(intent);
//        finish();
//        Toast.makeText(this, "", Toast.LENGTH_LONG).show();

    }

}
