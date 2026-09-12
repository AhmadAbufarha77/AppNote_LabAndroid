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

public class SortedFragment extends Fragment {

    public SortedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorted, container, false);

        RadioGroup group = view.findViewById(R.id.radioSortGroup);
        RadioButton date = view.findViewById(R.id.radioCreationDate);
        RadioButton alphabetical = view.findViewById(R.id.radioAlphabetically);

        SharedPreferences preferences = requireActivity().getSharedPreferences("NoteAppPreferences", Context.MODE_PRIVATE);
        String savedSort = preferences.getString("sort_order", "date");

        if (savedSort.equals("alphabetical")) {
            alphabetical.setChecked(true);
        } else {
            date.setChecked(true);
        }

        group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            SharedPreferences.Editor editor = preferences.edit();
            if (checkedId == R.id.radioAlphabetically) {
                editor.putString("sort_order", "alphabetical");
            } else {
                editor.putString("sort_order", "date");
            }
            editor.apply();
        });

        return view;
    }
}
