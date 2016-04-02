  <?php

    class DB_Connect {

        // constructor
        function __construct() {

        }

        // destructor
        function __destruct() {
            // $this->close();
        }

        // Connecting to database
        public function connect() {
            require_once 'dbconfig.php';
            // connecting to mysql
            $con = mysql_connect('127.0.0.1:3306', DB_USER, '') or die(mysql_error());
            // selecting database
            mysql_select_db(DB_DATABASE);

            // return database handler
            return $con;
        }

        // Closing database connection
        public function close() {
            mysql_close();
        }

    } 
    ?>