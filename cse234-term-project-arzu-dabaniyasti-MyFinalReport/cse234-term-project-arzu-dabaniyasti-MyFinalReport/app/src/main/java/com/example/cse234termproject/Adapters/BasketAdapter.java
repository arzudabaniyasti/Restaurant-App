package com.example.cse234termproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cse234termproject.Model.BasketModel;
import com.example.cse234termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;


public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder>  {
    Context context;
    List<BasketModel> basketList;
    int basketTotalPrice=0;
    private FirebaseFirestore firestr;
    private FirebaseAuth mAuth;


    public BasketAdapter(Context context, List<BasketModel> basketList) {
        this.context = context;
        this.basketList = basketList;
        firestr=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(basketList.get(position).getProductImg()).into(holder.imageView);
        holder.name.setText(basketList.get(position).getProductName());
        holder.time.setText(basketList.get(position).getCurrentTime());
        holder.date.setText(basketList.get(position).getCurrentDate());
        holder.quantity.setText(basketList.get(position).getTotalQuantity());
        holder.totalPrice.setText(Integer.toString(basketList.get(position).getTotalPrice()));
        basketTotalPrice=basketTotalPrice+basketList.get(position).getTotalPrice();
        Intent intent=new Intent("basketTotalAmount");
        intent.putExtra("totalAmount",basketTotalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestr.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToCart")
                        .document(basketList.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    basketTotalPrice=0;
                                    basketList.remove(basketList.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();

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
        return basketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,time,quantity,totalPrice;
        ImageView imageView;
        ImageView deleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.basket_product_img);
            name=itemView.findViewById(R.id.productname);
            date=itemView.findViewById(R.id.current_date);
            time=itemView.findViewById(R.id.current_time);
            quantity=itemView.findViewById(R.id.total_quantity);
            totalPrice=itemView.findViewById(R.id.total_price);
            deleteItem=itemView.findViewById(R.id.deleteIcon);

        }
    }
}
