package com.shann.ecom.services.impl;

import com.shann.ecom.adapter.GoogleMapsAdapters;
import com.shann.ecom.adapter.MapsAdapters;
import com.shann.ecom.exceptions.AddressNotFoundException;
import com.shann.ecom.exceptions.ProductNotFoundException;
import com.shann.ecom.repositories.AddressRepository;
import com.shann.ecom.repositories.DeliveryHubRepository;
import com.shann.ecom.repositories.ProductRepository;
import com.shann.ecom.services.ProductService;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private MapsAdapters mapsAdapters;
  private ProductRepository productRepository;
  private AddressRepository addressRepository;
  private DeliveryHubRepository deliveryHubRepository;

  public ProductServiceImpl(
      GoogleMapsAdapters googleMapsAdapters,
      ProductRepository productRepository,
      AddressRepository addressRepository,
      DeliveryHubRepository deliveryHubRepository) {
    this.mapsAdapters = googleMapsAdapters;
    this.productRepository = productRepository;
    this.addressRepository = addressRepository;
    this.deliveryHubRepository = deliveryHubRepository;
  }

  /**
   * This method will calculate estimate delivery time
   *
   * @param productId
   * @param addressId
   */
  @Override
  public Date estimateDeliveryDate(int productId, int addressId)
      throws ProductNotFoundException, AddressNotFoundException {

    var product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () -> new ProductNotFoundException("Product with: " + productId + " not found"));

    var seller = product.getSeller();

    var address =
        addressRepository
            .findById(addressId)
            .orElseThrow(
                () -> new AddressNotFoundException("There is no address with: " + addressId));
    var deliveryHub = deliveryHubRepository.findByZipCode(address.getZipCode()).get();
    var timeToReachDeliveryHub =
        mapsAdapters.getEstimatedTime(
            seller.getAddress().getLatitude(),
            seller.getAddress().getLongitude(),
            deliveryHub.getAddress().getLatitude(),
            deliveryHub.getAddress().getLongitude());

    var timeToReachUser =
        mapsAdapters.getEstimatedTime(
            deliveryHub.getAddress().getLatitude(),
            deliveryHub.getAddress().getLongitude(),
            address.getLatitude(),
            address.getLongitude());

    var estimatedTime = timeToReachDeliveryHub + timeToReachUser;
    var totalTime = new Date().getTime() + estimatedTime;
    return new Date(totalTime);
  }
}
