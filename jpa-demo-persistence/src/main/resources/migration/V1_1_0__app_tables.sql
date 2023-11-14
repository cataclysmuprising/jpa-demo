/* Create Extensions */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
/* Drop Tables */
DROP TABLE IF EXISTS mjr_role;
DROP TABLE IF EXISTS mjr_action;
DROP TABLE IF EXISTS mjr_role_x_action;
DROP TABLE IF EXISTS mjr_administrator;
DROP TABLE IF EXISTS mjr_administrator_x_role;
DROP TABLE IF EXISTS mjr_static_content;
DROP TABLE IF EXISTS mjr_setting;
/* Create Tables */
CREATE TABLE mjr_role
(
	id SERIAL PRIMARY KEY,
	app_name varchar(30) NOT NULL,
	name varchar(20) NOT NULL,
	type smallint DEFAULT 1 NOT NULL,
	description varchar(200) NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL
) WITHOUT OIDS;

CREATE TABLE mjr_action
(
	id SERIAL PRIMARY KEY,
	app_name varchar(30) NOT NULL,
	page varchar(50) NOT NULL,
	action_name varchar(50) NOT NULL,
	display_name varchar(50) NOT NULL,
	-- 0[main-action that is the main page action]
	-- 1[sub-action that process within a page]
	action_type smallint NOT NULL,
	url varchar NOT NULL,
	description varchar(200) NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL,
	CONSTRAINT unk_app_action_name UNIQUE (app_name, action_name),
	CONSTRAINT unk_app_url UNIQUE (app_name, url)
) WITHOUT OIDS;

CREATE TABLE mjr_role_x_action
(
    id SERIAL PRIMARY KEY,
	role_id bigint NOT NULL,
	action_id bigint NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL,
	CONSTRAINT unk_role_action UNIQUE (role_id, action_id)
) WITHOUT OIDS;

CREATE TABLE mjr_administrator
(
	id SERIAL PRIMARY KEY,
	content_id bigint,
	name varchar(50) NOT NULL,
	login_id varchar(50) NOT NULL UNIQUE,
	password varchar(200) NOT NULL,
	status smallint DEFAULT 0 NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL
) WITHOUT OIDS;
ALTER SEQUENCE mjr_administrator_id_seq RESTART 1000;

CREATE TABLE mjr_administrator_x_role
(
    id SERIAL PRIMARY KEY,
	administrator_id bigint NOT NULL,
	role_id bigint NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL,
	CONSTRAINT unk_user_role UNIQUE (administrator_id, role_id)
) WITHOUT OIDS;

CREATE TABLE mjr_login_history
(
	id SERIAL PRIMARY KEY,
	client_id bigint,
	client_type smallint NOT NULL,
	ip_address varchar(45) NOT NULL,
	-- Operating System
	os varchar(100),
	client_agent varchar(100),
	login_date timestamp NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL
) WITHOUT OIDS;
ALTER SEQUENCE mjr_login_history_id_seq RESTART 1000;

CREATE TABLE mjr_static_content
(
	id SERIAL PRIMARY KEY,
	file_name varchar(200) NOT NULL,
	file_path varchar NOT NULL,
	file_size varchar(20),
	file_type smallint NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL
) WITHOUT OIDS;
ALTER SEQUENCE mjr_static_content_id_seq RESTART 1000;

CREATE TABLE mjr_setting
(
	id SERIAL PRIMARY KEY,
	setting_group varchar(50) NOT NULL,
	setting_sub_group varchar(50) NOT NULL,
	setting_type varchar(50) NOT NULL,
	setting_name varchar(50) NOT NULL,
	setting_value varchar NOT NULL,
	record_reg_id bigint NOT NULL,
	record_upd_id bigint NOT NULL,
	record_reg_date timestamp DEFAULT current_timestamp NOT NULL,
	record_upd_date timestamp DEFAULT current_timestamp NOT NULL,
	CONSTRAINT complx_setting UNIQUE (setting_name, setting_value, setting_type, setting_group, setting_sub_group)
) WITHOUT OIDS;


/* Create Foreign Keys */

ALTER TABLE mjr_administrator
	ADD CONSTRAINT frk_content_administrator FOREIGN KEY (content_id)
	REFERENCES mjr_static_content (id)
	ON UPDATE NO ACTION
	ON DELETE SET NULL
;

ALTER TABLE mjr_role_x_action
	ADD CONSTRAINT frk_action_role FOREIGN KEY (action_id)
	REFERENCES mjr_action (id)
	ON UPDATE NO ACTION
	ON DELETE CASCADE
;

ALTER TABLE mjr_role_x_action
	ADD CONSTRAINT frk_role_action FOREIGN KEY (role_id)
	REFERENCES mjr_role (id)
	ON UPDATE NO ACTION
	ON DELETE CASCADE
;

ALTER TABLE mjr_administrator_x_role
	ADD CONSTRAINT frk_administrator_role FOREIGN KEY (administrator_id)
	REFERENCES mjr_administrator (id)
	ON UPDATE NO ACTION
	ON DELETE CASCADE
;

ALTER TABLE mjr_administrator_x_role
	ADD CONSTRAINT frk_role_user FOREIGN KEY (role_id)
	REFERENCES mjr_role (id)
	ON UPDATE NO ACTION
	ON DELETE CASCADE
;


/* Comments */

COMMENT ON COLUMN mjr_action.action_type IS '0[main-action that is the main page action]
1[sub-action that process within a page]
';
COMMENT ON COLUMN mjr_login_history.os IS 'Operating System';