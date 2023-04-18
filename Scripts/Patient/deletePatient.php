<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"])
    && isset($_GET["patient_name"]))
{
    $user=$_GET["user"];
    $patient_name=$_GET["patient_name"];
    
    $req=mysqli_query($cnx,"delete from patients where (user='$user' && patient_name='$patient_name')");
    if($req)
    {
        $response["success"]=1;
        $response["message"]="deleted successfully!";
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
    $response["message"]="required filed is missing!";
    echo json_encode($response);
    

}




?>