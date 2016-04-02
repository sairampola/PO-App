<?php
require 'db_functions.php';


$obj = new DB_Functions();
$data = array("error"=>true);
if(isset($_POST['id']))
{
    $result = $obj->get_notice_details(intval($_POST['id']));    	
    	echo json_encode(mysql_fetch_object($result));   
}
else
{
	$data['error'] = true;
	$date['error_details'] = 'POST variable contails no title';
	echo json_encode($data);
}
echo $result;
?>