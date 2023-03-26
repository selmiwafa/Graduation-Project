<?php
include ("db_connect.php");
$response=array();
$data = json_decode(file_get_contents("php://input"));
$returnData = [];

if(isset($_GET["email"]) && isset($_GET["patient_name"]))
{
    $email=$_GET["email"];
    $patient_name=$_GET["patient_name"];

    $req=mysqli_query($cnx,"select * from patients where user='$email'");
    if(mysqli_num_rows($req)>0)
    {
        $cur=mysqli_fetch_array($req);
        if($patient_name==$cur["patient_name"])
        {
            $tmp=array();
            $response["patients"]=array();
            $tmp["patient_name"]=$cur["patient_name"];
            $tmp["user"]=$cur["user"];
            $tmp["relationship"]=$cur["relationship"];
            $tmp["age"]=$cur["age"];
           
            
            $response["success"]=1;
            $response["message"]="patient saved";
            array_push($response["user"],$tmp);
            echo json_encode($response);
        }
        else
        {
            $response["success"]=0;
            $response["message"]="wrong patient_name";
            echo json_encode($response);
        }
        
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no patient found";
        echo json_encode($response);
    }
}
else if (mysqli_num_rows($req)==0)
{
    $response["success"]=0;
    $response["message"]="required field is missing";
    echo json_encode($response);
}

?>