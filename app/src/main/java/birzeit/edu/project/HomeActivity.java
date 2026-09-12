package birzeit.edu.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            openFragment(new allnote(), "All Notes");
            navigationView.setCheckedItem(R.id.nav_all);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_all) {
                openFragment(new allnote(), "All Notes");
            } else if (id == R.id.nav_favorites) {
                openFragment(new favorites(), "Favorites");
            } else if (id == R.id.nav_sorted) {
                openFragment(new SortedFragment(), "Sorted");
            } else if (id == R.id.nav_search) {
                openFragment(new SearchFragment(), "Search");
            } else if (id == R.id.nav_profile) {
                openFragment(new Profile(), "Profile");
            } else if (id == R.id.nav_logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void openFragment(Fragment fragment, String title) {
        toolbar.setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void openNoteDetails(AddNote note) {

        NoteDetailsFragment fragment = new NoteDetailsFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("noteId", note.getId());
        bundle.putString("title", note.getTitle());
        bundle.putString("content", note.getContent());
        bundle.putString("tag", note.getTag());
        bundle.putString("date", note.getCreationDate());
        bundle.putBoolean("favorite", note.isFavorite());

        fragment.setArguments(bundle);

        openFragment(fragment, "Note Details");
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }
}
