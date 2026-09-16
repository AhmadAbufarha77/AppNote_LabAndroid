package birzeit.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class allnote extends Fragment {

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;

    private Spinner spinnerTagFilter;

    private String currentUserEmail;
    private String selectedTag = "All Tags";

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


        SharedPrefManager sharedPrefManager =
                SharedPrefManager.getInstance(
                        requireContext()
                );

        currentUserEmail =
                sharedPrefManager.readString(
                        "currentUserEmail",
                        ""
                );


        RecyclerView recyclerView =
                view.findViewById(
                        R.id.recyclerAllNotes
                );

        spinnerTagFilter =
                view.findViewById(
                        R.id.spinnerTagFilter
                );


        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );


        ArrayList<AddNote> notes =
                databaseHelper.getAllNotes(
                        currentUserEmail
                );


        adapter = new NoteAdapter(
                notes,
                new NoteAdapter.OnNoteClickListener() {

                    @Override
                    public void onNoteClick(
                            AddNote note
                    ) {

                        HomeActivity homeActivity =
                                (HomeActivity) requireActivity();

                        homeActivity.openNoteDetails(
                                note
                        );
                    }


                    @Override
                    public void onFavoriteClick(
                            AddNote note,
                            int position
                    ) {

                        boolean newFavorite =
                                !note.isFavorite();

                        note.setFavorite(
                                newFavorite
                        );

                        databaseHelper.updateFavorite(
                                note.getId(),
                                newFavorite
                        );

                        adapter.notifyItemChanged(
                                position
                        );
                    }
                }
        );


        recyclerView.setAdapter(
                adapter
        );


        loadTags();


        spinnerTagFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        selectedTag =
                                parent.getItemAtPosition(
                                        position
                                ).toString();

                        loadNotes();
                    }


                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );


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


    private void loadTags() {

        ArrayList<String> tags =
                databaseHelper.getUserTags(
                        currentUserEmail
                );


        ArrayAdapter<String> tagAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        tags
                );


        tagAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerTagFilter.setAdapter(
                tagAdapter
        );
    }


    private void loadNotes() {

        if (selectedTag.equals("All Tags")) {

            adapter.setNotes(
                    databaseHelper.getAllNotes(
                            currentUserEmail
                    )
            );

        } else {

            adapter.setNotes(
                    databaseHelper.getNotesByTag(
                            currentUserEmail,
                            selectedTag
                    )
            );
        }
    }


    @Override
    public void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                adapter != null &&
                spinnerTagFilter != null) {

            loadTags();
            loadNotes();
        }
    }
}