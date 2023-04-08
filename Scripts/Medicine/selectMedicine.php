<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]))
{
    $user=$_GET["user"];

    $req=mysqli_query($cnx,"select * from medicine where user='$user'");
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
        $response["number"]=mysqli_num_rows($req);
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