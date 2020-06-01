<?php
$servername = "server174";
$username = "tonujajk_UPT";;
$password = "tyxgId-cygror-pekmy2";
$dbname = "tonujajk_UPT";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>
