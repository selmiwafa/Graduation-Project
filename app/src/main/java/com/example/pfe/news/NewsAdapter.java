package com.example.pfe.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfe.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final List<News> news;
    private OnItemClickListener mListener;
    Context mContext;
    int id;

    public NewsAdapter(Context context, List<News> news) {
        this.news = news;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news1 = news.get(position);

        id = news1.getId();

        holder.edTitle.setText(news1.getTitle());
        holder.edContent.setText(news1.getContent());
        holder.edDate.setText(news1.getDate().toString());
    }

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    @Override
    public int getItemCount() {
        if (news != null) {
            return news.size();
        } else {
            return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView edDate;
        private final TextView edTitle, edContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edDate = itemView.findViewById(R.id.date);
            edTitle = itemView.findViewById(R.id.title);
            edContent = itemView.findViewById(R.id.content);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(news.get(position));
                    }
                }
            });
        }
    }
}