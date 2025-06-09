package com.example.realestate.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<ReservedProperty> list;
    private DataBaseHelper dbHelper;

    public ReservationAdapter(List<ReservedProperty> list, DataBaseHelper dbHelper) {
        this.list = list;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservedProperty reserved = list.get(position);
        Property property = reserved.getProperty();

        holder.tvTitle.setText(property.getTitle());
        holder.tvReservationTime.setText("Reserved on: " + reserved.getReservationDate());

        Glide.with(holder.itemView.getContext())
                .load(property.getImage_url())
                .placeholder(R.drawable.icon_noimg)
                .into(holder.imgThumbnail);

        holder.btnCancel.setOnClickListener(v -> {
            dbHelper.cancelReservation(property.getId(), reserved.getProperty().getId());
            list.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(v.getContext(), "Reservation cancelled", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvReservationTime;
        Button btnCancel;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvReservationTime = itemView.findViewById(R.id.tv_reservation_time);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}

