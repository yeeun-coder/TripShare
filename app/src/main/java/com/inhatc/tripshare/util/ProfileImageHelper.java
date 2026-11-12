package com.inhatc.tripshare.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.Uri;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.inhatc.tripshare.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ProfileImageHelper {

    private static final int MAX_SIZE = 400;

    private ProfileImageHelper() {
    }

    public static void loadInto(ImageView imageView, String profileImage) {
        applyCircularClip(imageView);
        if (profileImage != null && !profileImage.isBlank()) {
            try {
                String encoded = profileImage.contains(",")
                        ? profileImage.split(",", 2)[1]
                        : profileImage;
                byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    imageView.setBackground(null);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            } catch (Exception ignored) {
                // fall through
            }
        }
        imageView.setImageDrawable(null);
        imageView.setBackground(ContextCompat.getDrawable(imageView.getContext(),
                R.drawable.bg_profile_circle));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public static void loadCover(ImageView imageView, String coverUrl, int fallbackRes) {
        applyRoundedClip(imageView, 8f);
        if (coverUrl != null && !coverUrl.isBlank()) {
            try {
                String encoded = coverUrl.contains(",")
                        ? coverUrl.split(",", 2)[1]
                        : coverUrl;
                byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    return;
                }
            } catch (Exception ignored) {
                // fall through
            }
        }
        imageView.setImageResource(fallbackRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public static void applyRoundedClip(ImageView imageView, float cornerRadiusDp) {
        imageView.post(() -> {
            float radius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    cornerRadiusDp,
                    imageView.getResources().getDisplayMetrics());
            imageView.setClipToOutline(true);
            imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                }
            });
        });
    }

    public static void applyCircularClip(ImageView imageView) {
        imageView.post(() -> {
            imageView.setClipToOutline(true);
            imageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        });
    }

    public static String encodeUri(Context context, Uri uri) throws IOException {
        Bitmap original;
        try (InputStream input = context.getContentResolver().openInputStream(uri)) {
            if (input == null) {
                throw new IOException("Cannot open image");
            }
            original = BitmapFactory.decodeStream(input);
        }
        if (original == null) {
            throw new IOException("Invalid image");
        }

        Bitmap scaled = scaleDown(original);
        if (scaled != original) {
            original.recycle();
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, output);
        scaled.recycle();
        return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP);
    }

    private static Bitmap scaleDown(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= MAX_SIZE && height <= MAX_SIZE) {
            return bitmap;
        }
        float ratio = Math.min((float) MAX_SIZE / width, (float) MAX_SIZE / height);
        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}
