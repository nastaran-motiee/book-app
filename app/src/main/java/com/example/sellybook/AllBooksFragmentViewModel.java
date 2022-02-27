package com.example.sellybook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;

import java.util.List;

public class AllBooksFragmentViewModel extends ViewModel {
    LiveData<List<Book>> data = Model.instance.getAll();


    public LiveData<List<Book>> getData() {
        return data;
    }


}
