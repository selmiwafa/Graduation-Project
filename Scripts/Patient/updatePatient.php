<?php

include ("db_connect.php");
$response=array();


if(
   isset($_GET["patient_name"]) &&
   isset($_GET["relationship"]) &&
   isset($_GET["patient_age"] ) &&
   isset($_GET["user"])
   )
   {
      $user=$_GET["user"];
      $patient_name=$_GET["patient_name"];
      $relationship=$_GET["relationship"];
      $patient_age=$_GET["patient_age"];

      $req=mysqli_query($cnx,"update patients set patient_name='$patient_name', relationship='$relationship',patient_age='$patient_age' where (user='$user' && patient_name='$patient_name')");
      $select_req=mysqli_query($cnx,"select * from patients where user='$user' && patient_name='$patient_name'");
      $cur=mysqli_fetch_array($select_req);
      if($req)
      {
        $tmp=array();
        $response["patient"]=array();
        $tmp["patient_name"]=$cur["patient_name"];
        $tmp["relationship"]=$cur["relationship"];
        $tmp["patient_age"]=$cur["patient_age"];

        $response["success"]=1;
        $response["message"]="updated successfully!";
        array_push($response["patient"],$tmp);
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