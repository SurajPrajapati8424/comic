 package com.example.comic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comic.Adapter.MyComicAdapter;
import com.example.comic.Adapter.MySliderAdapter;
import com.example.comic.Common.Common;
import com.example.comic.Interface.BannerLoadDone;
import com.example.comic.Interface.ComicLoadDone;
import com.example.comic.Model.Comic;
import com.example.comic.Services.PicassoLoadingServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

 public class MainActivity extends AppCompatActivity implements BannerLoadDone, ComicLoadDone {
    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView txt_comic;
    ImageView btn_filter_serch;

    //
     DatabaseReference banners , comics;

     BannerLoadDone bannerListener;
     ComicLoadDone comicListener;
     android.app.AlertDialog alertDialog;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slider=(Slider)findViewById(R.id.slider);
        Slider.init(new PicassoLoadingServices());

        //dtatabase coding
        banners= FirebaseDatabase.getInstance().getReference("Banners" );
        comics=FirebaseDatabase.getInstance().getReference("Comic");

        //interface
        bannerListener= this;
        comicListener= this;

        btn_filter_serch =(ImageView)findViewById(R.id.btn_show_filter_search);
        btn_filter_serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
            }
        });


        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_to_refesh);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary,
                R.color.design_default_color_primary_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lordBanner();
                lordComic();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                lordBanner();
                lordComic();
            }
        });

        recycler_comic=(RecyclerView)findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this,2));

        txt_comic=(TextView)findViewById(R.id.txt_comic);
    }

     private void lordComic() {
          alertDialog = new SpotsDialog.Builder().setContext(this)
                 .setCancelable(false)
                 .setMessage("Please wait .........")
                 .build();
          if(!swipeRefreshLayout.isRefreshing())
         alertDialog.show();

        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Comic> comic_load = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot comicSnapShot:snapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }
                comicListener.onComicLoadDoneListnner(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
     }

     private void lordBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> bannerList=new ArrayList<>();
                for (DataSnapshot bannerSnapShot:snapshot.getChildren())
                {
                    String image =bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }
                // call listener
                bannerListener.onBannnerLoadDoneListnner(bannerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage() ,Toast.LENGTH_SHORT).show();
            }
        });
     }

     @Override
     public void onBannnerLoadDoneListnner(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));
     }

     @Override
     public void onComicLoadDoneListnner(List<Comic> comicList) {
         Common.comicList=comicList;

         recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(),comicList) );
         txt_comic.setText(new StringBuilder("NEW COMIC (")
                 .append(comicList.size())
                 .append(")"));
         if (!swipeRefreshLayout.isRefreshing())
             alertDialog.dismiss();




     }
 }