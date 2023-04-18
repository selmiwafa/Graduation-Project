<?php
include ("db_connect.php");
$response=array();
if(isset($_GET["user"]))
{
    $user=$_GET["user"];

    $req=mysqli_query($cnx,"select * from analyses where user='$user'");
    if(mysqli_num_rows($req)>0)
    {
        $marray=array();
        $response["analysis"]=array();
        while($analysis=mysqli_fetch_array($req))
        {
            $marray["analysis_name"]=$analysis["analysis_name"];
            $marray["analysis_date"]=$analysis["analysis_date"];
            $marray["result"]=$analysis["result"];
            array_push($response["analysis"],$marray);
        }
        $response["success"]=1;
        $response["number"]=mysqli_num_rows($req);
        $response["message"]="success selecting!";
        echo json_encode($response);
    }
    else
    {
        $response["success"]=0;
        $response["message"]="no analyses found!";
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