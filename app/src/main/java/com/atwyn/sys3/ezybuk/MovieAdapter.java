package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SYS3 on 8/2/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static String moviesUrlAddress = Config.moviesUrlAddress;
String u;
    private Context context;
    private LayoutInflater inflater;
    List<MySQLDataBase> data= Collections.emptyList();
    MySQLDataBase current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public MovieAdapter(Context context, List<MySQLDataBase> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }



    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.grid_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        MySQLDataBase current=data.get(position);
        myHolder.textCastname.setText(current.CastName);
        myHolder.textCastrole.setText(current.CastRole);

     /*   myHolder.textCastname.setText(current.CastName+ "\n"+"Actor");
        myHolder.textCastrole.setText("As" + " "+ current.CastRole);*/

        // myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        // load image into imageview using glide
  /* Glide.with(context).load(Config.mainUrlAddress + current.CastImgUrl)
               // .placeholder(R.drawable.ic_img_error)
                //.error(R.drawable.ic_img_error)
                .into(myHolder.im);*/
     u=   Config.mainUrlAddress + current.CastImgUrl;
        Log.d("result image ", "> " + u);
        /*com.bumptech.glide.Glide.with(context).load(myHolder.im).dontTransform()
                .thumbnail(com.bumptech.glide.Glide.with(context).load(R.drawable.camaraposter).crossFade().fitCenter())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .crossFade().centerCrop()

                .diskCacheStrategy(DiskCacheStrategy.ALL).into(myHolder.im);*/
 Glide.downloadImage2(context,u,myHolder.im);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textCastname;
        TextView textCastrole;
       ImageView im;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textCastname= (TextView) itemView.findViewById(R.id.tv_species);
            textCastrole= (TextView) itemView.findViewById(R.id.tv_species1);
          im  = (ImageView) itemView.findViewById(R.id.img_thumbnail);

        }

    }


}