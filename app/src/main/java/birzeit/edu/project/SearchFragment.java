package birzeit.edu.project;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;

    private EditText etSearch;

    private String currentUserEmail;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_search,
                container,
                false
        );


        etSearch =
                view.findViewById(
                        R.id.etSearch
                );

        RecyclerView recyclerView =
                view.findViewById(
                        R.id.recyclerSearch
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


        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );


        ArrayList<AddNote> notes =
                new ArrayList<>();


        adapter = new NoteAdapter(
                notes,
                new NoteAdapter.OnNoteClickListener() {

                    @Override
                    public void onNoteClick(AddNote note) {

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


        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }


                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        String searchText =
                                s.toString().trim();


                        if (searchText.isEmpty()) {

                            adapter.setNotes(
                                    new ArrayList<>()
                            );

                        } else {

                            adapter.setNotes(
                                    databaseHelper.searchNotes(
                                            currentUserEmail,
                                            searchText
                                    )
                            );
                        }
                    }


                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );


        return view;
    }


    @Override
    public void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                adapter != null &&
                etSearch != null) {

            String searchText =
                    etSearch.getText()
                            .toString()
                            .trim();

            if (!searchText.isEmpty()) {

                adapter.setNotes(
                        databaseHelper.searchNotes(
                                currentUserEmail,
                                searchText
                        )
                );
            }
        }
    }
}