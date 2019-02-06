<?php
include 'connect.php';

	if(isset($_POST['email'])&& isset($_POST['password']))
	{
		$fname =  $_POST['fname'];
		$sname =  $_POST['sname'];		
		$email =  $_POST['email'];		
		$password =  $_POST['password'];

		 $result = mysqli_query($conn, "SELECT email FROM user WHERE email = '".$email."'"); 
		 if(mysqli_num_rows($result) > 0){ 
 				echo "email exist";
 				exit;
 			} 
 		else{
	
			$query = "INSERT INTO customer (fname, sname, email, password)
		VALUES('$fname', '$sname', '$email', '$password')";
			
			if (mysqli_query($conn, $query))
			{
				echo ' sign in successful'
				exit;
  			}
  			else{
				echo 'error sign in '
				exit;
			}
		}
		
}
?>
