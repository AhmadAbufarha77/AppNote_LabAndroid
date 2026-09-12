package birzeit.edu.project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofileactivity);

        Toolbar toolbar = findViewById(R.id.toolbarEditProfile);
        toolbar.setNavigationOnClickListener(v -> finish());

        sharedPrefManager = SharedPrefManager.getInstance(this);
        databaseHelper = new DatabaseHelper(
                this,
                "NoteApp.db",
                null,
                3
        );

        TextInputLayout tilFirstName = findViewById(R.id.tilEditFirstName);
        TextInputEditText etFirstName = findViewById(R.id.etEditFirstName);

        TextInputLayout tilLastName = findViewById(R.id.tilEditLastName);
        TextInputEditText etLastName = findViewById(R.id.etEditLastName);

        TextInputLayout tilPassword = findViewById(R.id.tilNewPassword);
        TextInputEditText etPassword = findViewById(R.id.etNewPassword);

        TextInputLayout tilConfirmPassword = findViewById(R.id.tilConfirmNewPassword);
        TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmNewPassword);

        Button btnSave = findViewById(R.id.btnSaveProfile);

        String currentEmail = sharedPrefManager.readString("currentUserEmail", "");
        User user = databaseHelper.getUserByEmail(currentEmail);
        if (user != null) {
            etFirstName.setText(user.getFirstName());
            etLastName.setText(user.getLastName());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (!isValidName(etFirstName.getText().toString())) {
                    tilFirstName.setError("First name must be 3 to 10 characters");
                    valid = false;
                }

                if (!isValidName(etLastName.getText().toString())) {
                    tilLastName.setError("Last name must be 3 to 10 characters");
                    valid = false;
                }

                if (!etPassword.getText().toString().isEmpty()) {

                    if (!isValidPassword(etPassword.getText().toString())) {
                        tilPassword.setError(
                                "Password must be 6-12 characters and contain uppercase, lowercase and number"
                        );
                        valid = false;
                    }

                    if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                        tilConfirmPassword.setError("Passwords do not match");
                        valid = false;
                    }
                }

                if (!valid) {
                    return;
                }

                User updatedUser = new User();

                updatedUser.setEmail(user.getEmail());
                updatedUser.setFirstName(etFirstName.getText().toString());
                updatedUser.setLastName(etLastName.getText().toString());

                if (etPassword.getText().toString().isEmpty()) {
                    updatedUser.setPassword(user.getPassword());
                } else {
                    updatedUser.setPassword(etPassword.getText().toString());
                }

                databaseHelper.updateUser(updatedUser);

                Toast.makeText(
                        EditProfileActivity.this,
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });

        clearErrorOnTyping(etFirstName,tilFirstName);
        clearErrorOnTyping(etLastName,tilLastName);
        clearErrorOnTyping(etPassword,tilPassword);
        clearErrorOnTyping(etConfirmPassword,tilConfirmPassword);



    }

    private boolean isValidEmail(String email) {
        return email.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }

    private boolean isValidName(String name) {
        return name.length() >= 3 &&
                name.length() <= 10;
    }

    private boolean isValidPassword(String password) {

        if (password.length() < 6 ||
                password.length() > 12) {
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

        return hasUpper &&
                hasLower &&
                hasNumber;
    }

    private void clearErrorOnTyping(
            TextInputEditText editText,
            TextInputLayout layout) {

        editText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        layout.setError(null);
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                });
    }
}
