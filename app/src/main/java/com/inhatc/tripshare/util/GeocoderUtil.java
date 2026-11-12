package com.inhatc.tripshare.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class GeocoderUtil {

    private GeocoderUtil() {
    }

    public static LatLng geocode(Context context, String query) {
        if (query == null || query.isBlank()) {
            return defaultCenter();
        }
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> results = geocoder.getFromLocationName(query, 1);
            if (results != null && !results.isEmpty()) {
                Address address = results.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException ignored) {
            // fall through
        }
        return defaultCenter();
    }

    public static String reverseGeocode(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> results;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // synchronous fallback for simplicity on older pattern
                results = geocoder.getFromLocation(lat, lng, 1);
            } else {
                results = geocoder.getFromLocation(lat, lng, 1);
            }
            if (results != null && !results.isEmpty()) {
                Address address = results.get(0);
                if (address.getFeatureName() != null && address.getLocality() != null) {
                    return address.getFeatureName() + ", " + address.getLocality();
                }
                if (address.getFeatureName() != null) {
                    return address.getFeatureName();
                }
                if (address.getAddressLine(0) != null) {
                    return address.getAddressLine(0);
                }
            }
        } catch (IOException ignored) {
            // fall through
        }
        return String.format(Locale.KOREA, "%.5f, %.5f", lat, lng);
    }

    public static LatLng defaultCenter() {
        return new LatLng(37.5665, 126.9780);
    }
}
