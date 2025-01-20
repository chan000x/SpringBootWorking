-- After executing first time those schemas are stored in flyway schema history table.
-- If I need any alter to the created table I have to create another new SQL migration like this with version control.
-- Some naming convention is there. V used when once applied cannot modified.
-- R used in rename for can use reuse mean able to modified. ex : R1__FileName.sql
-- To undo already done migration can use a prefix U like this. ex : U2__Add_New_Table.sql



ALTER TABLE customer
ADD CONSTRAINT customer_email_uniq UNIQUE (email);





