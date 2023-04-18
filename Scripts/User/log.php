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
                $response["number_patients"]=mysqli_num_rows($req2);
                $response["message2"]="patients found";
            } 
            else 
            {
                $response["number_patients"]=0;
            }

            $req3=mysqli_query($cnx,"select * from medicine where user='$email'");
            if(mysqli_num_rows($req3)>0)
            {
                $marray=array();
                $response["medicine"]=array();
                while($medicine=mysqli_fetch_array($req3))
                {
                    $marray["barcode"]=$medicine["barcode"];
                    $marray["med_name"]=$medicine["med_name"];
                    $marray["quantity"]=$medicine["quantity"];
                    $marray["description"]=$medicine["description"];
                    $marray["exp_date"]=$medicine["exp_date"];
                    array_push($response["medicine"],$marray);
                }
                $response["number_medicine"]=mysqli_num_rows($req3);
            }
            else 
            {
                $response["number_medicine"]=0;
            }
            
            $req4=mysqli_query($cnx,"select * from analyses where user='$email'");
            if(mysqli_num_rows($req4)>0)
            {
                $aarray=array();
                $response["user_analyses"]=array();
                while($analysis=mysqli_fetch_array($req4))
                {
                    $aarray["analysis_name"]=$analysis["analysis_name"];
                    $aarray["analysis_date"]=$analysis["analysis_date"];
                    $aarray["result"]=$analysis["result"];
                    array_push($response["user_analyses"],$aarray);
                }
                $response["number_analyses"]=mysqli_num_rows($req4);
            }
            else 
            {
                $response["number_analyses"]=0;
            }

            $req5=mysqli_query($cnx,"select * from appointments where user='$email'");
            if(mysqli_num_rows($req5)>0)
            {
                $aparray=array();
                $response["appointments"]=array();
                while($apps=mysqli_fetch_array($req5))
                {
                    $aparray["app_id"]=$apps["app_id"];
                    $aparray["app_name"]=$apps["app_name"];
                    $aparray["app_date"]=$apps["app_date"];
                    $aparray["app_time"]=$apps["app_time"];
                    $aparray["app_type"]=$apps["app_type"];
                    array_push($response["appointments"],$aparray);
                }
                $response["number_apps"]=mysqli_num_rows($req5);
            }
            else 
            {
                $response["number_apps"]=0;
            }

            $req6=mysqli_query($cnx,"select * from donations where user='$email'");
            if(mysqli_num_rows($req6)>0)
            {
                $dparray=array();
                $response["donations"]=array();
                while($apps=mysqli_fetch_array($req6))
                {
                    $dparray["id"]=$apps["id"];
                    $dparray["barcode"]=$apps["barcode"];
                    $dparray["quantity"]=$apps["quantity"];
                    $dparray["date"]=$apps["date"];
                    array_push($response["donations"],$dparray);
                }
                $response["number_donations"]=mysqli_num_rows($req6);
            }
            else 
            {
                $response["number_donations"]=0;
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