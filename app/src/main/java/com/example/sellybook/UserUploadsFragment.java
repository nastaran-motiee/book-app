package com.example.sellybook;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sellybook.model.Book;
import com.example.sellybook.model.Model;
import com.example.sellybook.model.ModelFireBase;
import com.example.sellybook.ui.AllBooksFragmentDirections;
import com.squareup.picasso.Picasso;


public class UserUploadsFragment extends Fragment {
    public UserUploadedBooksFragmentViewModel viewModel;
    ProgressBar progressBar;
    UserBooksAdapter adapter;
    RecyclerView userBooksList;
    View view;
    SwipeRefreshLayout swipeRefresh;
    PopupMenu popupMenu;



    public UserUploadsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserUploadedBooksFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_user_uploads, container, false);
        userBooksList = view.findViewById(R.id.user_books_recyclerView);
        userBooksList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userBooksList.setLayoutManager(layoutManager);


        progressBar = view.findViewById(R.id.user_books_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new UserBooksAdapter();
        userBooksList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                Book book = viewModel.getData().getValue().get(position);
                NavDirections action = UserUploadsFragmentDirections.actionUserUploadsFragmentToBookInfoFragment(book.getId());
                Navigation.findNavController(view).navigate(action);
                Log.d("TAG", "row was clicked " + viewModel.getData().getValue().get(position).getId());
            }
        });

        swipeRefresh = view.findViewById(R.id.user_books_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ModelFireBase.instance.getUserUploadedBooks((bookLinkedList)->{
                    refreshData();

                });

            }

        });

        setHasOptionsMenu(true);


        if(viewModel.getData() == null){
            refreshData();
        }

        viewModel.getData().observe(getViewLifecycleOwner(), (userBooksList)->{
            adapter.notifyDataSetChanged();
        });

        return view;

    }

    private void refreshData() {
        swipeRefresh.setRefreshing(false);


    }

    //---------------------------------------------------------------------------------------------
    class UserBooksViewHolder extends RecyclerView.ViewHolder {
        private final OnItemClickListener listener;
        TextView bookNameTv;
        TextView priceTv;
        CheckBox cb;
        ImageView image;
        ImageView imageMore;

        public UserBooksViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);


            bookNameTv = itemView.findViewById(R.id.list_row_book_name_text_view);
            priceTv = itemView.findViewById(R.id.list_row_price_text_view);
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
         //  cb.setOnClickListener(new View.OnClickListener() {
         //      @Override
         //      public void onClick(View v) {
         //          int pos = getAdapterPosition();
         //          Book book = viewModel.getData().getValue().get(pos);
         //          if(cb.isChecked()){
         //              book.setFlag(true);
         //              Model.instance.addBookToUsersFavorites(new Book(book.getName(),book.getId(),book.getPrice(), book.getAuthor(),book.getPhone(),cb.isChecked(),book.getBookImageURL()),()->{
         //                  Log.d("tag", book.getName()+"added to favorites");
         //              });
         //          }else{
         //              book.setFlag(false);
         //              Model.instance.deleteBookFromUsersFavorites(book,()->{
         //                  Log.d("tag", book.getName()+"deleted from favorites");
         //              });
         //          }


         //      }
         //  });
            imageMore =itemView.findViewById(R.id.user_uploaded_list_row_pop_up_more);
            imageMore.setOnClickListener(v -> {
                popupMenu = new PopupMenu(imageMore.getContext(),imageMore);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.pop_up_delete:
                                int pos = getAdapterPosition();
                                Book book = viewModel.getData().getValue().get(pos);
                                ModelFireBase.instance.deleteBook(book,()->{
                                    Model.instance.reloadAllBooksList();

                                });
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popupMenu.show();
            });


        }


        public void bind(Book book) {
            bookNameTv.setText(book.getName());
            priceTv.setText(book.price);
           // cb.setChecked(book.isFlag());
            String url = book.getBookImageURL();
            if(url !=null ){
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

    public class UserBooksAdapter extends RecyclerView.Adapter<UserBooksViewHolder> {
        private OnItemClickListener mlistener;

        void setOnItemClickListener(OnItemClickListener listener) {
            this.mlistener = listener;
        }

        @NonNull
        @Override
        public UserBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.user_uploaded_list_row, parent, false);
            UserBooksViewHolder viewHolder = new UserBooksViewHolder(rowView,mlistener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserBooksViewHolder holder, int position) {
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