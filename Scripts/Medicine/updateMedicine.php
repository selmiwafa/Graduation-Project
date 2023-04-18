<?php
    include ("db_connect.php");
    $response=array();
    if(
        isset($_GET["user"]) &&
        isset($_GET["old_barcode"]) &&
        isset($_GET["barcode"]) &&
        isset($_GET["med_name"]) &&
        isset($_GET["quantity"] ) &&
        isset($_GET["description"]) &&
        isset($_GET["exp_date"])
    )
   {
        $user=$_GET["user"];
        $old_barcode=$_GET["old_barcode"];
        $barcode=$_GET["barcode"];
        $med_name=$_GET["med_name"];
        $quantity=$_GET["quantity"];
        $description=$_GET["description"];
        $exp_date=$_GET["exp_date"];

        $req=mysqli_query($cnx,"update medicine set barcode='$barcode', 
        med_name='$med_name', quantity='$quantity',description='$description' 
        where (barcode='$old_barcode' && user='$user')");
        if($req)
        {
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
        $response["message"]="required filed is missing";
        echo json_encode($response);
    }
?>