package com.idea.church.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.idea.church.models.Material;
import com.idea.church.R;

import java.util.ArrayList;

public class MaterialItemAdapter extends RecyclerView.Adapter<MaterialItemAdapter.MaterialViewHolder> {
    private Context context;
    private ArrayList<Material> materials;

    //Listener for items
    private static OnItemClickListener listener;

    // Listener interface
    public interface OnItemClickListener{
        void setOnItemDownloadListener(View buttonDownload, int layoutPosition);
        void setOnViewPdfListener(View buttonDownload, int layoutPosition);
    }

    // method for setting listener to activity
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public MaterialItemAdapter(Context context, ArrayList<Material> materials){
        this.context  = context;
        this.materials   = materials;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.material_item, null);

        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material material = materials.get(position);
        holder.title.setText(material.getTitle());
        holder.date.setText(material.getDate());
        holder.desc.setText(material.getDesc());

        Glide.with(context)
                .load(material.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    class MaterialViewHolder extends RecyclerView.ViewHolder {
        Button buttonDownload;
        ImageView image;
        TextView title;
        TextView date;
        TextView desc;

        MaterialViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);

            buttonDownload = itemView.findViewById(R.id.download);
            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.setOnItemDownloadListener(itemView, getLayoutPosition());
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.setOnViewPdfListener(itemView, getLayoutPosition());
                }
            });
        }
    }
}
