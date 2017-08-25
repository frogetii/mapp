package ke.co.mapp.mapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    // User Session Manager Class
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // User Session Manager
        session = new UserSessionManager(getApplicationContext());


        final EditText etemail = (EditText) findViewById(R.id.et_email);
        final EditText etpassword = (EditText) findViewById(R.id.et_password);

        final Button blogin = (Button) findViewById(R.id.login);
        final TextView registerLink = (TextView) findViewById(R.id.register);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = "Joshua Maina";
                final String email = etemail.getText().toString();
                final String phone = "+254790331936";
                final String password = etpassword.getText().toString();

                if (etemail.getText().toString().equals("")) {
                    etemail.setError("Please fill in the email");
                    return;
                }

                if (etpassword.getText().toString().equals("")) {
                    etpassword.setError("A password is required");
                    return;
                }

                else
                {
                    session.createUserLoginSession(name,
                            email, phone);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                }


            }
        });
    }
}
