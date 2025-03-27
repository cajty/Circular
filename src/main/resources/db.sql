
ALTER SEQUENCE IF EXISTS users_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS roles_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS enterprises_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS locations_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS materials_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS material_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS city_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS transactions_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS transaction_items_id_seq RESTART WITH 1;


INSERT INTO city (name, is_active, created_at) VALUES
('casablanca', true, CURRENT_TIMESTAMP),
('rabat', true, CURRENT_TIMESTAMP),
('tangier', true, CURRENT_TIMESTAMP),
('marrakech', true, CURRENT_TIMESTAMP),
('fes', true, CURRENT_TIMESTAMP),
('agadir', true, CURRENT_TIMESTAMP),
('meknes', false, CURRENT_TIMESTAMP),
('oujda', true, CURRENT_TIMESTAMP),
('kenitra', true, CURRENT_TIMESTAMP),
('tetouan', false, CURRENT_TIMESTAMP);

-- Material Categories
INSERT INTO material_categories (name, description, is_active, created_at) VALUES
('plastic', 'Various plastic materials including PET, HDPE, etc.', true, CURRENT_TIMESTAMP),
('paper', 'Paper products including cardboard, newsprint, etc.', true, CURRENT_TIMESTAMP),
('metal', 'Metal waste including aluminum, steel, etc.', true, CURRENT_TIMESTAMP),
('glass', 'Glass waste including bottles, jars, etc.', true, CURRENT_TIMESTAMP),
('electronic', 'E-waste including devices, components, etc.', true, CURRENT_TIMESTAMP),
('organic', 'Organic waste for composting', true, CURRENT_TIMESTAMP),
('textile', 'Textile waste including clothing, fabrics, etc.', true, CURRENT_TIMESTAMP),
('rubber', 'Rubber waste including tires, etc.', false, CURRENT_TIMESTAMP),
('construction', 'Construction and demolition waste', true, CURRENT_TIMESTAMP),
('hazardous', 'Hazardous waste requiring special handling', true, CURRENT_TIMESTAMP);

-- Enterprises
INSERT INTO enterprises (name, registration_number, type, status, rejection_reason) VALUES
('EcoRecycle', 'ABC12345', 'RECYCLER', 'VERIFIED', NULL),
('GreenCollect', 'DEF67890', 'COLLECTOR', 'VERIFIED', NULL),
('CircularProcess', 'GHI12345', 'PROCESSOR', 'VERIFIED', NULL),
('EarthSaver', 'JKL67890', 'RECYCLER', 'PENDING', NULL),
('WasteCollectors', 'MNO12345', 'COLLECTOR', 'UNDER_REVIEW', NULL),
('EcoProcess Inc.', 'PQR67890', 'PROCESSOR', 'REJECTED', 'Documentation incomplete'),
('RecycleNow', 'STU12345', 'RECYCLER', 'VERIFIED', NULL),
('CollectAll', 'VWX67890', 'COLLECTOR', 'VERIFIED', NULL),
('Process360', 'YZA12345', 'PROCESSOR', 'VERIFIED', NULL),
('GreenRecyclers', 'BCD67890', 'RECYCLER', 'UNDER_REVIEW', NULL);

