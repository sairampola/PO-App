<?php
require 'db_functions.php';

$obj = new DB_Functions();
$data = array("error"=>true);
if(isset($_POST["rollno"]) && isset($_POST["password"]))
{
$result = $obj->change_password($_POST["rollno"], $_POST["password"]);
   if($result)
    {
	$data["error"] = false;
	echo json_encode($data);
    }
    else
    {
	$data["error"] = true;
	$data["error_details"] = "password change unsuccessful";
	echo json_encode($data);
    }
}
else
{
	$data["error"] = true;
	$data["error_details"] = "required parameters are rollno and password";
	echo json_encode($data);
}
?>