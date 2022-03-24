package com.example.studentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    List<Etudiant> data;
    Context context;

    public myAdapter(List<Etudiant> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_etudiant, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getImage()).into(holder.photoImage);
        holder.id.setText(data.get(position).getId());
        holder.nom.setText(data.get(position).getNom());
        holder.prenom.setText(data.get(position).getPrenom());
        holder.ville.setText(data.get(position).getVille());
        holder.sexe.setText(data.get(position).getSexe());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, nom, prenom, ville, sexe;
        ImageView photoImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.idOutput);
            nom = (TextView) itemView.findViewById(R.id.nomOutput);
            prenom = (TextView) itemView.findViewById(R.id.prenomOutput);
            ville = (TextView) itemView.findViewById(R.id.villeOutput);
            sexe = (TextView) itemView.findViewById(R.id.sexeOutput);
            photoImage = itemView.findViewById(R.id.imageViewInput);
        }
    }
}
