package com.example.cse234termproject.Adapters;

import com.example.cse234termproject.Model.OrderModel;
import com.example.cse234termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.ViewHolder>  {
    Context context;
    List<OrderModel> orderList;
    private FirebaseFirestore firestr;
    private FirebaseAuth mAuth;
    List<String> dateList,timelist,quantityList,productNameList,priceList;
    List<Integer> productNameCountList,productQuantityCountList;
    String productString ="";
    String quantityString="";
    String orderDate,orderTime;

    public CurrentOrderAdapter(Context context,List<String> dateList, List<String> priceList,List<String> timelist,List<String> productNameList,List<String> quantityList,List<Integer> productNameCountList,List<Integer> productQuantityCountList) {
        this.context = context;
        this.timelist= timelist;
        this.dateList= dateList;
        this.priceList=priceList;
        this.productNameList= productNameList;
        this.quantityList=quantityList;
        this.productNameCountList=productNameCountList;
        this.productQuantityCountList=productQuantityCountList;
        firestr=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderAdapter.ViewHolder holder, int position) {
        holder.time.setText(timelist.get(position));
        holder.totalBasket.setText("Total Price: "+priceList.get(position));
        int a=0;
        int b=0;
        for(int i=0;i<=position;i++){
            a+=productNameCountList.get(i);
            if (i!=0) {
                b += productNameCountList.get(i-1);
            }
        }

        for(int i=b;i<a;i++){
            productString=productString+productNameList.get(i)+"\n";
        }
        holder.product.setText(productString);
        productString="";
        for(int i=b;i<a;i++){
            quantityString+=" "+quantityList.get(i)+" pcs \n";
        }
        holder.productQuantity.setText(quantityString);
        quantityString="";
        orderDate = (dateList.get(position));
        orderTime = (timelist.get(position));

        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        /*SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        String orderDateInt=orderDate.replaceAll("\\s","");
        String orderTimeInt=orderTime.replaceAll("\\s","");
        String orderTimeInt2=orderTimeInt.replaceAll("PM","");
        String orderTimeInt3=orderTimeInt2.replaceAll(":","");*/
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        if(!orderDate.equals(saveCurrentDate)) {
            movePastOrder(position, String.valueOf(priceList.get(position)));
        }

    }
    private void movePastOrder(int position,String totaltext) {

        final HashMap<String, Object> orderMap = new HashMap<>();
        final HashMap<String, Object> productMap = new HashMap<>();

        String a=mAuth.getCurrentUser().getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getInstance().getReference().child("Users").child(a).child("Order List");
        for(int i=0;i<productNameList.size();i++) {
            productMap.put("ProductName", productNameList.get(i));
            productMap.put("quantity", quantityList.get(i));
            String productID = productNameList.get(i);
            String orderID = "order time: "+orderDate+" "+orderTime;
            databaseReference.child("Past Orders").child(orderID).child("Products").child(productID)
                    .updateChildren(productMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }
        orderMap.put("date",orderDate);
        orderMap.put("time",orderTime);
        orderMap.put("totalBasket",totaltext);

        String orderID = "order time: "+orderDate+" "+orderTime;
        databaseReference.child("Past Orders").child(orderID).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        databaseReference.child("Current Orders").child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                }
            }
        });
    }

    private void remove(int position) {
        String orderID = "order time: "+orderDate+" "+orderTime;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getInstance().getReference().child("Users")
                .child(mAuth.getCurrentUser().getUid()).child("Order List");
        databaseReference.child("Current Orders").child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"Order Cancelled",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return timelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalBasket,time,product,productQuantity,cancelOrder;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.order_remaining_time);
            totalBasket=itemView.findViewById(R.id.total_price_order);
            product=itemView.findViewById(R.id.orderProducts);
            productQuantity=itemView.findViewById(R.id.orderProductsQuantity);
            cancelOrder=itemView.findViewById(R.id.cancelOrder);

        }
    }
}