-- Generate users with bcrypt passwords for 'password123'
-- Note: In a real script, you'd use dynamic UUIDs, but for simplicity, we'll use hardcoded ones
INSERT INTO users (id, email, password, first_name, last_name, phone_number, status, created_at, enterprise_id) VALUES
('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'manager1@ecorecycle.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'John', 'Manager', '+212601020304', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '1 year', 1),
('f47ac10b-58cc-4372-a567-0e02b2c3d480', 'user1@greencollect.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Sarah', 'Employee', '+212605060708', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '11 months', 2),
('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'manager2@circularprocess.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Mike', 'Director', '+212609101112', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '10 months', 3),
('f47ac10b-58cc-4372-a567-0e02b2c3d482', 'user2@earthsaver.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Anna', 'Worker', '+212613141516', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '2 months', 4),
('f47ac10b-58cc-4372-a567-0e02b2c3d483', 'manager3@wastecollectors.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'David', 'Boss', '+212617181920', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '1 month', 5),
('f47ac10b-58cc-4372-a567-0e02b2c3d484', 'user3@ecoprocess.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Emma', 'Staff', '+212621222324', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '3 months', 6),
('f47ac10b-58cc-4372-a567-0e02b2c3d485', 'manager4@recyclenow.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'James', 'Chief', '+212625262728', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '8 months', 7),
('f47ac10b-58cc-4372-a567-0e02b2c3d486', 'user4@collectall.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Sophia', 'Coordinator', '+212629303132', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '7 months', 8),
('f47ac10b-58cc-4372-a567-0e02b2c3d487', 'manager5@process360.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Robert', 'Head', '+212633343536', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '6 months', 9),
('f47ac10b-58cc-4372-a567-0e02b2c3d488', 'user5@greenrecyclers.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Olivia', 'Assistant', '+212637383940', 'ACTIVE', CURRENT_TIMESTAMP - INTERVAL '15 days', 10);

-- User Roles (ADMIN, USER, MANAGER, EMPLOYEE)
INSERT INTO user_roles (user_id, role_id) VALUES
('f47ac10b-58cc-4372-a567-0e02b2c3d479', 3), -- Manager role
('f47ac10b-58cc-4372-a567-0e02b2c3d480', 2), -- User role
('f47ac10b-58cc-4372-a567-0e02b2c3d481', 3), -- Manager role
('f47ac10b-58cc-4372-a567-0e02b2c3d482', 2), -- User role
('f47ac10b-58cc-4372-a567-0e02b2c3d483', 3), -- Manager role
('f47ac10b-58cc-4372-a567-0e02b2c3d484', 2), -- User role
('f47ac10b-58cc-4372-a567-0e02b2c3d485', 3), -- Manager role
('f47ac10b-58cc-4372-a567-0e02b2c3d486', 2), -- User role
('f47ac10b-58cc-4372-a567-0e02b2c3d487', 3), -- Manager role
('f47ac10b-58cc-4372-a567-0e02b2c3d488', 2); -- User role

-- Locations
INSERT INTO locations (address, city_id, type, is_active, enterprise_id) VALUES
('123 Recycling Ave', 1, 'RECYCLING_CENTER', true, 1),
('456 Collection St', 2, 'COLLECTION_POINT', true, 2),
('789 Processing Blvd', 3, 'WAREHOUSE', true, 3),
('321 Earth Rd', 4, 'RECYCLING_CENTER', true, 4),
('654 Waste St', 5, 'COLLECTION_POINT', true, 5),
('987 Eco Park', 1, 'WAREHOUSE', true, 6),
('147 Recycle Way', 2, 'RECYCLING_CENTER', true, 7),
('258 Collect Dr', 3, 'COLLECTION_POINT', true, 8),
('369 Process Ave', 4, 'WAREHOUSE', true, 9),
('741 Green St', 5, 'RECYCLING_CENTER', true, 10),
('852 Secondary Loc', 6, 'WAREHOUSE', false, 1),
('963 Alternative Site', 7, 'COLLECTION_POINT', true, 2),
('159 Processing Center', 8, 'WAREHOUSE', true, 3),
('357 Backup Facility', 9, 'RECYCLING_CENTER', false, 4),
('864 Collection Hub', 1, 'COLLECTION_POINT', true, 5);

-- Verification Documents
INSERT INTO verification_documents (enterprise_id, document_type, file_name, content_type, file_path, uploaded_at, uploaded_by) VALUES
(1, 'BUSINESS_LICENSE', 'license_ecorecycle.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_ecorecycle.pdf', CURRENT_TIMESTAMP - INTERVAL '364 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
(1, 'TAX_CERTIFICATE', 'tax_ecorecycle.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/tax_ecorecycle.pdf', CURRENT_TIMESTAMP - INTERVAL '363 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
(2, 'BUSINESS_LICENSE', 'license_greencollect.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_greencollect.pdf', CURRENT_TIMESTAMP - INTERVAL '334 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d480'),
(3, 'BUSINESS_LICENSE', 'license_circularprocess.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_circularprocess.pdf', CURRENT_TIMESTAMP - INTERVAL '304 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d481'),
(4, 'BUSINESS_LICENSE', 'license_earthsaver.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_earthsaver.pdf', CURRENT_TIMESTAMP - INTERVAL '60 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d482'),
(5, 'BUSINESS_LICENSE', 'license_wastecollectors.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_wastecollectors.pdf', CURRENT_TIMESTAMP - INTERVAL '30 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d483'),
(6, 'BUSINESS_LICENSE', 'license_ecoprocess.pdf', 'application/pdf', 'https://circular-bucket.s3.amazonaws.com/license_ecoprocess.pdf', CURRENT_TIMESTAMP - INTERVAL '90 days', 'f47ac10b-58cc-4372-a567-0e02b2c3d484');

-- Materials
INSERT INTO materials (name, description, quantity, unit, price, status, listed_at, available_until, category_id, user_id, hazard_level, location_id) VALUES
('PET Bottles', 'Clean PET plastic bottles', 500, 'KG', 5.50, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '300 days', CURRENT_TIMESTAMP + INTERVAL '30 days', 1, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'NONE', 1),
('Cardboard Boxes', 'Flattened cardboard packaging', 750, 'KG', 3.25, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '290 days', CURRENT_TIMESTAMP + INTERVAL '45 days', 2, 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'NONE', 2),
('Aluminum Scrap', 'Mixed aluminum scrap metal', 300, 'KG', 15.75, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '280 days', CURRENT_TIMESTAMP + INTERVAL '60 days', 3, 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'LOW', 3),
('Glass Bottles', 'Clear glass bottles', 400, 'KG', 2.50, 'RESERVED', CURRENT_TIMESTAMP - INTERVAL '270 days', CURRENT_TIMESTAMP + INTERVAL '15 days', 4, 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'LOW', 4),
('Electronic Waste', 'Mixed electronic components', 200, 'KG', 25.00, 'RESERVED', CURRENT_TIMESTAMP - INTERVAL '260 days', CURRENT_TIMESTAMP + INTERVAL '20 days', 5, 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'MEDIUM', 5),
('Organic Compost', 'Pre-processed organic material', 1000, 'KG', 2.00, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '250 days', CURRENT_TIMESTAMP + INTERVAL '90 days', 6, 'f47ac10b-58cc-4372-a567-0e02b2c3d484', 'NONE', 6),
('Used Clothing', 'Sorted textile materials', 350, 'KG', 4.50, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '240 days', CURRENT_TIMESTAMP + INTERVAL '75 days', 7, 'f47ac10b-58cc-4372-a567-0e02b2c3d485', 'NONE', 7),
('Tire Rubber', 'Shredded rubber from tires', 600, 'KG', 3.75, 'SOLD', CURRENT_TIMESTAMP - INTERVAL '230 days', CURRENT_TIMESTAMP - INTERVAL '30 days', 8, 'f47ac10b-58cc-4372-a567-0e02b2c3d486', 'LOW', 8),
('Concrete Waste', 'Crushed concrete for recycling', 2000, 'KG', 1.25, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '220 days', CURRENT_TIMESTAMP + INTERVAL '60 days', 9, 'f47ac10b-58cc-4372-a567-0e02b2c3d487', 'LOW', 9),
('Waste Batteries', 'Used batteries for proper disposal', 100, 'KG', 30.00, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '210 days', CURRENT_TIMESTAMP + INTERVAL '30 days', 10, 'f47ac10b-58cc-4372-a567-0e02b2c3d488', 'HIGH', 10),
('HDPE Plastic', 'HDPE plastic containers', 450, 'KG', 6.25, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '200 days', CURRENT_TIMESTAMP + INTERVAL '45 days', 1, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'NONE', 1),
('Newspaper', 'Bundled newspaper for recycling', 800, 'KG', 2.75, 'EXPIRED', CURRENT_TIMESTAMP - INTERVAL '190 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 2, 'f47ac10b-58cc-4372-a567-0e02b2c3d480', 'NONE', 2),
('Steel Scrap', 'Industrial steel scrap', 550, 'KG', 12.50, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '180 days', CURRENT_TIMESTAMP + INTERVAL '30 days', 3, 'f47ac10b-58cc-4372-a567-0e02b2c3d481', 'LOW', 3),
('Green Glass', 'Green glass bottles and jars', 350, 'KG', 2.25, 'AVAILABLE', CURRENT_TIMESTAMP - INTERVAL '170 days', CURRENT_TIMESTAMP + INTERVAL '60 days', 4, 'f47ac10b-58cc-4372-a567-0e02b2c3d482', 'LOW', 4),
('Computer Parts', 'Sorted computer components', 150, 'KG', 35.00, 'RESERVED', CURRENT_TIMESTAMP - INTERVAL '160 days', CURRENT_TIMESTAMP + INTERVAL '45 days', 5, 'f47ac10b-58cc-4372-a567-0e02b2c3d483', 'MEDIUM', 5);



