package th.ac.udru.seksil;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private Context context;
    private ArrayList<String> displayNameStringArrayList, pathUrlStringArrayList;
    private LayoutInflater layoutInflater;

    public FriendAdapter(Context context,
                         ArrayList<String> displayNameStringArrayList,
                         ArrayList<String> pathUrlStringArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.displayNameStringArrayList = displayNameStringArrayList;
        this.pathUrlStringArrayList = pathUrlStringArrayList;
    }   // Constructor

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.recycler_friend, viewGroup, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);

        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder friendViewHolder, int i) {

        String urlPathString = pathUrlStringArrayList.get(i);
        String displayNameString = displayNameStringArrayList.get(i);

        friendViewHolder.textView.setText(displayNameString);

        Picasso.get().load(urlPathString).into(friendViewHolder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return displayNameStringArrayList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView textView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.recyclerViewFriend);
            textView = itemView.findViewById(R.id.txtDisplayName);

        }
    }   // FriendViewHolder Class

}   // Main Class