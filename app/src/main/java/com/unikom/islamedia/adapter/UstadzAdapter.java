package com.unikom.islamedia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unikom.islamedia.R;
import com.unikom.islamedia.model.UstadzModel;

import java.util.List;

public class UstadzAdapter extends RecyclerView.Adapter<UstadzAdapter.ViewHolder> {
    private List<UstadzModel> ustadzModels;
    private Context context;
    private OnItemClickListener mListener;
    private ClickListener listener;

    public UstadzAdapter(List<UstadzModel> ustadzModels, Context context, ClickListener listener) {
        this.ustadzModels = ustadzModels;
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
    public UstadzAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ustadz_kontent, parent, false);
        return new UstadzAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UstadzAdapter.ViewHolder holder, int position) {

        String alamat = ustadzModels.get(position).getAlamat_ustadz();

        holder.nama_ustadz.setText(ustadzModels.get(position).getNama_ustadz());
        holder.alamat_ustadz.setText(alamat.equals("---") || alamat.length() <= 8 ? alamat : alamat.substring(0,10)+"...");

        Glide.with(context).load(ustadzModels.get(position).getFoto_ustadz()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto_ustadz);

    }

    @Override
    public int getItemCount() {
        return ustadzModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_ustadz,alamat_ustadz;
        ImageView foto_ustadz;
        Button wa_click;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            nama_ustadz = itemView.findViewById(R.id.nama_ustadz);
            alamat_ustadz = itemView.findViewById(R.id.alamat_ustadz);
            foto_ustadz = itemView.findViewById(R.id.foto_ustadz);
            wa_click = itemView.findViewById(R.id.btn_wa);

            wa_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositionClicked(v, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("InflateParams") View modelBottomSheet = LayoutInflater.from(context).inflate(R.layout.detail_ustadz_popup, null);

                    TextView det_nama_ustadz =  modelBottomSheet.findViewById(R.id.det_nama_ustadz);
                    TextView det_kompetensi =  modelBottomSheet.findViewById(R.id.status_ustadz);
                    TextView det_alamat =  modelBottomSheet.findViewById(R.id.det_alamat_ustadz);
                    TextView det_masjid =  modelBottomSheet.findViewById(R.id.det_masjid);
                    ImageView det_foto = modelBottomSheet.findViewById(R.id.det_foto_ustadz);
                    LinearLayout call_wa = modelBottomSheet.findViewById(R.id.call_pengguna);


                    det_nama_ustadz.setText(ustadzModels.get(getAdapterPosition()).getNama_ustadz());
                    det_kompetensi.setText(ustadzModels.get(getAdapterPosition()).getKompetensi());
                    det_alamat.setText(ustadzModels.get(getAdapterPosition()).getAlamat_ustadz());
                    det_masjid.setText(ustadzModels.get(getAdapterPosition()).getNama_masjid());

                    Glide.with(context).load(ustadzModels.get(getAdapterPosition()).getFoto_ustadz()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(det_foto);


                    call_wa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String phone = ustadzModels.get(getAdapterPosition()).getNo_kontak_ustadz().toString().trim();
                            String formatNumber = phone.replaceFirst("0","+62");
                            String url = "https://api.whatsapp.com/send?phone="+formatNumber;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
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
