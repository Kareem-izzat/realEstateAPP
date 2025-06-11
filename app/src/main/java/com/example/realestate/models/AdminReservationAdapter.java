package com.example.realestate.models;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

import java.util.List;

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.ViewHolder> {

    private final Context context;
    private final List<Reservation> reservationList;
    private final DataBaseHelper db;

    public AdminReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
        this.db = new DataBaseHelper(context, "Project_DB", null, 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation res = reservationList.get(position);

        Cursor userCursor = db.getUserByEmail(res.getUserEmail());
        String fullName = res.getUserEmail(); // fallback
        if (userCursor != null && userCursor.moveToFirst()) {
            String first = userCursor.getString(userCursor.getColumnIndexOrThrow("first_name"));
            String last = userCursor.getString(userCursor.getColumnIndexOrThrow("last_name"));
            fullName = first + " " + last;
            userCursor.close();
        }

        Property property = JsonParser.findPropertyById(res.getPropertyId());
        String title = "Unknown Property";
        String imageUri = null;
        if (property != null) {
            title = property.getTitle();
            imageUri = property.getImage_url();
        }

        holder.tvPropertyTitle.setText(context.getString(R.string.property_title, title));
        holder.tvUserEmail.setText(context.getString(R.string.customer_name, fullName));
        holder.tvReservationDate.setText(context.getString(R.string.reservation_period, res.getStartDate(), res.getEndDate()));

        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.icon_noimg)
                    .error(R.drawable.icon_noimg)
                    .into(holder.imgProperty);
        } else {
            holder.imgProperty.setImageResource(R.drawable.icon_noimg);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProperty;
        TextView tvPropertyTitle, tvUserEmail, tvReservationDate;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProperty = itemView.findViewById(R.id.img_property);
            tvPropertyTitle = itemView.findViewById(R.id.tv_property_title);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvReservationDate = itemView.findViewById(R.id.tv_reservation_date);
        }
    }
}
