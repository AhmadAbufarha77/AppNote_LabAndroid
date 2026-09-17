package birzeit.edu.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Profile extends Fragment {

    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;

    private SharedPrefManager sharedPrefManager;
    private DatabaseHelper databaseHelper;

    public Profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvFirstName = view.findViewById(R.id.tvProfileFirstName);
        tvLastName = view.findViewById(R.id.tvProfileLastName);
        tvEmail = view.findViewById(R.id.tvProfileEmail);

        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);

        sharedPrefManager = SharedPrefManager.getInstance(requireContext());

        databaseHelper = new DatabaseHelper(
                requireContext(),
                "NoteApp.db",
                null,
                3
        );

        loadUserData();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadUserData() {

        String currentUserEmail =
                sharedPrefManager.readString("currentUserEmail", "");

        User user = databaseHelper.getUserByEmail(currentUserEmail);

        if (user != null) {
            tvFirstName.setText(user.getFirstName());
            tvLastName.setText(user.getLastName());
            tvEmail.setText(user.getEmail());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (tvFirstName != null) {
            loadUserData();
        }

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).refreshDrawerHeader();
        }
    }
}