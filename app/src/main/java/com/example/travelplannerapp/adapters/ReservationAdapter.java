package com.example.travelplannerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.models.Reservation;

import java.util.List;

public class ReservationAdapter
        extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    List<Reservation> reservations;

    public ReservationAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_item,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Reservation reservation = reservations.get(position);

        holder.destination.setText(
                reservation.getDestination());

        holder.date.setText(
                "Date: " + reservation.getReservationDate());

        holder.status.setText(
                "Status: " + reservation.getStatus());

        holder.info.setText(
                "Type: " + reservation.getType()
                        + " | Quantity: "
                        + reservation.getQuantity());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView destination;
        TextView date;
        TextView status;
        TextView info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            destination = itemView.findViewById(R.id.tvDestination);
            date = itemView.findViewById(R.id.tvDate);
            status = itemView.findViewById(R.id.tvStatus);
            info = itemView.findViewById(R.id.tvInfo);
        }
    }
}