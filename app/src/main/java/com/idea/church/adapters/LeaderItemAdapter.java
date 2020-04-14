package com.idea.church.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idea.church.R;
import com.idea.church.models.Leader;

import java.util.ArrayList;

public class LeaderItemAdapter extends RecyclerView.Adapter<LeaderItemAdapter.LeaderViewHolder> {
    private ArrayList<Leader> leaders;
    private Context context;

    private OnItemClickListener listener;

    public LeaderItemAdapter(ArrayList<Leader> leaders, Context context) {
        this.leaders = leaders;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClickListener(View itemView, int position);
        void onCallClickListener(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.leader_item, null);

        return new LeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderViewHolder holder, int position) {
        Leader leader = leaders.get(position);
        holder.fullName.setText(leader.getFullName());
        holder.position.setText(leader.getPosition());
        holder.contact.setText(leader.getContact());

        Glide.with(context)
                .load(leader.getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return leaders.size();
    }

    class LeaderViewHolder extends RecyclerView.ViewHolder {
        TextView fullName;
        TextView position;
        TextView contact;
        ImageView image;
        CardView cardView;
        Button callBtn;

        LeaderViewHolder(@NonNull final View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.fullName);
            position = itemView.findViewById(R.id.position);
            contact = itemView.findViewById(R.id.contact);
            image = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.card);
            callBtn = itemView.findViewById(R.id.callBtn);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onItemClickListener(itemView, getLayoutPosition());
                }
            });

            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onCallClickListener(itemView, getLayoutPosition());
                }
            });

        }
    }
}
