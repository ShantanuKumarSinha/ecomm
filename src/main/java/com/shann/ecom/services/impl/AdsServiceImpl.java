package com.shann.ecom.services.impl;

import com.shann.ecom.exceptions.UserNotFoundException;
import com.shann.ecom.models.Advertisement;
import com.shann.ecom.repositories.AdvertisementRepository;
import com.shann.ecom.repositories.PreferenceRepository;
import com.shann.ecom.repositories.UserRepository;
import com.shann.ecom.services.AdsService;
import org.springframework.stereotype.Service;

/**
 * AdsServiceImpl is a service class that provides methods to manage advertisements. It implements
 * the AdsService interface and uses repositories to interact with the database.
 */
@Service
public class AdsServiceImpl implements AdsService {

  /** Repsitories for managing advertisements, preferences, and users. */
  private AdvertisementRepository advertisementRepository;

  private PreferenceRepository preferenceRepository;
  private UserRepository userRepository;

  /**
   * Constructor for AdsServiceImpl. It initializes the advertisementRepository,
   * preferenceRepository, and userRepository.
   *
   * @param advertisementRepository the advertisement repository
   * @param preferenceRepository the preference repository
   * @param userRepository the user repository
   */
  public AdsServiceImpl(
      AdvertisementRepository advertisementRepository,
      PreferenceRepository preferenceRepository,
      UserRepository userRepository) {
    this.advertisementRepository = advertisementRepository;
    this.preferenceRepository = preferenceRepository;
    this.userRepository = userRepository;
  }

  /**
   * Retrieves an advertisement for a specific user based on their preferences.
   *
   * @param userId the ID of the user
   * @return the advertisement for the user
   * @throws UserNotFoundException if the user is not found
   */
  @Override
  public Advertisement getAdvertisementForUser(int userId) throws UserNotFoundException {
    // Check if the user exists
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    if (user.getPreferences().isEmpty()) {
      // If the user has no preferences, return the first advertisement
      return advertisementRepository.findAll().get(0);
    } else {
      var preference = user.getPreferences().get(0);
      var advertisement =
          advertisementRepository
              .findByPreference(preference)
              .orElse(advertisementRepository.findAll().get(0));
      return advertisement;
    }
  }
}
