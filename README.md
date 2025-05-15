# Implement Inventory Management for an E-Commerce Platform

## Problem Statement

You are building an e-commerce platform. As a part of this system, you need to expose a set of functionalities using
which admins can manage the inventory, i.e. create, update and delete inventories for products.

### Requirements

- Create or Update Inventory: This functionality will be called when we are adding a new product to the platform or we
  want to update inventory of an existing product. The request will contain the user id, the product id and the quantity
  of the product that we want to add to the inventory. The response will contain inventory object.

- Delete Inventory: This functionality will be called when we want to delete the inventory of a product i.e. in
  scenarios where we want to remove a product from the platform. The request will contain the user id and the product
  id. The response will contain the response status i.e. SUCCESS or FAILURE.

- Note: All the above functionalities should be accessible by admin users only.

### Instructions

    * Carefully look at the classes in dtos package. These classes represent the request and response of the functionality which we want to implement.
    * Carefully examine the models package to understand the database schema.
    * Implement the createOrUpdateInventory and deleteInventory method inside the InventoryController.
    * Implement the InventoryService interface and fix the repository interfaces.
    * You might need to add annotations like @Service, @Autowired, @Entity etc. to make the solution work. You might also need to handle cardinality between the models.
    * We will be using H2 database which is an in-memory SQL database. You do not need to implement any database related code. You just need to use the repository interfaces to interact with the database.
    * Carefully examine the TestInventoryController class to understand how the controller will be tested. Your solution should pass all the tests in this class.

# Estimate delivery time functionality for an e-commerce platform

## Problem Statement

    Whenever we browse a product on Amazon/Flipkart, we see an estimated delivery date and time on the product page. This estimate tells us that if we buy the product now, then by what time we will receive the product. We want to build this functionality for our e-commerce platform.

### Requirements

- Products are stored at seller's warehouses. To ensure seamless and quick delivery experience, ecommerce companies
  setup delivery hubs at various locations. We can assume that each zipcode (pincode) has 1 delivery hub. Since each
  zipcode has 1 delivery hub, this is ideal spot from where the product can be delivered to the user.

- Total time required to deliver a product to a user is made up of 2 parts:

- Ship the product from seller warehouse to the delivery hub.
- Ship the product from the delivery hub to the user's delivery location. Total time required = Time required to ship
  from seller warehouse to delivery hub + Time required to ship from delivery hub to user's delivery location. Add this
  total time required to the current time to get the estimated time.
- We will use Google Maps API to calculate the time required to move from a src location to a destination location
- We need to make sure that this integration is as loosely coupled as possible to ensure that we can easily switch to a
  different API in the future.

- We have a address table which will be storing addresses of users, seller warehouses and delivery hubs. We can use this
  table to calculate the estimated delivery time.

### Assumptions:

    Each zip code has 1 delivery hub.
    Each customer has 1 stored address.
    Each seller has 1 warehouse.
    Each product will be sold by 1 seller.
    Each seller can sell multiple products.
    Request: The request will contain the following information:
    
    Product ID - The product for which we want to calculate the estimated delivery time.
    Address ID - The delivery address of the user.
    Response: The response will contain the following information:
    
    Estimated date - The estimated date and time of delivery.
    Response status - This will be either success or failure.

# Implement notify users for out of stock products functionality for an e-commerce platform

## Problem Statement:

    You are building an e-commerce platform. As a part of this system, you need to expose a functionality using which users can register for notifications when a out-of-stock product becomes available. Once the product is back in stock, these users should be notified via email.

### Requirements

- We need to build 3 functionalities:

### Requirement #1:

- Interested users can register for notifications when a product becomes available.
- Request for registering for notifications will contain: product id and user id. Before registering for notifications
  we need to check the following things:

- Does the user exist in the system? If not then we need to throw an exception.
- Does the product exist in the system? If not then we need to throw an exception.
- Is the product really out of stock? If not then we need to throw an exception.
- If all the above checks pass, then we need to register the user for notifications by making an entry in the
  notifications table.
- Response for registering for notifications will contain: notification object and response status.

### Requirement #2:

