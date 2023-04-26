<?php

include ("db_connect.php");

$response = array();

if (isset($_GET["pres_id"]) &&
   isset($_GET["pres_name"]) &&
   isset($_GET["pres_start"]) &&
   isset($_GET["pres_end"]) &&
   isset($_GET["owner_type"]) &&
   isset($_GET["owner_id"]))
{
   $owner_type=$_GET["owner_type"];
   if($owner_type == "user")
   {
      $pres_id = $_GET['pres_id'];
      $pres_name = $_GET['pres_name'];
      $pres_start = $_GET['pres_start'];
      $pres_end = $_GET['pres_end'];
      $owner_id = $_GET['owner_id'];
      $mysqli = new mysqli('192.168.43.205', 'root', '', 'healthbuddy');
      
      $stmt = $mysqli->prepare("INSERT INTO prescriptions (id, pres_name, start_date, end_date, user) VALUES (?, ?, ?, ?, ?)");
      $stmt->bind_param("sssss", $pres_id, $pres_name, $pres_start, $pres_end, $owner_id);
      $stmt->execute();
      
      $summaryListJson = $_GET["summarylist"];
      $summaryList = json_decode($summaryListJson, true);

      foreach ($summaryList as $presMedicine) {
         $barcode = $presMedicine["barcode"];
         $dose = $presMedicine["dose"];
         $period = $presMedicine["period"];
         $tpw = $presMedicine["tpw"];
         $frequency = $presMedicine["frequency"];
         $other = $presMedicine["other"];

         $stmt2 = $mysqli->prepare("INSERT INTO prescription_details (barcode, pres_id, dose, period, times_per_week, frequency, other_details) VALUES (?, ?, ?, ?, ?, ?, ?)");
         $stmt2->bind_param("sssssss", $barcode, $pres_id, $dose, $period, $tpw, $frequency, $other);
         $stmt2->execute();
      }
      
      $response["success"] = 1;
      $response["message"] = "Inserted!";
      echo json_encode($response);
   }
   else {
      $pres_id = $_GET['pres_id'];
      $pres_name = $_GET['pres_name'];
      $pres_start = $_GET['pres_start'];
      $pres_end = $_GET['pres_end'];
      $owner_id = $_GET['owner_id'];
      $mysqli = new mysqli('192.168.43.205', 'root', '', 'healthbuddy');
      
      $stmt = $mysqli->prepare("INSERT INTO prescriptions (id, pres_name, start_date, end_date, patient_id) VALUES (?, ?, ?, ?, ?)");
      $stmt->bind_param("sssss", $pres_id, $pres_name, $pres_start, $pres_end, $owner_id);
      $stmt->execute();
      
      $summaryListJson = $_GET["summarylist"];
      $summaryList = json_decode($summaryListJson, true);


      foreach ($summaryList as $presMedicine) {
         $barcode = $presMedicine["barcode"];
         $dose = $presMedicine["dose"];
         $period = $presMedicine["period"];
         $tpw = $presMedicine["tpw"];
         $frequency = $presMedicine["frequency"];
         $other = $presMedicine["other"];

         $stmt2 = $mysqli->prepare("INSERT INTO prescription_details (barcode, pres_id, dose, period, times_per_week, frequency, other_details) VALUES (?, ?, ?, ?, ?, ?, ?)");
         $stmt2->bind_param("sssssss", $barcode, $pres_id, $dose, $period, $tpw, $frequency, $other);
         $stmt2->execute();
      }
   }
}
else
{
    $response["success"] = 0;
    $response["message"] = "Required field is missing!";
    echo json_encode($response);
}

?>
