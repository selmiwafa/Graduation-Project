<?php

    include_once "../config/dbconnect.php";
    
    $c_id=$_POST['record'];
    $query="Update donations set isAffected=1 where id='$c_id'";

    $data=mysqli_query($conn,$query);

    if($data){
        echo"Donation Accepted!";
    }
    else{
        echo"Not able to delete";
    }
    
?>