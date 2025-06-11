package com.example.realestate.models;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.fragments.ReservationDetailsFragment;
import com.example.realestate.utils.DataBaseHelper;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> propertyList;
    private Context context;
    private DataBaseHelper dbHelper;
    private String currentUserEmail;

    public PropertyAdapter(List<Property> propertyList, Context context, DataBaseHelper dbHelper, String currentUserEmail) {
        this.propertyList = propertyList;
        this.context = context;
        this.dbHelper = dbHelper;
        this.currentUserEmail = currentUserEmail;
    }
    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvDescription.setText(property.getDescription());
        holder.tvLocation.setText(property.getLocation());

        boolean hasOffer = dbHelper.isOffered(property.getId());

        if (hasOffer) {
            int discountedPrice = dbHelper.getOfferPrice(property.getId());

            holder.tvPrice.setText("$" + discountedPrice + "  ");
            holder.tvPrice.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));

            // Optionally show original price as strikethrough in another TextView
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
            holder.tvOriginalPrice.setText("$" + property.getPrice());
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPrice.setText("$" + property.getPrice());
            holder.tvPrice.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(property.getImage_url())
                .placeholder(R.drawable.icon_noimg)
                .into(holder.imgThumbnail);


        holder.imgThumbnail.setAlpha(0f);
        holder.imgThumbnail.animate().alpha(1f).setDuration(700).start();

        boolean isFavorite = dbHelper.isFavorite(currentUserEmail, property.getId());
        holder.btnFavorite.setImageResource(
                isFavorite ? R.drawable.icon_favorite_filled : R.drawable.icon_favorite_border
        );

        holder.btnFavorite.setOnClickListener(v -> {
            if (dbHelper.isFavorite(currentUserEmail, property.getId())) {
                dbHelper.removeFavorite(currentUserEmail, property.getId());
                Toast.makeText(context, "Property removed from favorite", Toast.LENGTH_SHORT).show();
                holder.btnFavorite.setImageResource(R.drawable.icon_favorite_border);
            } else {
                dbHelper.addFavorite(currentUserEmail, property.getId());
                Toast.makeText(context, "Property added to favorite", Toast.LENGTH_SHORT).show();
                holder.btnFavorite.setImageResource(R.drawable.icon_favorite_filled);
            }
        });

        holder.btnReserve.setOnClickListener(v -> {
            ReservationDetailsFragment fragment = new ReservationDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("property", property);
            fragment.setArguments(bundle);

            ((FragmentActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvDescription, tvLocation, tvPrice,tvOriginalPrice;
        ImageButton btnFavorite;
        Button btnReserve;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOriginalPrice = itemView.findViewById(R.id.tv_original_price);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btnReserve = itemView.findViewById(R.id.btn_reserve);
        }
    }
}
