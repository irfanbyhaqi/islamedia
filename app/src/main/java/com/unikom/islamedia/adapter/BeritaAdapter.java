package com.unikom.islamedia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unikom.islamedia.R;
import com.unikom.islamedia.model.BeritaModel;

import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {
    private List<BeritaModel> beritaModels;
    private Context context;
    private OnItemClickListener mListener;
    private ClickListener listener;

    public BeritaAdapter(List<BeritaModel> beritaModels, Context context, ClickListener listener) {
        this.beritaModels = beritaModels;
        this.context = context;
        this.listener = listener;
    }

    public interface ClickListener{
        void onPositionClicked(View v, int position);
    }

    public interface OnItemClickListener{
        void onItemListener(int position);
    }

    public void setOnitemListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public BeritaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.berita_content, parent, false);
        return new BeritaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaAdapter.ViewHolder holder, int position) {
        holder.judul_berita.setText(beritaModels.get(position).getJudul());

        Glide.with(context).load(beritaModels.get(position).getGambar()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto_berita);
    }

    @Override
    public int getItemCount() {
        return beritaModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView judul_berita;
        ImageView foto_berita;
        Button btn_baca;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul_berita = itemView.findViewById(R.id.judul_berita);
            foto_berita = itemView.findViewById(R.id.foto_berita);
            btn_baca = itemView.findViewById(R.id.baca);


            btn_baca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositionClicked(v, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemListener(position);
                        }
                    }
                }
            });
        }


    }




}
