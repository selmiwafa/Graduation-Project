<?php
    include ("db_connect.php");
    $response=array();
    if(
        isset($_GET["old_analysis_name"]) &&
        isset($_GET["new_analysis_name"]) &&
        isset($_GET["analysis_date"]) &&
        isset($_GET["result"] ) &&
        isset($_GET["user"])
    )
   {
        $old_analysis_name=$_GET["old_analysis_name"];
        $new_analysis_name=$_GET["new_analysis_name"];
        $analysis_date=$_GET["analysis_date"];
        $result=$_GET["result"];
        $user=$_GET["user"];

        $req=mysqli_query($cnx,"update analyses set analysis_name='$new_analysis_name', 
        analysis_date='$analysis_date', result='$result'
        where (analysis_name='$old_analysis_name' && user='$user')");
        if($req)
        {
            $response["success"]=1;
            $response["message"]="updated successfully!";
            echo json_encode($response);
        }
        else
        {
            $response["success"]=0;
            $response["message"]="request error!";
            echo json_encode($response);
        }
    }
    else
    {
        $response["success"]=0;
        $response["message"]="required filed is missing";
        echo json_encode($response);
    }
?>