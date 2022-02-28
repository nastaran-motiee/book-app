package com.example.sellybook.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sellybook.AllBooksFragmentViewModel;
import com.example.sellybook.R;
import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllBooksFragment extends Fragment {
    AllBooksFragmentViewModel viewModel;
    ProgressBar progressBar;
    MyAdapter adapter;
    RecyclerView allBooksList;
    View view;
    SwipeRefreshLayout swipeRefresh;


    public AllBooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(AllBooksFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_books, container, false);
        allBooksList = view.findViewById(R.id.all_books_recyclerView);
        allBooksList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        allBooksList.setLayoutManager(layoutManager);


        progressBar = view.findViewById(R.id.all_books_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new MyAdapter();
        allBooksList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                Book book = viewModel.getData().getValue().get(position);
                NavDirections action = AllBooksFragmentDirections.actionAllBooksFragmentToBookInfoFragment(book.getId());
                Navigation.findNavController(view).navigate(action);
                Log.d("TAG", "row was clicked " + viewModel.getData().getValue().get(position).getId());
            }
        });

        swipeRefresh = view.findViewById(R.id.all_books_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }

        });

        setHasOptionsMenu(true);

        if(viewModel.getData() == null){
            refreshData();
        }

        viewModel.getData().observe(getViewLifecycleOwner(), (allBooksList)->{
            adapter.notifyDataSetChanged();
        });

        return view;

    }

    private void refreshData() {
      swipeRefresh.setRefreshing(false);


    //  Model.instance.getAllBooks(new Model.getAllBooksListener() {

    //      @SuppressLint("NotifyDataSetChanged")
    //      @Override
    //      public void onComplete(List<Book> d) {
    //          viewModel.setData(d);
    //          adapter.notifyDataSetChanged();
    //          progressBar.setVisibility(View.GONE);
    //          swipeRefresh.setRefreshing(false);

    //      }

    //  });
    }

    //---------------------------------------------------------------------------------------------
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final OnItemClickListener listener;
        TextView bookNameTv;
        TextView priceTv;
        CheckBox cb;
        ImageView image;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            bookNameTv = itemView.findViewById(R.id.list_row_book_name_text_view);
            priceTv = itemView.findViewById(R.id.list_row_price_text_view);
            cb = itemView.findViewById(R.id.list_row_cb);
            image = itemView.findViewById(R.id.list_row_imageView);
            this.listener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, v);

                        }
//
                    }
//
                }
            });
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //TODO:+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    Book book = Model.instance.getBktById(Model.instance.getBktById(String.valueOf(pos)).getId());
                    Model.instance.updateBook(new Book(book.name,book.id,book.price, book.author,book.phone,cb.isChecked()));

                }
            });


        }


        public void bind(Book book) {
            bookNameTv.setText(book.getName());
            priceTv.setText(book.price);
            cb.setChecked(book.isFlag());
            String url = book.getBookImageURL();
           if(url != null ){
               Picasso.get()
                       .load(url)
                       .placeholder(R.drawable.book_character_image)
                       .into(image);
           }


        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position, View view);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private OnItemClickListener mlistener;

        void setOnItemClickListener(OnItemClickListener listener) {
            this.mlistener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.books_list_row, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(rowView, mlistener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Book book = viewModel.getData().getValue().get(position);
            holder.bind(book);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() != null){
                return viewModel.getData().getValue().size();
            }

            return  0;
        }



    }
}