<?php

include ("db_connect.php");

$response=array();

if(
   isset($_GET["analysis_name"]) &&
   isset($_GET["analysis_date"]) &&
   isset($_GET["result"] ) &&
   isset($_GET["user"])
   )
   {
      $analysis_name=$_GET["analysis_name"];
      $analysis_date=$_GET["analysis_date"];
      $result=$_GET["result"];
      $user=$_GET["user"];

      $req1=mysqli_query($cnx,"select * from user_analyses where ( analysis_name='$analysis_name' && user='$user')");
      if(mysqli_num_rows($req1)==0)
      {
         $req=mysqli_query($cnx,"insert into user_analyses(analysis_name,analysis_date,result,user) values ('$analysis_name','$analysis_date','$result','$user')");
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

else
{
   $response["success"]=0;
   $response["message"]="required filed is missing";
   echo json_encode($response);
}
?>