<?php

include ("db_connect.php");

$response=array();

if(
        isset($_GET["id"]) &&
        isset($_GET["barcode"]) &&
        isset($_GET["quantity"]) &&
        isset($_GET["date"]) &&
        isset($_GET["user"])
    )
   {
      $id=$_GET["id"];
      $barcode=$_GET["barcode"];
      $quantity=$_GET["quantity"];
      $date=$_GET["date"];
      $user=$_GET["user"];
      $req1=mysqli_query($cnx,"select * from donations where ( id='$id' && user='$user')");
      if(mysqli_num_rows($req1)==0)
      {
         $req=mysqli_query($cnx,"insert into donations (id,barcode,quantity,date,user) values ('$id','$barcode','$quantity','$date','$user')");
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