package birzeit.edu.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<AddNote> notes;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(AddNote note);
        void onFavoriteClick(AddNote note, int position);
    }

    public NoteAdapter(
            ArrayList<AddNote> notes,
            OnNoteClickListener listener
    ) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.card,
                        parent,
                        false
                );

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteViewHolder holder,
            int position
    ) {

        AddNote note = notes.get(position);

        holder.tvTitle.setText(
                note.getTitle()
        );

        holder.tvDate.setText(
                note.getCreationDate()
        );

        if (note.isFavorite()) {

            holder.btnFavorite.setImageResource(
                    android.R.drawable.btn_star_big_on
            );

        } else {

            holder.btnFavorite.setImageResource(
                    android.R.drawable.btn_star_big_off
            );
        }


        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onNoteClick(note);
            }

        });


        holder.btnFavorite.setOnClickListener(v -> {

            Animation animation =
                    AnimationUtils.loadAnimation(
                            v.getContext(),
                            R.anim.favorite_star
                    );

            holder.btnFavorite.startAnimation(
                    animation
            );


            int currentPosition =
                    holder.getAdapterPosition();

            if (listener != null &&
                    currentPosition != RecyclerView.NO_POSITION) {

                listener.onFavoriteClick(
                        note,
                        currentPosition
                );
            }

        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(ArrayList<AddNote> notes) {

        this.notes = notes;
        notifyDataSetChanged();
    }


    public static class NoteViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;
        ImageButton btnFavorite;

        public NoteViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvTitle =
                    itemView.findViewById(
                            R.id.tvNoteTitle
                    );

            tvDate =
                    itemView.findViewById(
                            R.id.tvNoteDate
                    );

            btnFavorite =
                    itemView.findViewById(
                            R.id.btnFavorite
                    );
        }
    }
}