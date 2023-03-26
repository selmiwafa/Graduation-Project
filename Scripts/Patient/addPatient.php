<?php

include ("db_connect.php");

$response=array();

if(
   isset($_GET["name"]) &&
   isset($_GET["relationship"]) &&
   isset($_GET["age"] ) &&
   isset($_GET["user"])
   )
   {
      $name=$_GET["name"];
      $relationship=$_GET["relationship"];
      $age=$_GET["age"];
      $user=$_GET["user"];

      $req1=mysqli_query($cnx,"select * from patients where user='$user'");
      if(mysqli_num_rows($req1)<2)
      {

         $req=mysqli_query($cnx,"insert into patients(patient_name,relationship,patient_age,user) values ('$name','$relationship','$age','$user')");
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
            $response["message"]="Maximum patients reached";
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