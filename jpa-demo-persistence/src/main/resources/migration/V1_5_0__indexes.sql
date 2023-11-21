DROP INDEX IF EXISTS index_action_tbl_page;
CREATE INDEX index_action_tbl_page ON mjr_action (page);
DROP INDEX IF EXISTS index_loginhistory_tbl_user_id;
CREATE INDEX index_loginhistory_tbl_user_id ON mjr_login_history (client_id);
DROP INDEX IF EXISTS index_administrator_tbl_content_id;
CREATE INDEX index_administrator_tbl_content_id ON mjr_administrator (content_id);