package com.example.comic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.comic.Adapter.MyChapterAdapter;
import com.example.comic.Common.Common;
import com.example.comic.Model.Comic;

public class ChaptersActivity extends AppCompatActivity {
    RecyclerView recycler_chapter;
    TextView txt_chapters_name;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        txt_chapters_name = findViewById(R.id.txt_chapter_name);
        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));


        Toolbar toolbar = findViewById(R.id.toolbaar);
        toolbar.setTitle(Common.comicSelected.Name);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });

        fetchChapter(Common.comicSelected);

    }

    private void fetchChapter(Comic comicSelected) {
        Common.chapterList =comicSelected.Chapters;
        if (Common.chapterList == null) return;
        recycler_chapter.setAdapter(new MyChapterAdapter(this,comicSelected.Chapters));
        txt_chapters_name.setText(String.format("CHAPTERS (%d)", comicSelected.Chapters.size()));
    }
}