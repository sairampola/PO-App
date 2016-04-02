<?php
require 'db_functions.php';

$obj = new DB_Functions();
$data = array('error'=>true);
if(isset($_POST['rollno']) && isset($_POST['jobid']))
{
	error_log("143");
	$result = $obj->applyjob($_POST['rollno'],$_POST['jobid']);
	if($result === "applied")
	{
		$data["error"] = false;
		$data["msg"] = "User already Registered";
		echo json_encode($data);
	}
	else if($result === "success")
	{
		$data["error"] = false;
		$data["msg"] = "Successfully Registered for the JOB";
		echo json_encode($data);
	}
	else if($result === "failed")
	{
		$data["error"] = true;
		$data["error_details"] = "error";
		echo json_encode($data);
	}


}
else 
{
	error_log("341");
	$data["error"] = true;
	$data["error_details"] = "required parameters are rollno and jobid";
	echo json_encode($data);
}
?>