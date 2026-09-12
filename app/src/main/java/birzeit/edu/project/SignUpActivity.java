package birzeit.edu.project;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvSignIn = findViewById(R.id.tvToSignIn);

        btnBack.setOnClickListener(v -> finish());
        tvSignIn.setOnClickListener(v -> finish());
    }
}
