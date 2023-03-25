<?php

include ("db_connect.php");
$response=array();
if(isset($_GET["email"]) &&
   isset($_GET["name"]) &&
   isset($_GET["birthdate"]) &&
   isset($_GET["password"]) && 
   isset($_GET["adress"] ))
  {
    $email=$_GET["email"];
    $name=$_GET["name"];
    $birthdate=$_GET["birthdate"];
    $password=$_GET["password"];
    $adress=$_GET["adress"];

    $req=mysqli_query($cnx,"update user set name='$name', birthdate='$birthdate',password='$password',adress='$adress' where email='$email'");
    $selectreq=mysqli_query($cnx,"select * from user where email='$email'");
    $cur=mysqli_fetch_array($selectreq);
    if($req)
    {
        $tmp=array();
        $response["user"]=array();
        $tmp["email"]=$cur["email"];
        $tmp["name"]=$cur["name"];
        $tmp["birthdate"]=$cur["birthdate"];
        $tmp["password"]=$cur["password"];
        $tmp["adress"]=$cur["adress"];

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
    $response["message"]="required filed is missing!";
    echo json_encode($response);
  }
?>
