package birzeit.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class favorites extends Fragment {

    public favorites() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fabAddFavoriteNote);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddNoteActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
