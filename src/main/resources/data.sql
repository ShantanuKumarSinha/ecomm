-- This SQL file is used to insert initial data into the database tables.
-- Create table for ecom_user

-- Insert statements for the ecom_user table
INSERT INTO ecom_user (name, email, user_type) VALUES
('Shantanu Kumar', 'shan.raj93@gmail.com',0),
('Adorable Pirate', 'addorable.pirate@gmail.com',1);

-- Insert statements for the address table
INSERT INTO ecom_address (floor, building, street, city, state, zip_code, country, latitude, longitude, user_id) VALUES
(11, 'Victoria Tower','123 Main St', 'New York', 'NY', '10001', 'USA', 40.7128, -74.0060, 1),
(6,'Empire State Building','456 Elm St', 'Los Angeles', 'CA', '90001', 'USA', 34.0522, -118.2437, 2);

-- Insert statements for the seller table
INSERT INTO ecom_seller (name, email, address_id) VALUES
('Seller A', 'seller.a@example.com', 1),
('Seller B', 'seller.b@example.com', 2);

-- Insert statements for the delivery_hub table
INSERT INTO ecom_delivery_hub (name, address_id) VALUES
('Hub A',  1),
('Hub B',  2);

INSERT INTO ecom_product (name, description, price, seller_id) VALUES
('Product A', 'Description for Product A', 100.00,1),
('Product B', 'Description for Product B', 200.00, 1);

-- Insert statements for the inventory table
INSERT INTO ecom_inventory (product_id, quantity) VALUES
(1, 10),
(2, 20);

-- Insert statements for the order table
INSERT INTO ecom_order (user_id, order_status) VALUES
(1, 0),
(2, 2);

-- Insert statements for the order_detail table
INSERT INTO ecom_order_detail ( order_id, product_id, quantity) VALUES
(1, 1, 1),
(1, 2, 1);

