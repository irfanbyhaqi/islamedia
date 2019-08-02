package com.unikom.islamedia.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unikom.islamedia.R;
import com.unikom.islamedia.model.LokasiModel;

import java.util.List;

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.ViewHolder> {

    private List<LokasiModel> lokasiModels;
    private Context context;
    private OnItemClickListener mListener;
    private ClickListener listener;

    public LokasiAdapter(List<LokasiModel> lokasiModels, Context context, ClickListener listener) {
        this.lokasiModels = lokasiModels;
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
    public LokasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lokasi_kontent, parent, false);
//        View view = infalater.inflate(R.layout.masjid_content,parent,false);
        return new LokasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LokasiAdapter.ViewHolder holder, int position) {
        String jarakkk = String.valueOf(lokasiModels.get(position).getJarak());
        String[] parts = jarakkk.split("\\.");
        Log.i("Jarak", parts[0]);

        String ket = parts[0].equals("0") ? "m" : "km";
        String jar = parts[0].equals("0") ? parts[1].substring(0, 3) : parts[0] +","+ parts[1].substring(0,1);

        String alamat = lokasiModels.get(position).getAlamat_masjid();
        holder.nama_masjid.setText(lokasiModels.get(position).getNama_masjid());
        holder.alamat_masjid.setText(alamat.length() < 10 ? alamat : alamat.substring(0,12)+"...");
        holder.jarak_masjid.setText(jar+" "+ket);

        Glide.with(context).load(lokasiModels.get(position).getLogo_masjid()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.logo_masjid);
    }

    @Override
    public int getItemCount() {
        return lokasiModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_masjid,alamat_masjid,jarak_masjid;
        ImageView logo_masjid;
        LinearLayout redirect_map;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            nama_masjid = itemView.findViewById(R.id.nama_masjid);
            alamat_masjid = itemView.findViewById(R.id.alamat_masjid);
            jarak_masjid = itemView.findViewById(R.id.jarak_masjid);
            logo_masjid = itemView.findViewById(R.id.logo_masjid);
            redirect_map = itemView.findViewById(R.id.redirect_map);

            redirect_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositionClicked(v, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View modelBottomSheet = LayoutInflater.from(context).inflate(R.layout.detail_lokasi_popup, null);

                    TextView det_nama_masjid =  modelBottomSheet.findViewById(R.id.det_nama_masjid);
                    TextView det_alamat =  modelBottomSheet.findViewById(R.id.det_alamat_masjid);
                    ImageView det_logo_masjid =  modelBottomSheet.findViewById(R.id.det_logo_masjid);
                    LinearLayout redirect =  modelBottomSheet.findViewById(R.id.redirect_map);

                    det_nama_masjid.setText(lokasiModels.get(getAdapterPosition()).getNama_masjid());
                    det_alamat.setText(lokasiModels.get(getAdapterPosition()).getAlamat_masjid());

                    Glide.with(context).load(lokasiModels.get(getAdapterPosition()).getLogo_masjid()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(det_logo_masjid);

                    redirect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+lokasiModels.get(getAdapterPosition()).getLat()+","+lokasiModels.get(getAdapterPosition()).getLon()+"");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
                        }
                    });

                    BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
                    dialog.setContentView(modelBottomSheet);
                    dialog.show();

                }
            });
        }
    }
}
