<?php
require 'db_functions.php';

$obj = new DB_Functions();
$data = array('error'=>'true');
if(isset($_POST["rollno"]) && isset($_POST["password"]) && isset($_POST['branch']) && isset($_POST['name']) && isset($_POST['course']))
{
$result = $obj->save_student($_POST["rollno"],$_POST["password"],$_POST['name'], $_POST['course'],$_POST['branch']);
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
	$data["error_details"] = "password change unsuccessful";
	echo json_encode($data);
}
?>