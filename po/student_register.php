<?php
require 'db_functions.php';

$obj = new DB_Functions();
$data = array('error'=>'true');
error_log($_POST["registerid"]);
if(isset($_POST["rollno"]) && isset($_POST["registerid"]))
{
	$result = $obj->register($_POST["rollno"],$_POST["registerid"]);
	if($result === "Student already registered")
	{
		$data["error"] = true;
		$data["error_details"] = $result;
		echo json_encode($data);
	}
	else if($result === "invalid registration id or rollno")
	{
		$data["error"] = true;
		$data["error_details"] = $result;
		echo json_encode($data);
	}
	else
	{
		$data["error"] = false;
		$data["status"] = "Successfully Registered";
		echo json_encode($data);
	}
}
else
{
	$data["error"] = true;
	$data["error_details"] = "required parameters are rollno and registerid";
	echo json_encode($data);
}
?>