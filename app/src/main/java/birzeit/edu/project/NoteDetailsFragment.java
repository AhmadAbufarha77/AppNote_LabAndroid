package birzeit.edu.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class NoteDetailsFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvContent;
    private TextView tvTag;

    private ImageButton btnFavorite;

    private int noteId;

    public NoteDetailsFragment() {
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_note_details,
                container,
                false
        );


        databaseHelper = new DatabaseHelper(
                requireContext(),
                "NoteApp.db",
                null,
                3
        );


        tvTitle =
                view.findViewById(
                        R.id.tvDetailTitle
                );

        tvDate =
                view.findViewById(
                        R.id.tvDetailDate
                );

        tvContent =
                view.findViewById(
                        R.id.tvDetailContent
                );

        tvTag =
                view.findViewById(
                        R.id.tvDetailTag
                );

        btnFavorite =
                view.findViewById(
                        R.id.btnDetailFavorite
                );


        Button btnEdit =
                view.findViewById(
                        R.id.btnEditNote
                );

        Button btnDelete =
                view.findViewById(
                        R.id.btnDeleteNote
                );

        Button btnEmail =
                view.findViewById(
                        R.id.btnEmailNote
                );


        Bundle bundle = getArguments();

        if (bundle != null) {

            noteId =
                    bundle.getInt(
                            "noteId",
                            -1
                    );

            loadNote();
        }


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNote note =
                        databaseHelper.getNoteById(
                                noteId
                        );

                if (note == null) {
                    return;
                }


                Intent intent =
                        new Intent(
                                requireContext(),
                                EditNoteActivity.class
                        );

                intent.putExtra(
                        "noteId",
                        note.getId()
                );

                intent.putExtra(
                        "title",
                        note.getTitle()
                );

                intent.putExtra(
                        "content",
                        note.getContent()
                );

                intent.putExtra(
                        "tag",
                        note.getTag()
                );

                intent.putExtra(
                        "date",
                        note.getCreationDate()
                );

                startActivity(intent);
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(
                        requireContext()
                )
                        .setTitle("Delete Note")
                        .setMessage(
                                "Are you sure you want to delete this note?"
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Delete",
                                (dialog, which) -> {

                                    int result =
                                            databaseHelper.deleteNote(
                                                    noteId
                                            );

                                    if (result > 0) {

                                        Toast.makeText(
                                                requireContext(),
                                                "Note deleted",
                                                Toast.LENGTH_SHORT
                                        ).show();


                                        HomeActivity homeActivity =
                                                (HomeActivity) requireActivity();

                                        homeActivity.openAllNotes();
                                    }
                                }
                        )
                        .show();
            }
        });


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNote note =
                        databaseHelper.getNoteById(
                                noteId
                        );

                if (note == null) {
                    return;
                }


                Intent intent =
                        new Intent(
                                Intent.ACTION_SENDTO
                        );

                intent.setData(
                        Uri.parse("mailto:")
                );

                intent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        note.getTitle()
                );

                intent.putExtra(
                        Intent.EXTRA_TEXT,
                        note.getContent()
                );


                if (intent.resolveActivity(
                        requireActivity()
                                .getPackageManager()
                ) != null) {

                    startActivity(intent);

                } else {

                    Toast.makeText(
                            requireContext(),
                            "No email application found",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


        btnFavorite.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               AddNote note =
                                                       databaseHelper.getNoteById(
                                                               noteId
                                                       );

                                               if (note == null) {
                                                   return;
                                               }


                                               boolean newFavorite =
                                                       !note.isFavorite();


                                               databaseHelper.updateFavorite(
                                                       noteId,
                                                       newFavorite
                                               );


                                               if (newFavorite) {

                                                   btnFavorite.setImageResource(
                                                           android.R.drawable.btn_star_big_on
                                                   );

                                               } else {

                                                   btnFavorite.setImageResource(
                                                           android.R.drawable.btn_star_big_off
                                                   );
                                               }


                                               Animation animation =
                                                       AnimationUtils.loadAnimation(
                                                               requireContext(),
                                                               R.anim.favorite_star
                                                       );

                                               btnFavorite.startAnimation(
                                                       animation
                                               );
                                           }

                                       }

        );


        return view;
    }


    @Override
    public void onResume() {

        super.onResume();

        if (databaseHelper != null &&
                noteId != -1) {

            loadNote();
        }
    }


    private void loadNote() {

        AddNote note =
                databaseHelper.getNoteById(
                        noteId
                );

        if (note == null) {
            return;
        }


        tvTitle.setText(
                note.getTitle()
        );

        tvContent.setText(
                note.getContent()
        );

        tvDate.setText(
                note.getCreationDate()
        );


        if (note.getTag() == null ||
                note.getTag().isEmpty()) {

            tvTag.setVisibility(
                    View.GONE
            );

        } else {

            tvTag.setVisibility(
                    View.VISIBLE
            );

            tvTag.setText(
                    "#" + note.getTag()
            );
        }


        if (note.isFavorite()) {

            btnFavorite.setImageResource(
                    android.R.drawable.btn_star_big_on
            );

        } else {

            btnFavorite.setImageResource(
                    android.R.drawable.btn_star_big_off
            );
        }
    }
}