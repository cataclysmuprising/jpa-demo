/******      Administrator        ******/
INSERT INTO mjr_administrator
(id,content_id, name, 			        login_id, 					                     password, 															  	status,  			   record_reg_id ,record_upd_id ,record_reg_date ,record_upd_date )
VALUES 
/******      steve@admin_P@ssw0rd     ******/
(2, null, 	   'Steve', 	            'steve@admin', 			                         '$2a$10$5pwhs/UBdLnSmSTZt0I01eIs3fJQ92wVUic1eVDeGK2pSZQPP9BAq', 	 	1, 		 			   0,0,current_timestamp,current_timestamp),
/******      johndoe@user_P@ssw0rd     ******/
(3, null, 	   'John Doe', 	    	    'johndoe@user', 			                     '$2a$10$0v35rwCMzsky5MwdOIHK8eiIhdu.di6AuNABMx.rn3dYm7HG6bJk.', 	 	1, 		 			   0,0,current_timestamp,current_timestamp);


/******     Administrator Role       ******/
INSERT INTO mjr_administrator_x_role
(administrator_id, 	role_id,    record_reg_id,record_upd_id,record_reg_date,record_upd_date)
VALUES 
/******      Steve     ******/
(2,			  		2,          0,0,current_timestamp,current_timestamp),
/******      John Doe     ******/
(3,			  		3,          0,0,current_timestamp,current_timestamp);