- Users who have already registered for notifications can unregister themselves.
- Request for unregistering for notifications will contain: notification id and user id. Before unregistering for
  notifications we need to check the following things:

- Does the user exist in the system? If not then we need to throw an exception.
- Does the notification exist in the system? If not then we need to throw an exception.
- Does the notification belong to the user? If not then we need to throw an exception.
- If all the above checks pass, then we need to unregister the user for notifications by deleting the entry from the
  notifications table.
- Response for unregistering for notifications will contain: response status.

### Requirement #3:

- There is a functionality using which Admins can add stocks for a product. Modify this functionality to notify all the
  users who have registered for notifications for this product. The users who have registered themselves should be
  notified via email.
- We need to modify the updateInventory method inside the InventoryController to notify all the users who have
  registered for notifications for this product. The users who have registered themselves should be notified via email.
  We will use SendGrid (a third party service) to send emails. The email subject will be "{productname} back in stock!"
  and the email body will be "Dear {username}, {product_name} is now back in stock. Grab it ASAP!". The integration with
  SendGrid should be as loosely coupled as possible. We should be able to easily replace SendGrid with any other email
  service provider in the future.

# Implement cancel order functionality for an e-commerce platform

## Problem Statement

You are building an e-commerce platform. As a part of this system, you need to expose a functionality using which users
can cancel an order.

### Requirements

- The request to cancel an order will contain the following information:
- User id of the user who wants to cancel the order.
- Order id of the order which the user wants to cancel.
- Before canceling an order we need to check the following things:
- Does the user exist in the system? If not then we need to throw an exception.
- Does order exist in the system? If not then we need to throw an exception.
- Does the order belong to the user? If not then we need to throw an exception.
- An order cannot be cancelled if its in SHIPPED or DELIVERED or CANCELLED state.
- If all the above checks pass then we need to update the inventory with by adding the quantity of the order back to the
  inventory. And marking the order as cancelled.
- We should handle for concurrent requests, i.e. we should update the inventory correctly.

# Implement place order functionality for an e-commerce platform
## Problem Statement
  You are building an e-commerce platform. As a part of this system, you need to expose a functionality using which users can place an order for products.

### Requirements
#### The request to place an order will contain the following information:
* User id of the user who wants to place the order.
* List of product ids and their quantities (List>, the pair's key is the product id and value is the quantity) which the user wants to order.
* Address id of the user's address where the order needs to be delivered.
* Before creating an order we need to check the following things:
* Does the user exist in the system? If not then we need to throw an exception.
* Does the address exist in the system? If not then we need to throw an exception.
* Does the address belong to the user? If not then we need to throw an exception.
* Do all the products have enough quantity to fill the order? If not then we need to throw an exception.
* If all the above checks pass, then we need to update the inventory with the updated quantity, create an order (with status as placed) and return the order details.
* We should handle for concurrent requests, i.e. we should not overbook the inventory. We should allow concurrent requests to place an order only if the inventory has enough quantity to fulfill the order.
* Few products might be facing a lot of demand but their supply is limited eg. iPhones. For such products we will store max number of quantity per order that a user can order. Details for such products will be stored in the high_demand_products table.
#### Instructions
* Carefully look at PlaceOrderRequestDto and PlaceOrderResponseDto classes. These classes represent the request and response of the functionality which we want to implement.
* Carefully examine the models package to understand the database schema.
* Implement the placeOrder method inside the OrderController.
* Implement the OrderService interface and fix the repository interfaces.
* You might need to add annotations like @Service, @Autowired, @Entity etc. to make the solution work. You might also need to handle cardinality between the models.
* We will be using H2 database which is an in-memory SQL database. You do not need to implement any database related code. You just need to use the repository interfaces to interact with the database.
* Implement the necessary exceptions in the exceptions package.
* Do not modify the OrderService interface's placeOrder method signature. You can add additional methods to the interface if you want.

## H2 Console
  http://127.0.0.1:8080/ecom/api/v1/h2-console

## Swagger Documentation
  http://127.0.0.1:8080/ecom/api/v1/swagger-ui/index.html
