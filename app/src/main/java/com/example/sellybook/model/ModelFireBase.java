package com.example.sellybook.model;

import com.google.firebase.auth.FirebaseAuth;

public class ModelFireBase {
    public final static ModelFireBase instance = new ModelFireBase();

    private ModelFireBase(){

    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }
}
