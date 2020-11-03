package com.example.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.setData(categoryModelList.get(position).getImageUrl(), categoryModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView categoryTitleTextView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            categoryTitleTextView = itemView.findViewById(R.id.categoryTitle);
        }

        private void setData(String url, final String title) {
            Glide.with(itemView.getContext()).load(url).into(imageView);
            this.categoryTitleTextView.setText(title);

            itemView.setOnClickListener(v -> {
                Intent setIntent = new Intent(itemView.getContext(), QuestionActivity.class);
                setIntent.putExtra("category_name", title);
                itemView.getContext().startActivity(setIntent);
            });
        }
    }
}
