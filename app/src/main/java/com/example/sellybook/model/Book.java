package com.example.sellybook.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Book {
        public String id = "";
        public String name = "";
        public String price = "";
        public String writer = "";
        public boolean cb = false;
        //public ImageView selectImage;

        public Book(){}

        public Book(String name, String id, String price, String writer, boolean cb, ImageView selectImage){
            this.name = name;
            this.id = id;
            this.price = price;
            this.writer = writer;
            this.cb = cb;
            //this.selectImage = selectImage;
        }
        public Book(String name, String id, String price, String writer, boolean cb){
            this.name = name;
            this.id = id;
            this.price = price;
            this.writer = writer;
            this.cb = cb;
        }


        public String getId(){
            return this.id;
        }

    }
