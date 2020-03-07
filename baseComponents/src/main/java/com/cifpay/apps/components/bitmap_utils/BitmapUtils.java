package com.cifpay.apps.components.bitmap_utils;

import android.graphics.*;

/**
 * Created by zhaoyun
 * 2014/6/5.
 */
public class BitmapUtils {

    public static Bitmap roundCorner(Bitmap original, int roundPixels) {

        final int width = original.getWidth();
        final int height = original.getHeight();

        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        final int anyColor = 0xffabcdef;
        paint.setColor(anyColor);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(original, rect, rect, paint);

        return result;
    }

    public static Bitmap clipCenterSquare(Bitmap original) {

        final int width = original.getWidth();
        final int height = original.getHeight();
        final int squareSide = Math.min(width, height);

        return Bitmap.createBitmap(
                original,
                (width - squareSide) / 2,
                (height - squareSide) / 2,
                squareSide,
                squareSide
        );
    }

    public static Bitmap clipCenterSquare(Bitmap original, float squareSide) {

        final int width = original.getWidth();
        final int height = original.getHeight();
        final float scale = Math.max(squareSide / width, squareSide / height);

        Bitmap scaled = BitmapUtils.scale(original, scale);
        Bitmap result = clipCenterSquare(scaled);

        scaled.recycle();

        return result;
    }

    public static Bitmap createRoundCornerSquare(Bitmap original, float squareSide, int roundPixels) {

        Bitmap centerSquare = clipCenterSquare(original, squareSide);

        Bitmap result = roundCorner(centerSquare, roundPixels);
        centerSquare.recycle();

        return result;
    }

    public static Bitmap scale(Bitmap original, float scale) {

        final int width = original.getWidth();
        final int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
    }
}
