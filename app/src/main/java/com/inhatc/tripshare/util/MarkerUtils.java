package com.inhatc.tripshare.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inhatc.tripshare.R;

public final class MarkerUtils {

    private MarkerUtils() {
    }

    public static BitmapDescriptor createNumberedMarker(Context context, int number) {
        int size = 80;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.purple_primary));
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, circlePaint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(context, R.color.white));
        textPaint.setTextSize(36);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);

        String text = String.valueOf(number);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, size / 2f, size / 2f + bounds.height() / 2f, textPaint);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
