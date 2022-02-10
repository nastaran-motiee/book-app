package com.example.sellybook.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModelFireBase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final static ModelFireBase instance = new ModelFireBase();

    private ModelFireBase(){}

    public FirebaseAuth getFireBaseAuthInstance() {
         return FirebaseAuth.getInstance();
    }


}
