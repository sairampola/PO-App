<?php
require 'DB_Functions.php';
//this script is used to store the registration id of android app to database
$funobj = new DB_Functions();
if(isset($_POST['rollno']))
{
	$rollno = $_POST['rollno'];
	error_log($rollno);
	error_log('unregister');
   $funobj->UnsubscribeNotification($rollno);
   $data = array("error"=>false);
  echo json_encode($data);
}
else
{
	error_log('no post');
	 $data = array("error"=>false,"error_details"=>"no rollno in post");
  echo json_encode($data);
}
	
?>