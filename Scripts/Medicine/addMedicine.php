<?php

include ("db_connect.php");

$response=array();

if(
   isset($_GET["barcode"]) &&
   isset($_GET["med_name"]) &&
   isset($_GET["quantity"] ) &&
   isset($_GET["exp_date"])
   )
   {
      $barcode=$_GET["barcode"];
      $med_name=$_GET["med_name"];
      $quantity=$_GET["quantity"];
      $description=$_GET["description"];
      $exp_date=$_GET["exp_date"];
      $user=$_GET["user"];

      $req1=mysqli_query($cnx,"select * from medicine where ( barcode='$barcode' && user='$user')");
      if(mysqli_num_rows($req1)==0)
      {
         $req=mysqli_query($cnx,"insert into medicine(barcode,med_name,quantity,description,exp_date,user) values ('$barcode','$med_name','$quantity','$description','$exp_date','$user')");
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
