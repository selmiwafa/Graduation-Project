<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["email"]))
{
    $email=$_GET["email"];

    $req=mysqli_query($cnx,"delete from user where email='$email'");
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