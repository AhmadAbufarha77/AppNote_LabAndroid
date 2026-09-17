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

public class favorites extends Fragment {

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private String currentUserEmail;

    public favorites() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_favorites,
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
                SharedPrefManager.getInstance(requireContext());

        currentUserEmail =
                sharedPrefManager.readString(
                        "currentUserEmail",
                        ""
                );

        RecyclerView recyclerView =
                view.findViewById(
                        R.id.recyclerFavorites
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        ArrayList<AddNote> notes =
                databaseHelper.getFavoriteNotes(
                        currentUserEmail
                );

        adapter = new NoteAdapter(
                notes,
                new NoteAdapter.OnNoteClickListener() {

                    @Override
                    public void onNoteClick(AddNote note) {

                        HomeActivity homeActivity =
                                (HomeActivity) requireActivity();

                        homeActivity.openNoteDetails(note);
                    }

                    @Override
                    public void onFavoriteClick(
                            AddNote note,
                            int position
                    ) {

                        boolean newFavorite =
                                !note.isFavorite();

                        note.setFavorite(newFavorite);

                        databaseHelper.updateFavorite(
                                note.getId(),
                                newFavorite
                        );

                        adapter.setNotes(
                                databaseHelper.getFavoriteNotes(
                                        currentUserEmail
                                )
                        );
                    }
                }
        );

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab =
                view.findViewById(
                        R.id.fabAddFavoriteNote
                );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                adapter != null) {

            adapter.setNotes(
                    databaseHelper.getFavoriteNotes(
                            currentUserEmail
                    )
            );
        }
    }
}