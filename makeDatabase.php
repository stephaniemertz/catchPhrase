<?php
	include 'dbvars.php';

	// Connect to MySQL
	$conn = pg_connect("host=localhost user=root password=" . $rootpass . "dbname=catchphraseDb");

	// Create database
	if (!$succ) {
		return false;
	}

	$query = "GRANT ALL PRIVILEGES ON DATABASE catchphraseDb.* TO 'root'@'$servername';";
	$query .= "SET PASSWORD FOR 'root'@'$servername' = PASSWORD('$rootpass');";
	pg_query($conn, $query);


	// Create table for login information
	$query = "CREATE SEQUENCE Users_user_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;"
	pg_query($conn, $query);

  	$query = "CREATE TABLE Users
		(
		user_id int4 NOT NULL DEFAULT nextval('Users_user_id_seq'),
		firstname varchar(255) NOT NULL,
		lastname varchar(255) NOT NULL,
		email varchar(255),
		gender varchar(1),
		age varchar(2),
		location varchar(255),
		education varchar(255),
		UNIQUE (email),
		CONSTRAINT users_pk PRIMARY KEY (user_id)
		);";
	pg_query($conn, $query);

	return true;
?>