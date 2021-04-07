package com.example.comic.Interface;

import com.example.comic.Model.Comic;

import java.util.List;

public interface ComicLoadDone {
    void onComicLoadDoneListnner(List<Comic> comicList);

}
