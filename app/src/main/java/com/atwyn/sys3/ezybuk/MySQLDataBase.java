package com.atwyn.sys3.ezybuk;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SYS3 on 6/7/2017.
 */

class MySQLDataBase implements Serializable {
    public int MovieId,TheaterId,ScreenId,LayoutId,Rows,Columns,TrailerId;
    public String Title;
    public String SmallPosterUrl,BigPosterUrl;
  //  public String MoviesDescription;
    public String TheaterName,TrailerName,TrailerImageUrl,TrailerVideo;
    public String Genre;
    public String MLanguage;
    public String Releasing_Date,ShowDate,ShowTime,Certification;
public String Format,Synopsis,Duration_min,Videourl;
public String EmailId,UserName,UserMobileNo,UserDOB,UserGender;
    public String CastName,CastRole,CastImgUrl;
    public String CrewName,CrewRole,CrewImgUrl;

    public String SeatNos,RowNames;
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

    public String getSmallPosterUrl() {
        return SmallPosterUrl;
    }

    public void setSmallPosterUrl(String smallPosterUrl) {
        this.SmallPosterUrl = smallPosterUrl;
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

    public int getTheaterId() {
        return TheaterId;
    }

    public void setTheaterId(int theaterId) {
        this.TheaterId = theaterId;
    }
    public String getTheaterName() {
        return TheaterName;
    }

    public void setTheaterName(String theaterName) {this.TheaterName = theaterName;}

    public String getShowDate() {
        return ShowDate;
    }

    public void setShowDate(String showDate) {this.ShowDate = showDate;}

    public String getShowTime() {
        return ShowTime;
    }

    public void setShowTime(String showTime) {this.ShowTime = showTime;}

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {this.EmailId = emailId;}
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {this.UserName = userName;}

    public String getUserMobileNo() {
        return UserMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {this.UserMobileNo = userMobileNo;}


    public int getScreenId() {
        return ScreenId;
    }

    public void setScreenId(int screenId) {
        this.ScreenId = screenId;
    }

    public int getLayoutId() {
        return LayoutId;
    }
    public void setLayoutId(int layoutId) {
        this.LayoutId = layoutId;
    }

    public int getRows() {
        return Rows;
    }
    public void setRows(int rows) {
        this.Rows = Rows;
    }


    public int getColumns() {
        return Columns;
    }
    public void setColumns(int columns) {
        this.Columns = columns;
    }

    public String getSeatNos() {
        return SeatNos;
    }
    public void setSeatNos(String seatNos) {this.SeatNos = seatNos;}

    public String getRowNames() {
        return RowNames;
    }
    public void setRowNames(String rowNames) {this.RowNames = rowNames;}

    public String getCertification() {
        return Certification;
    }
    public void setCertification(String certification) {this.Certification = certification;}

    public int getTrailerId() {
        return TrailerId;
    }

    public void setTrailerId(int trailerId) {
        this.TrailerId = trailerId;
    }

    public String getTrailerName() {
        return TrailerName;
    }
    public void setTrailerName(String trailerName) {this.TrailerName = trailerName;}

    public String getTrailerImageUrl() {
        return TrailerImageUrl;
    }
    public void setTrailerImageUrl(String trailerImageUrl) {this.TrailerImageUrl = trailerImageUrl;}

    public String getTrailerVideo() {
        return TrailerVideo;
    }
    public void setTrailerVideo(String trailerVideo) {this.TrailerVideo = trailerVideo;}


}