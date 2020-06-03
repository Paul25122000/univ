<?php
session_start();

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="materialize/css/materialize.min.css">
    <link rel="icon" href="favicon.png" type="image/x-icon" />
    <link rel="stylesheet" href="css/main.css">
    <title>Greenhouse - Home</title>
</head>

<body>
    <?php
    include 'modules/navigation.php'
    ?>
    <section id="wall" class="card">
        <h4>Greenhouse data</h4>
        <div class="heading d_fl a_c">
            <h6 class="d_fl a_c">Current preference</h6>
            <span>Gangiubas</span>
        </div>
        <div class="data">
            <div class="item">
                <span>Light:</span>
                <b></b>
            </div>
            <div class="item">
                <span>Temperature:</span>
                <b></b>
            </div>
            <div class="item">
                <span>Water:</span>
                <b></b>
            </div>
        </div>
    </section>
    <script src="materialize/js/materialize.min.js"></script>
    <script src="js/main.js" defer></script>
</body>

</html>