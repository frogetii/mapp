package ke.co.mapp.mapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("M shopping product/service");
        getSupportActionBar().setSubtitle("Terms and Conditions");


        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://docs.google.com/document/d/1BmQaLCoauwyFkfCMxFsirrIeYMHHMNY0KquMl0qDWY4/edit?usp=sharing");
    }
}
