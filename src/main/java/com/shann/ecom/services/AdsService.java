package com.shann.ecom.services;

import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Advertisement;

public interface AdsService {
  public Advertisement getAdvertisementForUser(int userId) throws UserNotFoundException;
}
