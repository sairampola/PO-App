 <?php

    class DB_Functions {

        private $db;

        //put your code here
        // constructor
        function __construct() {
            include_once './db_connect.php';
            // connecting to database
            $this->db = new DB_Connect();
            $this->db->connect();
        }

        // destructor
        function __destruct() {

        }

        /**
         * Storing new user
         * returns user details
         */
        public function storeUser($rollno, $gcmregid) {
            // insert user into database
            $result = mysql_query("INSERT INTO gcm(rollno, regid, createdat) VALUES('$rollno', '$gcmregid', NOW())");
            // check for successful store
            if ($result) {
                // get user details
                $id = mysql_insert_id(); // last inserted id
                $result = mysql_query("SELECT * FROM gcm WHERE regid = '$gcmregid'") or die(mysql_error());
                // return user details
                if (mysql_num_rows($result) > 0) {
                    return mysql_fetch_array($result);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        /**
         * Get user by email and password
         */
        public function getUserByEmail($email) {
            $result = mysql_query("SELECT * FROM gcm WHERE email = '$email' LIMIT 1");
            return $result;
        }

        /**
         * Getting all users
         */
        public function getAllUsers() {
            $result = mysql_query("select * FROM gcm");
            return $result;
        }

        /**
         * Check user is existed or not
         */
        public function isUserExisted($id) {
            $result = mysql_query("SELECT regid from gcm WHERE regid = '$id'");
            $no_of_rows = mysql_num_rows($result);
            if ($no_of_rows > 0) {
                // user existed
                return true;
            } else {
                // user not existed
                return false;
            }
        }

    public function UnsubscribeNotification($rollno){
    $result=mysql_query("DELETE FROM gcm WHERE rollno = '$rollno'");
    error_log('query');
    echo $result;
    }    


    }

?>