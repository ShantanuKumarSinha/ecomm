-- This SQL file is used to insert initial data into the database tables.
ALTER TABLE ecom_address
DROP CONSTRAINT FKG0DE3E6FIOYFH38D9X2T0L2PC;

ALTER TABLE ecom_address
ADD CONSTRAINT FKG0DE3E6FIOYFH38D9X2T0L2PC
FOREIGN KEY (user_id) REFERENCES ecom_user(id) ON DELETE CASCADE;

ALTER TABLE ecom_delivery_hub
DROP CONSTRAINT FKSJXCOEFV6DA11KP7VWBF512A6;

ALTER TABLE ecom_delivery_hub
ADD CONSTRAINT FKSJXCOEFV6DA11KP7VWBF512A6
FOREIGN KEY(address_id) REFERENCES ecom_address(id) ON DELETE CASCADE;

ALTER TABLE ecom_seller
DROP CONSTRAINT FK2YG5X9ETQ5Y6M2HE99JQUPCPG;

ALTER TABLE ecom_seller
ADD CONSTRAINT FK2YG5X9ETQ5Y6M2HE99JQUPCPG
FOREIGN KEY(address_id) REFERENCES ecom_address(id) ON DELETE CASCADE;

ALTER TABLE ecom_order_detail
DROP CONSTRAINT FKTGU8434SP17CMA5E4QYXITE5R;

ALTER TABLE ecom_order_detail
ADD CONSTRAINT FKTGU8434SP17CMA5E4QYXITE5R
FOREIGN KEY(product_id) REFERENCES ecom_product(id) ON DELETE CASCADE;

ALTER TABLE ecom_order
DROP CONSTRAINT FKJ2T2JQFAK1CQ970DHPAPJC74N;

ALTER TABLE ecom_order
ADD CONSTRAINT FKJ2T2JQFAK1CQ970DHPAPJC74N
FOREIGN KEY(user_id) REFERENCES ecom_user(id) ON DELETE CASCADE;

ALTER TABLE ecom_inventory
DROP CONSTRAINT FK7CITN3JF9VOCWUO76HI2OFPKK;

ALTER TABLE ecom_inventory
ADD CONSTRAINT FK7CITN3JF9VOCWUO76HI2OFPKK
FOREIGN KEY(product_id) REFERENCES ecom_product(id) ON DELETE CASCADE;

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