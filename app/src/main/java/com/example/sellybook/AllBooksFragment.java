package com.example.sellybook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sellybook.interfaces.OnItemClickListener;
import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.example.sellybook.adapters.BookListAdapter;

import java.util.List;

public class AllBooksFragment extends Fragment {
    //TODO:check if there is a need for this variable
    List<Book> data;
    //TODO:++++++++++++++++++++++++++++

    BookListAdapter adapter;
    RecyclerView allBooksList;
    View view;


    public AllBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = Model.instance.getAllBooks();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_books, container, false);
        allBooksList = view.findViewById(R.id.all_books_recyclerView);
        allBooksList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        allBooksList.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(data,inflater);

        if(data != null){
            allBooksList.setAdapter(adapter);
        }


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG", "onItemClick: "+position);
            }
        });

        return view;
    }
//---------------------------------------------------------------------------------------------
//    static class MyViewHolder extends RecyclerView.ViewHolder{
//        private final OnItemClickListener listener;
//        TextView bookNameTv;
//        TextView priceTv;
//        CheckBox cb;
//        ImageView image;
//        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
//            super(itemView);
//            bookNameTv = itemView.findViewById(R.id.list_row_book_name_text_view);
//            priceTv = itemView.findViewById(R.id.list_row_price_text_view);
//            cb = itemView.findViewById(R.id.list_row_cb);
//            image = itemView.findViewById(R.id.list_row_imageView);
//            this.listener = listener;
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(listener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onItemClick(position);
//                        }
//
 //                   }
//
 //               }
 //           });
 //           cb.setOnClickListener(new View.OnClickListener() {
 //               @Override
 //               public void onClick(View v) {
 //                   int pos = getAdapterPosition();
 //                   //TODO:+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                   //Book book = Model.instance.getBooktById(idTv.getText().toString());
//                  // Model.instance.updateStudent(new Student(student.name,student.id,student.phone,student.address,cb.isChecked()));

//               }
//           });
//       }

//       public void bind(Book book) {
 //           bookNameTv.setText(book.name);
//           priceTv.setText(book.price);
//           cb.setChecked(book.cb);



//       }
//   }

//   public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
//       private OnItemClickListener mlistener;
//       void setOnItemClickListener(OnItemClickListener listener){
//           this.mlistener = listener;
//       }

//       @NonNull
//       @Override
//       public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//           LayoutInflater inflater = getLayoutInflater();
//           View rowView = inflater.inflate(R.layout.books_list_row,parent,false);
//           MyViewHolder viewHolder = new MyViewHolder(rowView, mlistener);
//           return viewHolder;
//       }

//       @Override
//       public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//           Book book = data.get(position);
//           holder.bind(book);
//       }

//       @Override
//       public int getItemCount() {
//           return data.size();
//       }
  //  }

}