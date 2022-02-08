package com.example.cse234termproject.Adapters;

import com.example.cse234termproject.Model.ReservationModel;
import com.example.cse234termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>  {
    Context context;
    List<ReservationModel> reservationList;
    private FirebaseFirestore firestr;
    private FirebaseAuth mAuth;


    public ReservationAdapter(Context context, List<ReservationModel> reservationList) {
        this.context = context;
        this.reservationList=reservationList;
        firestr=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ViewHolder holder, int position) {
        holder.date.setText(reservationList.get(position).getDate());
        holder.time.setText(reservationList.get(position).getTime());
        holder.number.setText(reservationList.get(position).getPersonNumber());
        holder.cancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestr.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("Reservations")
                        .document(reservationList.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            reservationList.remove(reservationList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"ERROR"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,time,number,cancelReservation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.reservationDate);
            time=itemView.findViewById(R.id.reservationTime);
            number=itemView.findViewById(R.id.reservationPeopleNumber);
            cancelReservation=itemView.findViewById(R.id.cancelReservation);

        }
    }
}
