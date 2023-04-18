<?php

include ("db_connect.php");

$response=array();

if(
   isset($_GET["app_id"]) &&
   isset($_GET["app_name"]) &&
   isset($_GET["app_date"] ) &&
   isset($_GET["app_time"]) &&
   isset($_GET["app_type"]) &&
   isset($_GET["owner_type"] ) &&
   isset($_GET["owner_id"])
   )
   {
      $owner_type=$_GET["owner_type"];
      if($owner_type=="user")
      {
         $app_id=$_GET["app_id"];
         $app_name=$_GET["app_name"];
         $app_date=$_GET["app_date"];
         $app_time=$_GET["app_time"];
         $app_type=$_GET["app_type"];
         $owner_id=$_GET["owner_id"];

         $req1=mysqli_query($cnx,"select * from appointments where ( app_id ='$app_id' && user='$owner_id')");
         if(mysqli_num_rows($req1)==0)
         {
            $req=mysqli_query($cnx,"insert into appointments(app_id,app_name,app_date,app_time,app_type,user) 
            values ('$app_id','$app_name','$app_date','$app_time','$app_type','$owner_id')");
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
            $response["message"]="Already exists!";
            echo json_encode($response);
            
         }
      }
      else {
         $app_id=$_GET["app_id"];
         $app_name=$_GET["app_name"];
         $app_date=$_GET["app_date"];
         $app_time=$_GET["app_time"];
         $app_type=$_GET["app_type"];
         $owner_id=$_GET["owner_id"];

         $req1=mysqli_query($cnx,"select * from appointments where ( app_id ='$app_id' && patient_id='$owner_id')");
         if(mysqli_num_rows($req1)==0)
         {
            $req=mysqli_query($cnx,"insert into appointments(app_id,app_name,app_date,app_time,app_type,patient_id) 
            values ('$app_id','$app_name','$app_date','$app_time','$app_type','$owner_id')");
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
            $response["message"]="Already exists!";
            echo json_encode($response);
            
         }
      }
}

else
{
   $response["success"]=0;
   $response["message"]="required filed is missing";
   echo json_encode($response);
}
?>