package birzeit.edu.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NoteDetailsFragment extends Fragment {

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

        TextView tvTitle =
                view.findViewById(R.id.tvDetailTitle);

        TextView tvDate =
                view.findViewById(R.id.tvDetailDate);

        TextView tvContent =
                view.findViewById(R.id.tvDetailContent);

        TextView tvTag =
                view.findViewById(R.id.tvDetailTag);

        ImageButton btnFavorite =
                view.findViewById(R.id.btnDetailFavorite);

        Bundle bundle = getArguments();

        if (bundle != null) {

            String title =
                    bundle.getString("title", "");

            String content =
                    bundle.getString("content", "");

            String tag =
                    bundle.getString("tag", "");

            String date =
                    bundle.getString("date", "");

            boolean favorite =
                    bundle.getBoolean("favorite", false);

            tvTitle.setText(title);
            tvContent.setText(content);
            tvDate.setText(date);

            if (tag.isEmpty()) {
                tvTag.setVisibility(View.GONE);
            } else {
                tvTag.setText("#" + tag);
            }

            if (favorite) {

                btnFavorite.setImageResource(
                        android.R.drawable.btn_star_big_on
                );

            } else {

                btnFavorite.setImageResource(
                        android.R.drawable.btn_star_big_off
                );
            }
        }

        return view;
    }
}