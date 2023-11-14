/******      Administrator        ******/
INSERT INTO mjr_administrator
(id,content_id, name, 			        login_id, 					                     password, 															  	status,  			   record_reg_id ,record_upd_id ,record_reg_date ,record_upd_date )
VALUES 
/******      SU@1001_P@ssw0rd     ******/
(1, null, 	   'Super User', 	    	'superuser@1001', 			                     '$2a$10$n5G7v7kLZygsbZf9viB6W.tzLcPxIuUjdirNFSBxqXfEs4q6iS7O2', 	 	1, 		 			   0,0,current_timestamp,current_timestamp);


/******      Role        ******/
INSERT INTO mjr_role
(id,	app_name,                   name, 			        description, 							                                                                                                type,   record_reg_id,record_upd_id,record_reg_date,record_upd_date)
VALUES
(1,		'myapp-backend',          'Super-User',	    	'Master role to manage entire application. This role own special right, advantage, or immunity granted or available.',			 		    0,      0,0,current_timestamp,current_timestamp),
(2,		'myapp-backend',          'Administrator',		'Default Administrator role.',					                                                                                       	    1,      0,0,current_timestamp,current_timestamp),
(3,		'myapp-backend',          'View only User',		'Default role for view only.',					                                                                                       	    1,      0,0,current_timestamp,current_timestamp);

/******     Administrator Role       ******/
INSERT INTO mjr_administrator_x_role
(administrator_id, 	role_id,    record_reg_id,record_upd_id,record_reg_date,record_upd_date)
VALUES 
/******      Super User     ******/
(1,			  		1,          0,0,current_timestamp,current_timestamp);

/******      action        ******/
INSERT INTO mjr_action  
(id,	 app_name, 					 page,						 action_name,			      		display_name,										action_type,		url,													                    description,																					                   record_reg_id,record_upd_id,record_reg_date,record_upd_date)
VALUES 	
----- Dashboard
(10011,	'myapp-backend',		    'Dashboard',			 	 'dashboard',		      			'Dashboard page for User',								0,				'^/sec/dashboard$',									              				'Control panel page for Sign-in Admin.',															                0,0,current_timestamp,current_timestamp),

----- Administrators
(10021,	'myapp-backend',		    'Administrator',			 'adminList',		      			'Admin Home page',								        0,				'^/sec/admin/all$',									              				'...',															                                                    0,0,current_timestamp,current_timestamp),
(10022,	'myapp-backend',		    'Administrator',			 'adminAdd',		      			'Admin register page',								    1,				'^/sec/admin/all/add$',									              		    '...',															                                                    0,0,current_timestamp,current_timestamp),
(10023,	'myapp-backend',		    'Administrator',			 'adminEdit',		      			'Admin edit page',								        1,				'^/sec/admin/all/[0-9]{1,}/edit$',									            '...',															                                                    0,0,current_timestamp,current_timestamp),
(10024,	'myapp-backend',		    'Administrator',			 'adminRemove',		      			'To remove existing administrator',						1,				'^/sec/admin/all/[0-9]{1,}/delete$',									        '...',															                                                    0,0,current_timestamp,current_timestamp),
(10025,	'myapp-backend',		    'Administrator',			 'adminDetail',		      			'View detail information of an administrator',			1,				'^/sec/admin/all/[0-9]{1,}$',									                '...',															                                                    0,0,current_timestamp,current_timestamp),
----- Users
(10031,	'myapp-backend',		    'User',			 	         'userList',		      			'User Home page',								        0,				'^/sec/user/all$',									              				'...',															                                                    0,0,current_timestamp,current_timestamp),
(10032,	'myapp-backend',		    'User',			 	         'userAdd',		      			    'User register page',								    1,				'^/sec/user/all/add$',									              		    '...',															                                                    0,0,current_timestamp,current_timestamp),
(10033,	'myapp-backend',		    'User',			 	         'userEdit',		      			'User edit page',								        1,				'^/sec/user/all/[0-9]{1,}/edit$',									            '...',															                                                    0,0,current_timestamp,current_timestamp),
(10034,	'myapp-backend',		    'User',			 	         'userRemove',		      			'To remove existing user',								1,				'^/sec/user/all/[0-9]{1,}/delete$',									            '...',															                                                    0,0,current_timestamp,current_timestamp),
(10035,	'myapp-backend',		    'User',			 	         'userDetail',		      			'View detail information of a user',					1,				'^/sec/user/all/[0-9]{1,}$',									                '...',															                                                    0,0,current_timestamp,current_timestamp);

/******      Role Action        ******/
INSERT INTO mjr_role_x_action
(role_id,	action_id,  record_reg_id,record_upd_id,record_reg_date,record_upd_date)
VALUES 
----- Super User
(1,			10011,      0,0,current_timestamp,current_timestamp),
(1,			10021,      0,0,current_timestamp,current_timestamp),
(1,			10022,      0,0,current_timestamp,current_timestamp),
(1,			10023,      0,0,current_timestamp,current_timestamp),
(1,			10024,      0,0,current_timestamp,current_timestamp),
(1,			10025,      0,0,current_timestamp,current_timestamp),
(1,			10031,      0,0,current_timestamp,current_timestamp),
(1,			10032,      0,0,current_timestamp,current_timestamp),
(1,			10033,      0,0,current_timestamp,current_timestamp),
(1,			10034,      0,0,current_timestamp,current_timestamp),
(1,			10035,      0,0,current_timestamp,current_timestamp),
----- Administrator
(2,			10011,      0,0,current_timestamp,current_timestamp),
(2,			10031,      0,0,current_timestamp,current_timestamp),
(2,			10032,      0,0,current_timestamp,current_timestamp),
(2,			10033,      0,0,current_timestamp,current_timestamp),
(2,			10034,      0,0,current_timestamp,current_timestamp),
(2,			10035,      0,0,current_timestamp,current_timestamp),
----- View only User
(3,			10011,      0,0,current_timestamp,current_timestamp);
