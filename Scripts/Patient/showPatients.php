<?php
include ("db_connect.php");
$response=array();
$data = json_decode(file_get_contents("php://input"));
$returnData = [];

if(isset($_GET["email"]))
{
    $email=$_GET["email"];
    $req=mysqli_query($cnx,"select * from patients where user='$email'");
    $number=mysqli_num_rows($req);
    if($number>0)
    {
        $cur=mysqli_fetch_array($req);
        if($password==$cur["password"])
        {
            
            $tmp=array();
            $response["patients"]=array();
            while($patients=mysqli_fetch_array($req)) {
                $tmp["email"]=$cur["email"];
                $tmp["name"]=$cur["name"];
                $tmp["birthdate"]=$cur["birthdate"];
                $tmp["password"]=$cur["password"];
                $tmp["adress"]=$cur["adress"];
                array_push($response["patients"],$tmp);
            }

            $response["success"]=1;
            $response["message"]="patients reloaded";
            $response["number"]=$number;
            echo json_encode($response);
        }
        else
        {
            $response["success"]=0;
            $response["message"]="wrong password";
            echo json_encode($response);
        }
        
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no user found";
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