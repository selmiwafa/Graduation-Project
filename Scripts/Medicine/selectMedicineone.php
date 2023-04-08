<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["barcode"]) && isset($_GET["user"]))
{
    $user=$_GET["user"];
    $barcode=$_GET["barcode"];

    $req=mysqli_query($cnx,"select * from medicine where (barcode='$barcode'&& user='$user')");
    if(mysqli_num_rows($req)>0)
    {
        $marray=array();
        $response["medicine"]=array();
        while($medicine=mysqli_fetch_array($req))
        {
            $marray["barcode"]=$medicine["barcode"];
            $marray["med_name"]=$medicine["med_name"];
            $marray["quantity"]=$medicine["quantity"];
            $marray["description"]=$medicine["description"];
            $marray["exp_date"]=$medicine["exp_date"];
            array_push($response["medicine"],$marray);
        }
        $response["success"]=1;
        $response["message"]="success selecting!";
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