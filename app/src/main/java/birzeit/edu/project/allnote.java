package birzeit.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class allnote extends Fragment {

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;

    public allnote() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_allnote,
                container,
                false
        );

        databaseHelper = new DatabaseHelper(
                requireContext(),
                "NoteApp.db",
                null,
                3
        );

        RecyclerView recyclerView =
                view.findViewById(
                        R.id.recyclerAllNotes
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());

        String currentUserEmail = sharedPrefManager.readString("currentUserEmail", "");
        ArrayList<AddNote> notes =
                databaseHelper.getAllNotes(currentUserEmail);

        adapter = new NoteAdapter(
                notes,
                note -> {

                    HomeActivity homeActivity =
                            (HomeActivity) requireActivity();

                    homeActivity.openNoteDetails(note);
                }
        );

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab =
                view.findViewById(
                        R.id.fabAddNote
                );

        fab.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            requireContext(),
                            AddNoteActivity.class
                    );

            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (databaseHelper != null &&
                adapter != null) {
            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());

            String currentUserEmail = sharedPrefManager.readString("currentUserEmail", "");
            adapter.setNotes(
                    databaseHelper.getAllNotes(currentUserEmail)
            );
        }
    }
}