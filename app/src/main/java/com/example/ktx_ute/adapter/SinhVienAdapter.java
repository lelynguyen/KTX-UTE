package com.example.ktx_ute.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.R;
import com.example.ktx_ute.interfacee.IOnClickSinhVien;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.SinhVienViewHolder> implements Filterable {
    List<SinhVien> sinhVienList;
    List<SinhVien> sinhVienListOld;
    IOnClickSinhVien iOnClick;


    public SinhVienAdapter(List<SinhVien> sinhVienList, IOnClickSinhVien iOnClick) {
        this.sinhVienList = sinhVienList;
        this.sinhVienListOld = sinhVienList;
        this.iOnClick = iOnClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SinhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sinhvien_row, parent, false);
        return new SinhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhVienViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClick.onClickSinhVien(sinhVienList.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClick.onClickSinhVien(sinhVienList.get(position));
            }
        });
        holder.txtTen.setText(sinhVienList.get(position).getHoTenSV());
        holder.txtMaSv.setText(sinhVienList.get(position).getMaSV());
    }

    @Override
    public int getItemCount() {
        if (sinhVienList!=null) {
            return sinhVienList.size();
        }
        return 0;
    }


    class SinhVienViewHolder extends RecyclerView.ViewHolder {
        Button btnSua;
        TextView txtMaSv, txtTen;
        public SinhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSua = itemView.findViewById(R.id.btn_sua);
            txtTen = itemView.findViewById(R.id.ten_sinh_vien);
            txtMaSv = itemView.findViewById(R.id.masv);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    sinhVienList = sinhVienListOld;
                } else {
                    List<SinhVien> list1 = new ArrayList<>();
                    for (SinhVien sinhVien : sinhVienListOld) {
                        if (sinhVien.getMaSV().toLowerCase().contains(strSearch.toLowerCase())) {
                            list1.add(sinhVien);
                        }
                    }
                    sinhVienList = list1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sinhVienList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sinhVienList = (List<SinhVien>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
