package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.cse234termproject.Model.UserModel;
import com.example.cse234termproject.Model.CategoryAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView quantity;
    private int totalQuantity=1;
    private int totalPrice=0;
    private ImageView detailedImg;
    private TextView price,description;
    private Button addToCart;
    private ImageView addItem,removeItem;
    private ImageView addToFavourites;
    private CategoryAllModel categoryAllModel = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof CategoryAllModel) {
            categoryAllModel = (CategoryAllModel) object;
        }
        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        price = findViewById(R.id.detailed_price);
        description = findViewById(R.id.detailed_description);
        if (categoryAllModel != null) {
            Glide.with(getApplicationContext()).load(categoryAllModel.getImg_url()).into(detailedImg);
            description.setText(categoryAllModel.getDescription());
            price.setText("Price : $" + Integer.toString(categoryAllModel.getPrice()));
            totalPrice = categoryAllModel.getPrice() * totalQuantity;
        }


        addToCart = findViewById(R.id.button_add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 10) {
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = categoryAllModel.getPrice() * totalQuantity;
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = categoryAllModel.getPrice() * totalQuantity;
                }
            }
        });
        addToFavourites = findViewById(R.id.favourite__border_24);
        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToFavourites();
            }
        });

      /*  FavoritesModel favoritesModel=new FavoritesModel(categoryAllModel.getName(),categoryAllModel.getImg_url(),categoryAllModel.getDescription(),categoryAllModel.getPrice());
        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToFavourites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).
                                collection("AddToCart").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.equals(description)){
                                   addToFavourites.setImageResource(R.drawable.ic_baseline_favorite_24);
                                }
                            }
                        });
                    }
                } else {
                }
            }
        });*/
    }

    private void addedToCart(){
        String saveCurrentDate,saveCurrentTime;
        Calendar calendarForDate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate=currentDate.format(calendarForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendarForDate.getTime());

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("productName", categoryAllModel.getName());
        cartMap.put("productImg", categoryAllModel.getImg_url());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);
        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToCart")
                .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(ProductDetailActivity.this, "Added", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void addedToFavourites(){
        final HashMap<String,Object> favouritesMap=new HashMap<>();
        favouritesMap.put("favouritesName", categoryAllModel.getName());
        favouritesMap.put("favouritesImg", categoryAllModel.getImg_url());
        favouritesMap.put("favouritesDescription", categoryAllModel.getDescription());
        favouritesMap.put("favouritesPrice", categoryAllModel.getPrice());
        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToFavourites")
                .add(favouritesMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(ProductDetailActivity.this, "Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel user = task.getResult().getValue(UserModel.class);
                    TextView userName=findViewById(R.id.UserName);
                    userName.setText(user.getName()+" "+user.getSurname());
                    userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_24, 0, 0, 0);
                }
            }
        });

    }
}