package com.example.ktx_ute.adapter;

import static com.example.ktx_ute.R.drawable.background_message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.R;
import com.example.ktx_ute.activity.BqlPhongShowActivity;
import com.example.ktx_ute.interfacee.IOnClick;
import com.example.ktx_ute.model.Phong;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder> implements Filterable {
    List<Phong> phongList;
    List<Phong> phongListOld;
    IOnClick iOnClick;

    Boolean isEnable;

    public void onClickItem(IOnClick iOnClick) {
        this.iOnClick = iOnClick;
    }


    public PhongAdapter(List<Phong> phongList) {
        this.phongList = phongList;
        this.phongListOld = phongList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phong_row, parent, false);
        return new PhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClick.onClickPhong(phongList.get(position));
            }
        });




        holder.btnPhong.setText(new StringBuilder().append("Phòng ").append(phongList.get(position).getSoPhong()).toString());

    }

    @Override
    public int getItemCount() {
        if (phongList == null) {
            return 0;
        }
        return phongList.size();
    }

    class PhongViewHolder extends RecyclerView.ViewHolder {
        Button btnPhong;
        public PhongViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPhong = itemView.findViewById(R.id.btn_login_sv);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    phongList = phongListOld;
                } else {
                    List<Phong> list1 = new ArrayList<>();
                    for (Phong phong : phongListOld) {
                        if (phong.getSoPhong().toLowerCase().contains(strSearch.toLowerCase())) {
                            list1.add(phong);
                        }
                    }
                    phongList = list1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = phongList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                phongList = (List<Phong>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
