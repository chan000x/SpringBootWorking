CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY, -- Automatically creates a sequence and assigns it to the id column.
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL
);


-- CREATE SEQUENCE customer_id_sequence;
-- CREATE TABLE customer (
--     id BIGINT DEFAULT nextval('customer_id_sequence') PRIMARY KEY, 
--     -- You manually create the sequence (customer_id_sequence).
--     --You manually create the sequence (customer_id_sequence).
--     name TEXT NOT NULL,
--     email TEXT NOT NULL,
--     age INT NOT NULL
-- );
-- To set the sequence value : SELECT setval('tableName_columnName_seq',1,false);
-- To view the next sequence value(The value will increament by one each time this used) : select nextval('customer_id_seq');
-- To view the current sequence value(Sequence value does not increment when using this command in postgreSQL) : select currval('customer_id_seq');




