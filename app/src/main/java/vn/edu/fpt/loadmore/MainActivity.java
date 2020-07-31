package vn.edu.fpt.loadmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.fpt.loadmore.model.GetModel;
import vn.edu.fpt.loadmore.model.Photo;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvList;
    private ListAdapter listAdapter;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String api_key = "90291cefd912b10eb9e9e08e2d28f60f";
    private String user_id = "187034571@N03";
    private String extras = "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o";
    private String format = "json";
    private String method = "flickr.favorites.getList";
    private String nojsoncallback = "1";
    private int per_page = 6;
    private int page = 1;
    private List<Photo> photoList;
    private List<Photo> photoList2;
    private int maxSize;
    private boolean isScrolling = false;
    private int totalItems;
    private boolean loadMore = true;
    private int position[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        rvList = findViewById(R.id.rvList);
        progressBar = findViewById(R.id.progressBar);

        photoList = new ArrayList<>();
        setLayoutManager();
        setAdapter();

        postRetrofit(1);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photoList = new ArrayList<>();
                setLayoutManager();
                setAdapter();
                page=1;
                postRetrofit(page);

                loadMore = true;
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                position = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                totalItems = staggeredGridLayoutManager.getItemCount();

                for (int i = 0; i < position.length; i++) {
                    if (i == 0) {
                        maxSize = position[i];
                    } else if (position[i] > maxSize) {
                        maxSize = position[i];
                    }
                }
                if (!isScrolling && maxSize == totalItems - 1 && loadMore) {
                    page++;
                    postRetrofit(page);
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                }

                if (isScrolling && maxSize == totalItems - 1 && loadMore) {
                    page++;
                    postRetrofit(page);
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    public void postRetrofit(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        retrofitService.postHttp(api_key, user_id, extras, format, method, nojsoncallback, per_page, page).enqueue(new Callback<GetModel>() {
            @Override
            public void onResponse(Call<GetModel> call, Response<GetModel> response) {

                photoList2 = new ArrayList<>();

                photoList2 = response.body().getPhotos().getPhoto();

                if (photoList2.size()==0){
                    loadMore = false;
                } else {
                    int currentSize = photoList.size();
                    photoList.addAll(photoList2);
                    listAdapter.notifyItemRangeInserted(currentSize,photoList.size()-1);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setLayoutManager() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(staggeredGridLayoutManager);
    }
    void setAdapter() {
        listAdapter = new ListAdapter(photoList, MainActivity.this);
        rvList.setAdapter(listAdapter);
    }
}