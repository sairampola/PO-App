<?php
require 'db_functions.php';

$obj = new DB_Functions(); 
$err_msg = "invali details";
$data = array("error"=>true);
if(isset($_POST['rollno']) && isset($_POST['password']))
{
	$rollno = $_POST['rollno'];
	$password = $_POST['password'];
    $result = $obj->s_login($rollno, $password);
    error_log($result);
    if($result === "false")
    {    	
        error_log("invalid");
	$data["error"] = TRUE;
	$data["error_details"] = "invalid rollno or password";
    echo json_encode($data);
    } 
    else if($result === "not registered")
    {
        error_log("xyz");
        $data["error"] = true;
        $data["error_details"] = "entered rollno has not registered, Please register in order to login";
        echo json_encode($data);
    }
    else
    	{
            //$temp = mysql_fetch_array($result);
             error_log("logged in");      
    		$data["error"] = false;
    		$data["name"] = $result;
            echo json_encode($data);
    	}

}
else
{
	$data["error"] = TRUE;
	$data["error_details"] = "expected parameters are rollno and password";
	echo json_encode($data);
}
?>