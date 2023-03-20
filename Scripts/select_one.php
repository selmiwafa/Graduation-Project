<?php
include ("db_connect.php");
$response=array();

if(isset($_GET["email"]))
{
    $req=mysqli_query($cnx,"select* from user where email='$email'");
    if(mysqli_num_rows($req)>0)
    {
        $tmp=array();
        $response["user"]=array();
        $cur=mysqli_fetch_array($req);

        $tmp["email"]=$cur["email"];
        $tmp["name"]=$cur["name"];
        $tmp["birthdate"]=$cur["birthdate"];
        $tmp["password"]=$cur["password"];
        $tmp["adress"]=$cur["adress"];
        
        array_push($response["users"],$tmp);
        $response["success"]=1;
        echo json_encode($response);
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no data found";
        echo json_encode($response);
    }
}
else
{
    $response["success"]=0;
    $response["message"]="required field is missing";
    echo json_encode($response);
}






?>