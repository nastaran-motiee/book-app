package com.example.sellybook.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellybook.R;
import com.example.sellybook.interfaces.OnItemClickListener;
import com.example.sellybook.model.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    List<Book> data;
    private final LayoutInflater mInflater;
    //--------------------------------------------
    private OnItemClickListener mlistener;

    public BookListAdapter(List<Book> data, LayoutInflater mInflater) {
        this.data = data;
        this.mInflater = mInflater;
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = mInflater.inflate(R.layout.books_list_row,parent,false);
        BookListViewHolder viewHolder = new BookListViewHolder(rowView, mlistener);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        Book book = data.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //-----------------------------------------
    static class BookListViewHolder extends RecyclerView.ViewHolder{
        private final OnItemClickListener listener;
        TextView bookNameTv;
        TextView priceTv;
        CheckBox cb;
        ImageView image;
        public BookListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            bookNameTv = itemView.findViewById(R.id.list_row_book_name_text_view);
            priceTv = itemView.findViewById(R.id.list_row_price_text_view);
            cb = itemView.findViewById(R.id.list_row_cb);
            image = itemView.findViewById(R.id.list_row_imageView);
            this.listener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }

                }
            });
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //TODO:+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    //Book book = Model.instance.getBooktById(idTv.getText().toString());
                    // Model.instance.updateStudent(new Student(student.name,student.id,student.phone,student.address,cb.isChecked()));

                }
            });
        }

        public void bind(Book book) {
            bookNameTv.setText(book.name);
            priceTv.setText(book.price);
            cb.setChecked(book.cb);



        }
    }

}