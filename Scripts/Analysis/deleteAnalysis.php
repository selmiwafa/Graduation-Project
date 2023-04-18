<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]) 
    && isset($_GET["analysis_name"]))
{
    $user=$_GET["user"];
    $analysis_name=$_GET["analysis_name"];

    $req=mysqli_query($cnx,"delete from analyses where (analysis_name='$analysis_name' && user='$user')");
    if($req)
    {
        $response["success"]=1;
        $response["message"]="deleted successfully!";
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
    $response["message"]="required filed is missing!";
    echo json_encode($response);
    

}




?>