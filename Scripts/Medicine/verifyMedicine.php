<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["barcode"]) && isset($_GET["user"]))
{
    $user=$_GET["user"];
    $barcode=$_GET["barcode"];

    $req=mysqli_query($cnx,"select * from medicine where (barcode='$barcode' && user='$user')");
    if(mysqli_num_rows($req)>0)
    {
        $response["success"]=1;
        $response["message"]="existing!";
        echo json_encode($response);
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no medicine found!";
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