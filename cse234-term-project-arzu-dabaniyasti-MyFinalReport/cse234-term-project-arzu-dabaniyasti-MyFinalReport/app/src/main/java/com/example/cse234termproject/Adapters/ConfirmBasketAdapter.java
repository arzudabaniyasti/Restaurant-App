package com.example.cse234termproject.Adapters;

import com.example.cse234termproject.Model.ConfirmBasketModel;
import com.example.cse234termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.util.List;

public class ConfirmBasketAdapter extends RecyclerView.Adapter<ConfirmBasketAdapter.ViewHolder>  {
    Context context;
    List<ConfirmBasketModel> cardList;
    private FirebaseFirestore firestr;
    private FirebaseAuth mAuth;
    private int lastSelectedPosition = -1;


    public ConfirmBasketAdapter(Context context, List<ConfirmBasketModel> cardList) {
        this.context = context;
        this.cardList=cardList;
        firestr=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull final ConfirmBasketAdapter.ViewHolder holder,final int position) {
        holder.cardName.setText(cardList.get(position).getCardName());
        holder.deleteLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestr.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("Credit Cards")
                        .document(cardList.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cardList.remove(cardList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.radioButton.setChecked(lastSelectedPosition == position);

    }
    @Override
    public int getItemCount() {
        return cardList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cardName;
        ImageView deleteLine;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.CardName);
            deleteLine = itemView.findViewById(R.id.deleteLine);
            radioButton = itemView.findViewById(R.id.cardcheckBox);

        }
    }

    }

