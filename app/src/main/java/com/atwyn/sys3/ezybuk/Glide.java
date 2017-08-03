package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by SYS3 on 7/25/2017.
 */

public class Glide {
    public static void downloadImage(Context c, String imageUrl, ImageView img) {
        if (imageUrl.length() > 0 && imageUrl != null) {
            //Picasso.with(c).load(imageUrl).placeholder(R.drawable.pageloader).into(img);
           /* Glide.with(c).load(imageUrl).centerCrop().crossFade().placeholder(R.drawable.cccc)
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);*/
            com.bumptech.glide.Glide.with(c).load(imageUrl).dontTransform()
                    .thumbnail(com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).crossFade().fitCenter())
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade().centerCrop()

                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        } else {
            //Picasso.with(c).load(R.mipmap.ic_launcher).into(img);
            // Glide.with(c).load(R.drawable.cccc).override(100,100).into(img);
            com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).dontTransform().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).crossFade().fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
        }
    }

    public static void downloadImage1(Context c, String imageUrl, ImageView img) {
        if (imageUrl.length() > 0 && imageUrl != null) {
            //Picasso.with(c).load(imageUrl).placeholder(R.drawable.pageloader).into(img);
           /* Glide.with(c).load(imageUrl).centerCrop().crossFade().placeholder(R.drawable.cccc)
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);*/
            com.bumptech.glide.Glide.with(c).load(imageUrl).dontTransform()
                    .thumbnail(com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).crossFade().fitCenter())

                    .crossFade().centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation(c, 15, 1,
                            RoundedCornersTransformation.CornerType.TOP))
                    //.bitmapTransform(new RoundedCornersTransformation(c,10,0))

                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        } else {
            //Picasso.with(c).load(R.mipmap.ic_launcher).into(img);
            // Glide.with(c).load(R.drawable.cccc).override(100,100).into(img);
            com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).dontTransform().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).crossFade().fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
        }
    }
    public static void downloadImage2(Context c, String imageUrl, ImageView img)
    {
        if(imageUrl.length()>0 && imageUrl!=null)
        {
            //Picasso.with(c).load(imageUrl).placeholder(R.drawable.pageloader).into(img);
           /* Glide.with(c).load(imageUrl).centerCrop().crossFade().placeholder(R.drawable.cccc)
                    .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);*/
            com.bumptech.glide.Glide.with(c).load(imageUrl).dontTransform()
                    .thumbnail(com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).crossFade().fitCenter())

                    .crossFade().centerCrop()
                    .bitmapTransform( new CropCircleTransformation(c))
                    //.bitmapTransform(new RoundedCornersTransformation(c,10,0))

                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        }else {
            //Picasso.with(c).load(R.mipmap.ic_launcher).into(img);
            // Glide.with(c).load(R.drawable.cccc).override(100,100).into(img);
            com.bumptech.glide.Glide.with(c).load(R.drawable.camaraposter).dontTransform() .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).crossFade().fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
        }
    }

}