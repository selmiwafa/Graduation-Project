<?php
//$ip = $_SERVER['REMOTE_ADDR'];
$cnx=mysqli_connect("192.168.43.205","root","");
if(!$cnx)
{
    echo "connexion error";
}

$db=mysqli_select_db($cnx,"healthbuddy");
if(!$db)
{
    echo "data base connexion error";
}
?>
