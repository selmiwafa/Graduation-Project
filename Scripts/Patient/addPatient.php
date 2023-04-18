<?php

include ("db_connect.php");

$response=array();

if(
   isset($_GET["patient_id"]) &&
   isset($_GET["patient_name"]) &&
   isset($_GET["relationship"]) &&
   isset($_GET["patient_age"] ) &&
   isset($_GET["user"])
   )
   {
      $id=$_GET["patient_id"];
      $name=$_GET["patient_name"];
      $relationship=$_GET["relationship"];
      $age=$_GET["patient_age"];
      $user=$_GET["user"];

      $req1=mysqli_query($cnx,"select * from patients where user='$user'");
      $number=mysqli_num_rows($req1);
      if($number<2)
      {
         $req=mysqli_query($cnx,"insert into patients(patient_id,patient_name,relationship,patient_age,user) values ('$id','$name','$relationship','$age','$user')");
         if($req)
         {
            $req2=mysqli_query($cnx,"select * from patients where user='$user'");
            $tmp=array();
            $response["patients"]=array();
            while($cur=mysqli_fetch_array($req2)){
               $tmp["patient_id"]=$cur["patient_id"];
               $tmp["patient_name"]=$cur["patient_name"];
               $tmp["patient_age"]=$cur["patient_age"];
               $tmp["relationship"]=$cur["relationship"];
               array_push($response["patients"],$tmp);
            }
            $response["number"]=mysqli_num_rows($req2);
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
            $response["message"]="You can only have 2 patients maximum!";
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