<?php
require 'DB_Functions.php';
//this script is used to store the registration id of android app to database
$funobj = new DB_Functions();
if(isset($_POST['rollno']) !== null &&isset($_POST['regid']) !== null)
{
	$rollno = $_POST['rollno'];
	$regid = $_POST['regid'];
	error_log($email);
	error_log($regid);
   $result = $funobj->storeUser($rollno,$regid);
  echo 'success';
}
else
{
	echo 'requried parameters are email and regid';
}
	
?>