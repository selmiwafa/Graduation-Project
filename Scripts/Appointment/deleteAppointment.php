<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]) 
    && isset($_GET["app_id"]))
{
    $user=$_GET["user"];
    $app_id=$_GET["app_id"];

    $req=mysqli_query($cnx,"delete from appointments where (app_id='$app_id' && user='$user')");
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