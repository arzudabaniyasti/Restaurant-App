package com.example.cse234termproject.Adapters;

import com.example.cse234termproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.util.List;

public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>  {
    Context context;
    List<String> quantityList,productNameList,priceList,timeList,dateList;
    List<Integer> productNameCountList,productQuantityCountList;
    String productString ="";
    String quantityString="";
    String orderDate,orderTime;

    public PreviousOrderAdapter(Context context,List<String> dateList, List<String> priceList, List<String> timeList,List<String> productNameList, List<String> quantityList, List<Integer> productNameCountList, List<Integer> productQuantityCountList) {
        this.context = context;
        this.priceList=priceList;
        this.dateList=dateList;
        this.timeList=timeList;
        this.productNameList= productNameList;
        this.quantityList=quantityList;
        this.productNameCountList=productNameCountList;
        this.productQuantityCountList=productQuantityCountList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousOrderAdapter.ViewHolder holder, int position) {
        holder.totalBasket.setText("Total Price: "+priceList.get(position));
        holder.time.setText("Date: "+dateList.get(position)+"\n"+"Time: "+timeList.get(position));
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

    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalBasket,time,product,productQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            totalBasket=itemView.findViewById(R.id.pasttotal_price_order);
            product=itemView.findViewById(R.id.pastorderProducts);
            productQuantity=itemView.findViewById(R.id.pastorderProductsQuantity);
            time=itemView.findViewById(R.id.datetimepastOrder);

        }
    }
}


