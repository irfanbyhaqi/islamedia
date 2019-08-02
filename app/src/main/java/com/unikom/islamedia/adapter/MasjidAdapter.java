package com.unikom.islamedia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unikom.islamedia.R;
import com.unikom.islamedia.model.MasjidModel;

import java.util.List;

public class MasjidAdapter extends RecyclerView.Adapter<MasjidAdapter.ViewHolder> {

    private List<MasjidModel> masjidModels;
    private Context context;
    private OnItemClickListener mListener;


    public MasjidAdapter(List<MasjidModel> masjidModels, Context context) {
        this.masjidModels = masjidModels;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemListener(int position);
    }

    public void setOnitemListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MasjidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.masjid_content, parent, false);

        return new MasjidAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasjidAdapter.ViewHolder holder, int position) {
        holder.nama_masjid.setText(masjidModels.get(position).getNama_masjid());
        holder.alamat_masjid.setText(masjidModels.get(position).getAlamat_masjid());

        Glide.with(context).load(masjidModels.get(position).getLogo_masjid()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto_masjid);
    }

    @Override
    public int getItemCount() {
        return masjidModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_masjid,alamat_masjid;
        ImageView foto_masjid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_masjid = itemView.findViewById(R.id.nama_masjid);
            alamat_masjid = itemView.findViewById(R.id.alamat_masjid);
            foto_masjid = itemView.findViewById(R.id.foto_masjid);

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
