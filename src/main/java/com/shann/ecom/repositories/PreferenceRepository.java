package com.shann.ecom.repositories;

import com.shann.ecom.models.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

}
