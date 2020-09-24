package com.guillot.go4lunch.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.google.android.gms.maps.model.LatLng;

public class Utils {

    /**
     * Resize a given bitmap.
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     * Convert user location to string.
     *
     * @param position
     * @return
     */
    public static String convertLocationForApi(LatLng position) {
        if (position != null) {
            double lat = position.latitude;
            double lng = position.longitude;
            return lat + "," + lng;
        }
        return null;
    }
}
