<?php
include ("db_connect.php");
$response=array();
$data = json_decode(file_get_contents("php://input"));
$returnData = [];

if(isset($_GET["email"]) && isset($_GET["password"]))
{
    $email=$_GET["email"];
    $password=$_GET["password"];

    $req=mysqli_query($cnx,"select * from user where email='$email'");
    if(mysqli_num_rows($req)>0)
    {
        $cur=mysqli_fetch_array($req);
        if($password==$cur["password"])
        {
            $tmp=array();
            $response["user"]=array();
            $tmp["email"]=$cur["email"];
            $tmp["name"]=$cur["name"];
            $tmp["birthdate"]=$cur["birthdate"];
            $tmp["password"]=$cur["password"];
            $tmp["adress"]=$cur["adress"];

            $req2=mysqli_query($cnx,"select * from patients where user='$email'");
 
            if(mysqli_num_rows($req2)>0)
            {
                $parray=array();
                $response["patients"]=array();
                while($patients=mysqli_fetch_array($req2))
                {
                    $parray["patient_name"]=$patients["patient_name"];
                    $parray["patient_age"]=$patients["patient_age"];
                    $parray["relationship"]=$patients["relationship"];
                    array_push($response["patients"],$parray);
                }

                $response["number"]=mysqli_num_rows($req2);
                $response["message2"]="patients found";
            }
            $response["success"]=1;
            $response["message"]="logged";
            array_push($response["user"],$tmp);
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