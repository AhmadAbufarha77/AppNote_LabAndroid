package birzeit.edu.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnoteactivity);

        Toolbar toolbar = findViewById(R.id.toolbarAddNote);

        EditText etTitle = findViewById(R.id.etNoteTitle);
        EditText etContent = findViewById(R.id.etNoteContent);
        EditText etTag = findViewById(R.id.etTag);

        TextView tvCreationDate =
                findViewById(R.id.tvCreationDate);

        Button btnSaveNote =
                findViewById(R.id.btnSaveNote);

        toolbar.setNavigationOnClickListener(v -> finish());

        databaseHelper = new DatabaseHelper(
                this,
                "NoteApp.db",
                null,
                2
        );

        String currentDate =
                new SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                ).format(new Date());

        tvCreationDate.setText(
                "Creation date: " + currentDate
        );

        btnSaveNote.setOnClickListener(v -> {

            String title =
                    etTitle.getText().toString().trim();

            String content =
                    etContent.getText().toString().trim();

            String tag =
                    etTag.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                return;
            }

            if (content.isEmpty()) {
                etContent.setError("Content is required");
                return;
            }

            AddNote note = new AddNote(
                    0,
                    title,
                    content,
                    tag,
                    currentDate,
                    false
            );

            long result =
                    databaseHelper.insertNote(note);

            if (result != -1) {

                Toast.makeText(
                        AddNoteActivity.this,
                        "Note saved successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        AddNoteActivity.this,
                        "Failed to save note",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}