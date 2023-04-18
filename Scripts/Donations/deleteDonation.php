<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]) 
    && isset($_GET["id"]))
{
    $user=$_GET["user"];
    $id=$_GET["id"];

    $req=mysqli_query($cnx,"delete from donations where (id='$id' && user='$user')");
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