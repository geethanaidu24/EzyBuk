package com.atwyn.sys3.ezybuk;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SYS3 on 6/7/2017.
 */

class MySQLDataBase implements Serializable {
    public int MovieId;
    public String Title;
    public String PosterUrl,BigPosterUrl;
  //  public String MoviesDescription;
    public String Genre;
    public String MLanguage;
    public String Releasing_Date;
public String Format,Synopsis,Duration_min,Videourl;

    public String CastName,CastRole,CastImgUrl;
    public MySQLDataBase() {

    }

    public int getMovieId() {
        return MovieId;
    }

    public void setMovieId(int movieId) {
        this.MovieId = movieId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getPosterUrl() {
        return PosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.PosterUrl = posterUrl;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        this.Genre = genre;
    }

    public String getMLanguage() {
        return MLanguage;
    }

    public void setMLanguage(String mLanguage) {
        this.MLanguage = mLanguage;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {this.Format = format;}


    public String getSynopsis() {
        return Synopsis;
    }

    public void setSynopsis(String synopsis) {this.Synopsis = synopsis;}


    public String getDuration_min() {
        return Duration_min;
    }

    public void setDuration_min(String duration_min) {this.Duration_min = duration_min;}


    public String getVideourl() {
        return Videourl;
    }

    public void setVideourl(String videourl) {this.Videourl = videourl;}

    public String getReleasing_Date() {
        return Releasing_Date;
    }

    public void setReleasing_Date(String releasing_date) {this.Releasing_Date = releasing_date;}

    public String getBigPosterUrl() {
        return BigPosterUrl;
    }

    public void setBigPosterUrl(String bigPosterUrl) {this.BigPosterUrl = bigPosterUrl;}

    public String getCastName() {
        return CastName;
    }

    public void setCastName(String castName) {this.CastName = castName;}

    public String getCastRole() {
        return CastRole;
    }

    public void setCastRole(String castRole) {this.CastRole = castRole;}

    public String getCastImgUrl() {
        return CastImgUrl;
    }

    public void setCastImgUrl(String castImgUrl) {this.CastImgUrl = castImgUrl;}

}