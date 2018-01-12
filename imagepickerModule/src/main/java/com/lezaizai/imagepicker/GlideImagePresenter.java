/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.lezaizai.imagepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class GlideImagePresenter implements ImagePresenter{
    @Override
    public View onPresentImage(Context context, String imageUri, int size) {
        ImageView img = new ImageView(context);
        img.setBackgroundColor(Color.GRAY);
        GridView.LayoutParams params = new AbsListView.LayoutParams(size, size);
        img.setLayoutParams(params);
        Glide.with(context)
                .load(new File(imageUri))
                .apply(new RequestOptions()
                    .transform(new GlideRoundTransform(context, 10))
                    .centerCrop()
                    .dontAnimate()
                    .override(size/4*3, size/4*3)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img))
                .thumbnail(0.5f)
                .into(img);
        return img;
    }
    @Override
    public void onPresentImage(final ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .apply(new RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .override(size/4*3, size/4*3)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .transform(new GlideRoundTransform(imageView.getContext())))
                .thumbnail(0.5f)
                .into(imageView);

    }
    @Override
    public void onPresentWebImage(final ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                .load(imageUri)
                .apply(new RequestOptions()
                    .centerCrop()
                    .dontAnimate()
                    .override(size/4*3, size/4*3)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .transform(new GlideRoundTransform(imageView.getContext())))
                .thumbnail(0.5f)
                .into(imageView);

    }

}
