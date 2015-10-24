<!--
// Add user to database
-->

<?php
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
        $result = pq_query($conn, $query);
        if($result){
            $msg = "User Created Successfully.";
        }
    }
// }
?>