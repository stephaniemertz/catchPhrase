<!--
// Create database and table
-->

<?php

// function makeDatabases() {
// 	include 'dbvars.php';

// 	// Connect to MySQL
// 	$conn = mysql_connect($servername,"root",$rootpass);

// 	// Create database
// 	$query = "CREATE DATABASE catchphraseDb;";
// 	$succ = mysql_query($query);

// 	if (!$succ) {
// 		return false;
// 	}

// 	$query = "Use catchphraseDb;";
// 	mysql_query($query);

// 	$query = "GRANT ALL ON catchphraseDb.* TO 'root'@'$servername';";
// 	mysql_query($query);

// 	$query = "SET PASSWORD FOR 'root'@'$servername' = PASSWORD('$rootpass');";
// 	mysql_query($query);


// 	// Create table for login information
// 	$query = "CREATE TABLE `loginInfo` (
// 		`username` varchar(40) NOT NULL,
// 		`firstname` varchar(15) NOT NULL,
// 		`lastname` varchar(20) NOT NULL,
// 		`email` varchar(40) NOT NULL,
// 		`gender` int,
// 		`age` int,
// 		`location` varchar(255),
// 		`education` int,
// 		PRIMARY KEY (`email`)
// 		) ENGINE=MYISAM CHARSET=utf8;";
// 	mysql_query($query);

// 	return true;
// }

// function addUser() {
	include 'dbvars.php';
	include 'connect.php';

	if (isset($_POST['username']) && isset($_POST['email'])){
        $username = $_POST['username'];
        $email = $_POST['email'];
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $gender = $_POST['gender'];
        $age = $_POST['age'];
        $location = $_POST['location'];
        $education = $_POST['education'];
        $query = "INSERT INTO `user` (username, firstname, lastname, email, gender, age, location, education) VALUES ('$username', '$firstname', '$lastname', '$email', '$gender', '$age', '$location', '$education')";
        $result = mysql_query($query);
        if($result){
            $msg = "User Created Successfully.";
        }
    }
// }
?>