package com.fvbox.util.blurry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Copyright (C) 2020 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Blurry {

    private static final String TAG = Blurry.class.getSimpleName();

    public static Composer with(Context context) {
        return new Composer(context);
    }

    public static class Composer {

        private final View blurredView;
        private final Context context;
        private final BlurFactor factor;

        public Composer(Context context) {
            this.context = context;
            blurredView = new View(context);
            blurredView.setTag(TAG);
            factor = new BlurFactor();
        }

        public Composer radius(int radius) {
            factor.radius = radius;
            return this;
        }

        public Composer sampling(int sampling) {
            factor.sampling = sampling;
            return this;
        }

        public Composer color(int color) {
            factor.color = color;
            return this;
        }

        public BitmapComposer from(Bitmap bitmap) {
            return new BitmapComposer(context, bitmap, factor);
        }

        public BitmapComposer from(File file) {
            return from(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

    }

    public static class BitmapComposer {

        private final Context context;
        private final Bitmap bitmap;
        private final BlurFactor factor;

        public BitmapComposer(Context context, Bitmap bitmap, BlurFactor factor) {
            this.context = context;
            this.bitmap = bitmap;
            this.factor = factor;
        }

        public void into(final ImageView target) {
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap());
            target.setImageDrawable(drawable);
        }

        public Bitmap bitmap() {
            factor.width = bitmap.getWidth();
            factor.height = bitmap.getHeight();

            return Blur.of(context, bitmap, factor);
        }
    }

}
