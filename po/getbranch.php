<?php

require 'db_functions.php';

$obj = new DB_Functions();
if(isset($_POST['rollno']))
{	
$result = $obj->getbranch($_POST['rollno']);
echo json_encode($result);
}
?>