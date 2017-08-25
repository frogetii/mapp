package ke.co.mapp.mapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText etname = (EditText) findViewById(R.id.et_name) ;
        final EditText etemail = (EditText) findViewById(R.id.et_email);
        final EditText etphone = (EditText) findViewById(R.id.et_phone);
        final EditText etpass = (EditText) findViewById(R.id.et_password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etname.getText().toString().equals("")){
                    etname.setError("can't be blank");
                    return;
                }
                if(etemail.getText().toString().equals("")){
                    etemail.setError("can't be blank");
                    return;
                }
                if(etphone.getText().toString().equals("")){
                    etphone.setError("can't be blank");
                    return;
                }
                if(etpass.getText().toString().equals("")){
                    etpass.setError("can't be blank");
                    return;
                }

                Snackbar.make(view, "Registration details ok", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
