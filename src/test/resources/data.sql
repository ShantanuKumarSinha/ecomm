-- This SQL file is used to insert initial data into the database tables.

ALTER TABLE ecom_inventory
ADD CONSTRAINT FK_PRODUCT
FOREIGN KEY (product_id) REFERENCES ecom_product(id) ON DELETE CASCADE;