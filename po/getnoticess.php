<?php

require 'db_functions.php';

$obj = new DB_Functions();

$result = $obj->get_noticess(50);
echo json_encode($result);
?>