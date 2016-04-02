<?php
// API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AIzaSyAwDaEGjR8CMAweKT9XQb5gZzP_-oMyQzs' );
$registrationIds = array( 'cYH_3FwYqto:APA91bE645VHLHmEZPHw4NOFxAPjDCwuHeEeUNBJvEXv8t2qZQUfTgmd6Aeg_ZTICjeUCTcyYeJsBrNT1vpxK4XFncb3CzKbgR5W8FI0QDJwsAWZr2GgnMI-anjJCr9uefutGyMqqpaI' );
// prep the bundle
$msg = array
(
	'title' => 'Google campus recuriment',
	'don' => '1234',
	'id' => '987'
);
$fields = array
(
	'registration_ids' 	=> $registrationIds,
	'data'			=> $msg
);
 
$headers = array
(
	'Authorization: key=' . API_ACCESS_KEY,
	'Content-Type: application/json'
);
 echo json_encode($fields);
$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
$result = curl_exec($ch );
curl_close( $ch );
echo $result;
?>