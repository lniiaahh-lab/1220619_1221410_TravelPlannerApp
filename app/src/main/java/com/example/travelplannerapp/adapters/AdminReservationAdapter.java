package com.example.travelplannerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.models.ReservationAdmin;

import java.util.List;

public class AdminReservationAdapter
        extends RecyclerView.Adapter<AdminReservationAdapter.ViewHolder> {

    private List<ReservationAdmin> reservationList;

    public AdminReservationAdapter(
            List<ReservationAdmin> reservationList) {

        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_admin_reservation,
                                parent,
                                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        ReservationAdmin reservation =
                reservationList.get(position);

        holder.tvUserEmail.setText(
                reservation.getUserEmail());

        holder.tvTrip.setText(
                "Trip: " +
                        reservation.getDestination());

        holder.tvDate.setText(
                "Date: " +
                        reservation.getDate());

        holder.tvQuantity.setText(
                "Quantity: " +
                        reservation.getQuantity());

        holder.tvType.setText(
                "Type: " +
                        reservation.getType());

        holder.tvStatus.setText(
                reservation.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvUserEmail;
        TextView tvTrip;
        TextView tvDate;
        TextView tvQuantity;
        TextView tvType;
        TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserEmail =
                    itemView.findViewById(R.id.tvUserEmail);

            tvTrip =
                    itemView.findViewById(R.id.tvTrip);

            tvDate =
                    itemView.findViewById(R.id.tvDate);

            tvQuantity =
                    itemView.findViewById(R.id.tvQuantity);

            tvType =
                    itemView.findViewById(R.id.tvType);

            tvStatus =
                    itemView.findViewById(R.id.tvStatus);
        }
    }
}