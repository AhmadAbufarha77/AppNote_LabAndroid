package birzeit.edu.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SortedFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;

    private String currentUserEmail;

    public SortedFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_sorted,
                container,
                false
        );

        RadioGroup group =
                view.findViewById(R.id.radioSortGroup);

        RadioButton date =
                view.findViewById(R.id.radioCreationDate);

        RadioButton alphabetical =
                view.findViewById(R.id.radioAlphabetically);

        RecyclerView recyclerView =
                view.findViewById(R.id.recyclerSorted);


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


        SharedPreferences preferences =
                requireActivity().getSharedPreferences(
                        "NoteAppPreferences",
                        Context.MODE_PRIVATE
                );

        String savedSort =
                preferences.getString(
                        "sort_order",
                        "date"
                );


        if (savedSort.equals("alphabetical")) {

            alphabetical.setChecked(true);

        } else {

            date.setChecked(true);
        }


        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );


        ArrayList<AddNote> notes =
                databaseHelper.getSortedNotes(
                        currentUserEmail,
                        savedSort
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

                        adapter.notifyItemChanged(position);
                    }
                }
        );


        recyclerView.setAdapter(adapter);


        group.setOnCheckedChangeListener(
                (radioGroup, checkedId) -> {

                    SharedPreferences.Editor editor =
                            preferences.edit();

                    String sortOrder;

                    if (checkedId == R.id.radioAlphabetically) {

                        sortOrder = "alphabetical";

                    } else {

                        sortOrder = "date";
                    }

                    editor.putString(
                            "sort_order",
                            sortOrder
                    );

                    editor.apply();


                    adapter.setNotes(
                            databaseHelper.getSortedNotes(
                                    currentUserEmail,
                                    sortOrder
                            )
                    );
                }
        );


        return view;
    }
}