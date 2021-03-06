package vn.edu.fpt.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.fpt.loadmore.model.Photo;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {
    private List<Photo> photoList;
    private Context context;

    public ListAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListHolder holder, final int position) {


        Glide.with(context).load(photoList.get(position).getUrlL()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}
