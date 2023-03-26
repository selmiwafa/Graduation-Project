<?php
$cnx=mysqli_connect("localhost","root","");
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
