package com.idea.church.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idea.church.models.Material;
import com.idea.church.R;

import java.util.ArrayList;

public class DownloadedMaterialItemAdapter extends RecyclerView.Adapter<DownloadedMaterialItemAdapter.MaterialViewHolder> {
    ArrayList<Material> materials;
    private Context context;

    //Listener for items
    private OnItemClickListener listener;

    public DownloadedMaterialItemAdapter(ArrayList<Material> materials, Context context) {
        this.materials = materials;
        this.context = context;
    }

    // Listener interface
    public interface OnItemClickListener{
        void onItemClickListener(View itemView, int position);
        void onDeleteClickListener(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.downloaded_material_item, null);

        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedMaterialItemAdapter.MaterialViewHolder holder, int position) {
        String title = materials.get(position).getTitle();
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView title;
        Button delete;


        MaterialViewHolder(@NonNull final View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            delete = itemView.findViewById(R.id.deleteBtn);
            title = itemView.findViewById(R.id.title);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClickListener(itemView, getLayoutPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onDeleteClickListener(itemView, getLayoutPosition());
                }
            });
        }
    }
}
