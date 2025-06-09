package com.example.realestate.models;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

import java.util.List;

public class AdminPropertyAdapter extends RecyclerView.Adapter<AdminPropertyAdapter.ViewHolder> {
    private Context context;
    private List<Property> propertyList;
    private DataBaseHelper db;

    public AdminPropertyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
        this.db = new DataBaseHelper(context, "Project_DB", null, 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discount_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.tvTitle.setText(property.getTitle());
        holder.tvLocation.setText(property.getLocation());

        if (property.isFeatured() && property.getDiscountPrice() != null) {
            holder.tvOriginalPrice.setText("$" + property.getPrice());
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDiscountPrice.setText("$" + property.getDiscountPrice());
            holder.tvDiscountPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvOriginalPrice.setText("$" + property.getPrice());
            holder.tvOriginalPrice.setPaintFlags(0); // remove strikethrough
            holder.tvDiscountPrice.setVisibility(View.GONE);
        }


        Glide.with(context).load(property.getImage_url())
                .placeholder(R.drawable.icon_noimg)
                .into(holder.imgThumbnail);

        // Toggle buttons
        boolean hasOffer = property.isFeatured();
        holder.btnAddOffer.setVisibility(hasOffer ? View.GONE : View.VISIBLE);
        holder.btnRemoveOffer.setVisibility(hasOffer ? View.VISIBLE : View.GONE);

        // Add Offer
        holder.btnAddOffer.setOnClickListener(v -> {
            EditText input = new EditText(context);
            input.setHint("Enter discounted price");

            new AlertDialog.Builder(context)
                    .setTitle("Add Special Offer")
                    .setView(input)
                    .setPositiveButton("Save", (dialog, which) -> {
                        try {
                            String inputText = input.getText().toString().trim();
                            if (inputText.isEmpty()) {
                                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int offer = Integer.parseInt(inputText);
                            if (offer >= property.getPrice()) {
                                Toast.makeText(context, "Offer must be less than original price", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int id =property.getId();
                            db.addOffer(id, offer);
                            property.setDiscountPrice(offer);
                            property.setFeatured(true);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Remove Offer
        holder.btnRemoveOffer.setOnClickListener(v -> {
            db.removeOffer(property.getId());
            property.setFeatured(false);
            property.setDiscountPrice(null);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvLocation, tvOriginalPrice, tvDiscountPrice;
        Button btnAddOffer, btnRemoveOffer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvOriginalPrice = itemView.findViewById(R.id.tv_original_price);
            tvDiscountPrice = itemView.findViewById(R.id.tv_discount_price);

            btnAddOffer = itemView.findViewById(R.id.btn_add_offer);
            btnRemoveOffer = itemView.findViewById(R.id.btn_remove_offer);
        }
    }
}
