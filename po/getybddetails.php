<?php

require 'db_functions.php';

$obj = new DB_Functions();
if(isset($_POST['jobid']))
{
	$result = $obj->getybddetails($_POST['jobid']);
    echo json_encode(mysql_fetch_object($result));
}

?>