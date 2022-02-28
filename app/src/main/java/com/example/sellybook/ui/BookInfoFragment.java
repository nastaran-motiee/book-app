package com.example.sellybook.ui;

import android.graphics.BlendMode;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.TintableBackgroundView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sellybook.R;
import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.squareup.picasso.Picasso;

import java.util.List;

//TODO:+++++++++++++++++++++++++++
public class BookInfoFragment extends Fragment {
    View view;
    TextView name;
    TextView phone;
    TextView price;
    TextView address;
    TextView author;
    CheckBox cb;
    String id;
    List<Book> data;
    Button editBtn;
    Book book;
    ProgressBar progressBar;
    ImageView image;



    public BookInfoFragment() {
        // Required empty public constructor
    }

    public static BookInfoFragment newInstance(String param1, String param2) {
        BookInfoFragment fragment = new BookInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      view = inflater.inflate(R.layout.fragment_book_info, container, false);
      //TODO)))))))))))))))))))))))))))))))
      String bookId = BookInfoFragmentArgs.fromBundle(getArguments()).getBookId();
      name = ((TextView) view.findViewById(R.id.book_info_name_tv));
      price = ((TextView) view.findViewById(R.id.book_info_price_tv));
      phone = ((TextView) view.findViewById(R.id.book_info_phone_tv));
      address = ((TextView) view.findViewById(R.id.book_info_address_tv));
      author = ((TextView) view.findViewById(R.id.book_info_author_tv));
      cb = ((CheckBox) view.findViewById(R.id.book_info_check_box));
      progressBar = view.findViewById(R.id.book_info_progress_bar);
      image = view.findViewById(R.id.book_info_image_view);
      progressBar.setVisibility(view.VISIBLE);
      Model.instance.getBookById(bookId, (book)->{
            updateDisplay(book);
      });

        //TODO:++++++++++++++++++++++++++++++++++++++

      if (book != null){
        updateDisplay(book);
      }
      //editBtn = view.findViewById(R.id.student_details_Edit_Button);
      //editBtn.setOnClickListener(new View.OnClickListener() {
      //    @Override
      //    public void onClick(View v) {
      //        NavDirections action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToEditStudentDetails(studentId);
      //        Navigation.findNavController(view).navigate(action);
      //    }
      //});


        return view;
    }


    public void updateDisplay(Book book){
        this.book = book;
        cb.setChecked(book.cb);
        phone.setText(book.phone);
        name.setText(book.name);
        price.setText(book.price);
        author.setText(book.author);
        if(book.getBookImageURL() != null){
            Picasso.get()
                    .load(book.getBookImageURL())
                    .placeholder(R.drawable.book_character_image)
                    .into(image);
        }
        progressBar.setVisibility(View.GONE);
    }

}