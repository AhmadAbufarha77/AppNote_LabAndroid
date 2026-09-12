package birzeit.edu.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofileactivity);

        Toolbar toolbar = findViewById(R.id.toolbarEditProfile);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
