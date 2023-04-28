<?php

    include_once "../config/dbconnect.php";
    
    $c_id=$_POST['record'];
    $query="DELETE FROM donations where id='$c_id'";

    $data=mysqli_query($conn,$query);

    if($data){
        echo"Donation Rejected!";
    }
    else{
        echo"Not able to delete";
    }
    
?>