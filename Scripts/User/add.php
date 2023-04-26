<?php

include ("db_connect.php");

$response=array();
if(
   isset($_GET["email"]) &&
   isset($_GET["name"]) &&
   isset($_GET["birthdate"]) &&
   isset($_GET["password"]) && 
   isset($_GET["number"]) && 
   isset($_GET["adress"] )
   )
   
   {
    $email=$_GET["email"];
    $name=$_GET["name"];
    $birthdate=$_GET["birthdate"];
    $password=$_GET["password"];
    $adress=$_GET["adress"];
    $number=$_GET["number"];

    $req=mysqli_query($cnx,"insert into user(email,name,birthdate,password,adress,number) values ('$email','$name','$birthdate','$password','$adress','$number') ");
    if($req)
    {
        $response["success"]=1;
        $response["message"]="inserted!";

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

