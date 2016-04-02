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
        function s_login($rollno, $password)
        {
            //$n ="".mysql_query("select isregistered from studentmbd where rollno = '$rollno' and password = '$password'");
            
            //error_log((string)$n);

            $result = mysql_query("SELECT * from studentmbd where rollno = '$rollno' and password = '$password'") or die(mysql_error());
            $test = mysql_fetch_array($result);
            if($test['isregistered'] == 0)
            {
                error_log("not registered");
                $r = "not registered";
                return $r;
            }
            else if(mysql_num_rows($result)>0)
            {
                error_log("true");
                return $test['name'];
            }
            else
            {
                error_log("false");
                return "false";
            }
        }
        function register($rollno, $register_id)
        {
            $result = mysql_query("select isregistered from studentmbd where rollno = '$rollno' and registrationcode = '$register_id'")
            or die(mysql_error());
             if($result == 1)
             {
                return "Student already registered";
             }       
            else if(mysql_num_rows($result)>0)
                {
                    //query to set the isregistered field in student mbd
                   // $a = mysql_query("update studentmbd set isregistered = 1 where rollno = '$rollno'")
                   // or die(mysql_error());    
                    return "Successfully Registered";
                }
            else
                return "invalid registration id or rollno";

        }
        function save_student($rollno, $password, $name, $course, $branch)
        {
           $result = mysql_query("update studentmbd set password = '$password',name = '$name', course = '$course', branch = '$branch' where rollno = '$rollno'")
           or die(mysql_error());
           $a = mysql_query("update studentmbd set isregistered = 1 where rollno = '$rollno'")
           or die(mysql_error());
           return true;
        }
        function change_password($rollno, $password)
        {
            $result = mysql_query("update studentmbd set password = '$password' where rollno = '$rollno'")
            or die(mysql_error());
            return true;
        }
        function get_noticess($count)
        {
            $result = mysql_query("SELECT title,don,id from notice order by id DESC LIMIT $count");
            $emparray = array();
    while($row =mysql_fetch_object($result))
    {
        $emparray[] = $row;
    }
   
            return json_encode($emparray);
        }
        function get_notice_details($id)
        {
            $result = mysql_query("select content from notice where id=$id")
                      or die(mysql_error());
                      //echo json_encode(mysql_fetch_object($result));
                      
            return $result;
        }
        function getybds($count)
        {
            $result = mysql_query("SELECT name,crp_details.jobid,lastdate,role,be,me,mtech,mca,bme,civil,cse,ece,eee,mech from crp_details,orglist,eligible_courses where id = companyid and crp_details.jobid = eligible_courses.jobid")
            or die(mysql_error());
            $emparray = array();
            while ($row = mysql_fetch_object($result)) {
                $emparray[] = $row;
            }
            return json_encode($emparray);

        }
        function getbranch($rollno)
        {
            $result = mysql_query("select branch from studentmbd where rollno = 0105108")
            or die(mysql_error());
            return mysql_fetch_object($result);
        }
        function getybddetails($jobid)
        {
            $result = mysql_query("SELECT name,lastdate,role,recruitment_type,cut_off,package,service_bond from crp_details,orglist where companyid = id and jobid = '$jobid'")
            or die(mysql_error());
            //echo json_encode(mysql_fetch_object($result));
            return $result;
        }
        function applyjob($rollno,$jobid)
        {
            $result = mysql_query("Select * from jobapplications where rollno = '$rollno' and jobid = '$jobid'")
            or die(mysql_error());
            if(mysql_num_rows($result)>0)
                return "applied";
            else
            {
                $temp = mysql_query("insert into jobapplications(rollno,jobid,result) values('$rollno','$jobid','Applied')");
                if($temp)
                    return "success";
                else
                    return "failed";
            }
        }
}

?>