package com.shann.ecom.adapter;

import com.shann.ecom.libraries.GoogleMapsApi;
import com.shann.ecom.libraries.models.GLocation;
import org.springframework.stereotype.Component;

@Component
public class GoogleMapsAdapters implements MapsAdapters {

  private final GoogleMapsApi googleMapsApi = new GoogleMapsApi();

  @Override
  public int getEstimatedTime(double srcLat, double srcLong, double destLat, double destLong) {
    GLocation src = new GLocation();
    src.setLatitude(srcLat);
    src.setLongitude(srcLong);
    GLocation dest = new GLocation();
    dest.setLatitude(destLat);
    dest.setLongitude(destLong);
    return googleMapsApi.estimate(src, dest);
  }
}
