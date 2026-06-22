package com.example.travelplannerapp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelplannerapp.R;
import com.example.travelplannerapp.models.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private List<Trip> tripList;
    private OnTripClickListener listener;
    private boolean isAdmin;

    public interface OnTripClickListener {
        void onFavoriteClick(Trip trip); // Edit
        void onReserveClick(Trip trip);  // Delete
        void onTripClick(Trip trip);
    }

    public TripAdapter(
            Context context,
            List<Trip> tripList,
            OnTripClickListener listener,
            boolean isAdmin) {

        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_trip, parent, false);

        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TripViewHolder holder,
            int position) {

        Trip trip = tripList.get(position);

        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvDescription.setText(trip.getDescription());
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvDuration.setText(trip.getDurationDays() + " Days");
        holder.tvRating.setText("⭐ " + trip.getRating());

        Glide.with(context)
                .load(trip.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imgTrip);

        if (isAdmin) {

            holder.btnFavorite.setVisibility(View.VISIBLE);
            holder.btnReserve.setVisibility(View.VISIBLE);

            holder.btnFavorite.setText("Edit");
            holder.btnReserve.setText("Delete");

            holder.btnFavorite.setBackgroundTintList(
                    ColorStateList.valueOf(
                            context.getResources().getColor(
                                    R.color.theme_color_3)));

            holder.btnReserve.setBackgroundTintList(
                    ColorStateList.valueOf(
                            context.getResources().getColor(
                                    R.color.theme_color_4)));

        } else {

            holder.btnFavorite.setVisibility(View.VISIBLE);
            holder.btnReserve.setVisibility(View.VISIBLE);

            holder.btnFavorite.setText("Favorite");
            holder.btnReserve.setText("Reserve");

            holder.btnFavorite.setBackgroundTintList(
                    ColorStateList.valueOf(
                            context.getResources().getColor(
                                    R.color.theme_color_4)));

            holder.btnReserve.setBackgroundTintList(
                    ColorStateList.valueOf(
                            context.getResources().getColor(
                                    R.color.theme_color_3)));
        }

        holder.btnFavorite.setOnClickListener(v ->
                listener.onFavoriteClick(trip));

        holder.btnReserve.setOnClickListener(v ->
                listener.onReserveClick(trip));

        holder.itemView.setOnClickListener(v ->
                listener.onTripClick(trip));
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(List<Trip> newList) {
        tripList = newList;
        notifyDataSetChanged();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView imgTrip;

        TextView tvDestination;
        TextView tvCountry;
        TextView tvDescription;
        TextView tvPrice;
        TextView tvDuration;
        TextView tvRating;

        Button btnFavorite;
        Button btnReserve;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            imgTrip = itemView.findViewById(R.id.imgTrip);

            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvRating = itemView.findViewById(R.id.tvRating);

            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }
    }
}