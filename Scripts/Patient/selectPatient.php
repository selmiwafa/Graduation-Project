<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]))
{
    $user=$_GET["user"];

    $req=mysqli_query($cnx,"select * from patients where user='$user'");
    if(mysqli_num_rows($req)>0)
    {
        $marray=array();
        $response["patients"]=array();
        while($patients=mysqli_fetch_array($req))
        {
            $marray["patient_name"]=$patients["patient_name"];
            $marray["relationship"]=$patients["relationship"];
            $marray["patient_age"]=$patients["patient_age"];
            
            array_push($response["patients"],$marray);
        }
        $response["success"]=1;
        $response["number"]=mysqli_num_rows($req);
        $response["message"]="success selecting!";
        echo json_encode($response);
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no patients found!";
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