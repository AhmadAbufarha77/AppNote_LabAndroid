package birzeit.edu.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseHelper = new DatabaseHelper(
                this,
                "NoteApp.db",
                null,
                1
        );
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvSignIn = findViewById(R.id.tvToSignIn);

        btnBack.setOnClickListener(v -> finish());
        tvSignIn.setOnClickListener(v -> finish());
        //id
        TextInputLayout tilEmail = (TextInputLayout) findViewById(R.id.tilSignUpEmail);
        TextInputEditText etEmail =(TextInputEditText) findViewById(R.id.etSignUpEmail);

        TextInputLayout tilFirstName = (TextInputLayout) findViewById(R.id.tilSignUpFirstName);
        TextInputEditText etFirstName =(TextInputEditText) findViewById(R.id.etSignUpFirstName);

        TextInputLayout tilLastName = (TextInputLayout) findViewById(R.id.tilSignUpLastName);
        TextInputEditText etLastName =(TextInputEditText) findViewById(R.id.etSignUpLastName);

        TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.tilSignUpPassword);
        TextInputEditText etPassword =(TextInputEditText) findViewById(R.id.etSignUpPassword);

        TextInputLayout tilConfirmPassword = (TextInputLayout) findViewById(R.id.tilConfirmPassword);
        TextInputEditText etConfirmPassword =(TextInputEditText) findViewById(R.id.etConfirmPassword);

        Button btnCreatAcc = (Button) findViewById(R.id.btnCreatAccount);

        clearErrorOnTyping(etEmail, tilEmail);
        clearErrorOnTyping(etFirstName, tilFirstName);
        clearErrorOnTyping(etLastName, tilLastName);
        clearErrorOnTyping(etPassword, tilPassword);
        clearErrorOnTyping(etConfirmPassword, tilConfirmPassword);

        btnCreatAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                if(!isValidEmail(etEmail.getText().toString())){
                    tilEmail.setError("Invalid email");
                    valid = false;
                }
                else if(databaseHelper.emailExists(etEmail.getText().toString())){
                    tilEmail.setError("Email already registered");
                    valid = false;
                }
                if (!isValidName(etFirstName.getText().toString())) {
                    tilFirstName.setError("First name must be 3 to 10 characters");
                    valid = false;
                }
                if (!isValidName(etLastName.getText().toString())) {
                    tilLastName.setError("Last name must be 3 to 10 characters");
                    valid = false;
                }
                if (!isValidPassword(etPassword.getText().toString())) {
                    tilPassword.setError("Password must be 6-12 characters and contain uppercase, lowercase and number");
                    valid = false;
                }
                if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                    tilConfirmPassword.setError("Passwords do not match");
                    valid = false;
                }

                if(!valid){return;}

                User user = new User();
                user.setEmail(etEmail.getText().toString());
                user.setFirstName(etFirstName.getText().toString());
                user.setLastName(etLastName.getText().toString());
                user.setPassword(etPassword.getText().toString());
                databaseHelper.insertUser(user);

                Toast.makeText(
                        SignUpActivity.this,
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });


    }

    private boolean isValidEmail(String email) {
            return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidName(String name){
        return name.length() >= 3 && name.length() <= 10;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6 || password.length() > 12) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {

            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }

            if (Character.isLowerCase(c)) {
                hasLower = true;
            }

            if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        return hasUpper && hasLower && hasNumber;
    }

    private void clearErrorOnTyping(TextInputEditText editText, TextInputLayout layout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
