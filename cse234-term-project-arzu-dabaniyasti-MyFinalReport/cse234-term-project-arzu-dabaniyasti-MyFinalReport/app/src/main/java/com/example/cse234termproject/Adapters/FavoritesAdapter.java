package com.example.cse234termproject.Adapters;

import com.bumptech.glide.Glide;
import com.example.cse234termproject.Model.FavoritesModel;
import com.example.cse234termproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>  {
    Context context;
    List<FavoritesModel> favouritestList;
    private FirebaseFirestore firestr;
    private FirebaseAuth mAuth;


    public FavoritesAdapter(Context context, List<FavoritesModel> favouritestList) {
        this.context = context;
        this.favouritestList= favouritestList;
        firestr=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(favouritestList.get(position).getFavouritesImg()).into(holder.imageView);
        holder.name.setText(favouritestList.get(position).getFavouritesName());
        holder.price.setText(Integer.toString(favouritestList.get(position).getFavouritesPrice()));
        holder.description.setText(favouritestList.get(position).getFavouritesDescription());
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestr.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToFavourites")
                        .document(favouritestList.get(position).getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            favouritestList.remove(favouritestList.get(position));
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
        return favouritestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,description;
        ImageView imageView;
        ImageView removeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.favourites_img);
            name=itemView.findViewById(R.id.favourites_name);
            price=itemView.findViewById(R.id.favourites_price);
            description=itemView.findViewById(R.id.favourites_description);
            removeItem=itemView.findViewById(R.id.removeFavourites);

        }
    }
}
