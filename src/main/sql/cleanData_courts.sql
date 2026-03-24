-- Clean rentals table
TRUNCATE rentals CASCADE;
ALTER SEQUENCE rentals_id_seq RESTART WITH 1;

-- Clean courts table
TRUNCATE courts CASCADE;
ALTER SEQUENCE courts_id_seq RESTART WITH 1;

-- Clean clubs table
TRUNCATE clubs CASCADE;
ALTER SEQUENCE clubs_id_seq RESTART WITH 1;

-- Clean users table
TRUNCATE users CASCADE;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
