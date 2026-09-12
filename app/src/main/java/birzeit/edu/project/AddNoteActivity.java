package birzeit.edu.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnoteactivity);

        Toolbar toolbar = findViewById(R.id.toolbarAddNote);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
