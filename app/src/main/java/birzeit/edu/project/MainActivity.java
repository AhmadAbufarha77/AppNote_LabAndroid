package birzeit.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(
                this,
                "NoteApp.db",
                null,
                1
        );

        TextInputLayout tilEmail =
                (TextInputLayout) findViewById(R.id.tilEmail);

        TextInputEditText etEmail =
                (TextInputEditText) findViewById(R.id.edEmail);

        TextInputLayout tilPassword =
                (TextInputLayout) findViewById(R.id.tilPassword);

        TextInputEditText etPassword =
                (TextInputEditText) findViewById(R.id.etPassword);

        Button btnSignIn =
                (Button) findViewById(R.id.btnSignIn);

        CheckBox cbRememberMe =
                (CheckBox) findViewById(R.id.cbRememberMe);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                boolean valid = true;

                if (email.isEmpty()) {
                    tilEmail.setError("Email is required");
                    valid = false;
                }

                if (password.isEmpty()) {
                    tilPassword.setError("Password is required");
                    valid = false;
                }

                if (!databaseHelper.checkLogin(email, password)) {
                    tilEmail.setError("Invalid email or password");
                    tilPassword.setError("Invalid email or password");
                    valid = false;
                }

                if (!valid) {
                    return;
                }

                Intent intent =
                        new Intent(MainActivity.this, HomeActivity.class);

                startActivity(intent);
            }
        });

        clearErrorOnTyping(etEmail, tilEmail);
        clearErrorOnTyping(etPassword, tilPassword);
    }

    private void clearErrorOnTyping(
            TextInputEditText editText,
            TextInputLayout layout
    ) {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}