<?php

include ("db_connect.php");

$response = array();

if (isset($_GET["pres_id"]) &&
   isset($_GET["pres_name"]) &&
   isset($_GET["pres_start"] ) &&
   isset($_GET["pres_end"]) )

{
    $pres_id = $_GET["pres_id"];
    $pres_name = $_GET["pres_name"];
    $pres_start_date = $_GET["pres_start"];
    $pres_end_date = $_GET["pres_end"];

    $barcode = $_GET["barcode"];
    $dose = $_GET["dose"];
    $period = $_GET["period"];
    $tpw = $_GET["tpw"];
    $frequency = $_GET["frequency"];
    $other = $_GET["other"];

   
   $mysqli = new mysqli('192.168.43.205', 'root', '', 'healthbuddy');

   $mysqli->begin_transaction();

   $req = mysqli_query($mysqli, "INSERT INTO prescriptions (id, pres_name, start_date, end_date,user)
   VALUES ('$pres_id','$pres_name', '$pres_start_date', '$pres_end_date','$user')");

   $prescription_id = $mysqli->insert_id;

   $req2 = mysqli_query($mysqli, "INSERT INTO prescription_details 
   (barcode, pres_id,  dose, frequency, period, times_per_week, other_details)
   VALUES ('$barcode', '$pres_id', '$dose', '$frequency', '$period', '$tpw', '$other')");

   if ($req && $req2)
   {
      $mysqli->commit();

      $response["success"] = 1;
      $response["message"] = "Inserted!";
      echo json_encode($response);
   }
   else
   {
      $mysqli->rollback();

      $response["success"] = 0;
      $response["message"] = "Request error!";
      echo json_encode($response);
   }

   $mysqli->close();
    
   
}
else
{
    $response["success"] = 0;
    $response["message"] = "Required field is missing!";
    echo json_encode($response);
}

?>
