package com.shann.ecom.repositories;

import com.shann.ecom.models.Advertisement;
import com.shann.ecom.models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    Optional<Advertisement> findByPreference(Preference preference);
}
