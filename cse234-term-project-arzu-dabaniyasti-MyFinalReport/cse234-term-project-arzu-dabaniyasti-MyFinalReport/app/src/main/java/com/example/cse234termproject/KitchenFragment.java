package com.example.cse234termproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse234termproject.Adapters.CategoriesAdapter;
import com.example.cse234termproject.Model.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


public class KitchenFragment extends Fragment {
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    List<CategoryModel> categoryList;
    CategoriesAdapter kitchenCatagoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.scroll_kitchen_category);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        categoryList = new ArrayList<>();
        kitchenCatagoriesAdapter = new CategoriesAdapter(getActivity(), categoryList);
        recyclerView.setAdapter(kitchenCatagoriesAdapter);

        db.collection("Catagories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CategoryModel kitchenCategory = document.toObject(CategoryModel.class);
                        categoryList.add(kitchenCategory);
                        kitchenCatagoriesAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }
}