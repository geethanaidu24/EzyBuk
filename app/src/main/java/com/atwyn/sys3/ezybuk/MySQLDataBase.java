package com.atwyn.sys3.ezybuk;

import java.io.Serializable;

/**
 * Created by SYS3 on 6/7/2017.
 */

class MySQLDataBase implements Serializable {
    public int MoviesId;
    public String MoviesName;
    public String MoviesImageUrl;
    public String MoviesDescription;

    public MySQLDataBase() {

    }

    public int getMoviesId() {
        return MoviesId;
    }

    public void setMoviesId(int proid) {
        this.MoviesId = proid;
    }

    public String getMoviesName() {
        return MoviesName;
    }

    public void setMoviesName(String moviesName) {
        this.MoviesName = moviesName;
    }

    public String getMoviesImageUrl() {
        return MoviesImageUrl;
    }

    public void setMoviesImageUrl(String moviesImageUrl) {
        this.MoviesImageUrl = moviesImageUrl;
    }

 /*   public String getNewsDescription() {
        return NewsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.NewsDescription = newsDescription;
    }*/
}