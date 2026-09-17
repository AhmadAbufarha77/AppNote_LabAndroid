package birzeit.edu.project;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
public class EditNoteActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnoteactivity);
        Toolbar toolbar = findViewById(R.id.toolbarAddNote);
        EditText etTitle = findViewById(R.id.etNoteTitle);
        EditText etContent = findViewById(R.id.etNoteContent);
        EditText etTag = findViewById(R.id.etTag);
        TextView tvCreationDate = findViewById(R.id.tvCreationDate);
        Button btnSaveNote = findViewById(R.id.btnSaveNote);
        toolbar.setTitle("Edit Note");
        btnSaveNote.setText("Save Changes");
        toolbar.setNavigationOnClickListener(v -> finish());
        databaseHelper = new DatabaseHelper(
                this,
                "NoteApp.db",
                null,
                3
        );
        int noteId = getIntent().getIntExtra("noteId", -1);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String tag = getIntent().getStringExtra("tag");
        String date = getIntent().getStringExtra("date");
        etTitle.setText(title);
        etContent.setText(content);
        etTag.setText(tag);
        tvCreationDate.setText("Creation date: " + date);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = etTitle.getText().toString().trim();
                String newContent = etContent.getText().toString().trim();
                String newTag = etTag.getText().toString().trim();
                if (newTitle.isEmpty()) {
                    etTitle.setError("Title is required");
                    return;
                }
                if (newContent.isEmpty()) {
                    etContent.setError("Content is required");
                    return;
                }
                int result = databaseHelper.updateNote(noteId, newTitle, newContent, newTag);
                if (result > 0) {
                    Toast.makeText(
                            EditNoteActivity.this,
                            "Note updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                } else {
                    Toast.makeText(
                            EditNoteActivity.this,
                            "Failed to update note",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}