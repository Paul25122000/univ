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
    <link rel="stylesheet" href="css/logs.css">
    <link rel="icon" href="favicon.png" type="image/x-icon" />
    <title>GreenHouse - Logs</title>
</head>

<body>
    <?php
    include 'modules/navigation.php'
    ?>
    <section id="wall">
        <div class="pick_time card">
            <h4>Logs from:</h4>
            <form>
                <input type="text" placeholder="Date" class="datepicker">
                <input type="text" placeholder="Time" class="timepicker">
                <div class="filter_actions d_fl a_c j_sb">
                    <button id="clearFilter" class="btn darken outline">Clear filter</button>
                    <input type="button" value="Filter" onclick="setFilter()" class="btn darken indigo">
                </div>
            </form>
        </div>
        <div id="controls" class="card d_fl a_c j_sb">
            <div class="d_fl a_c">
                <span>Auto reload: </span>
                <div class="switch">
                    <label class="d_fl a_c">
                        Off
                        <input type="checkbox" checked>
                        <span class="lever"></span>
                        On
                    </label>
                </div>
            </div>
            <button class="btn outline darken modal-trigger" href="#modal1">Delete all records</button>
        </div>
        <div class="row tabs_container">
            <div class="col s12">
                <ul class="tabs indigo">
                    <li class="tab col s4"><a href="#light" class="active">Light</a></li>
                    <li class="tab col s4"><a href="#temperature">Temperature</a></li>
                    <li class="tab col s4"><a href="#water">Water</a></li>
                </ul>
            </div>
            <div id="light" class="col s12"></div>
            <div id="temperature" class="col s12"></div>
            <div id="water" class="col s12"></div>
        </div>
        <div id="logs" class="card">
            <table class="striped centered">
                <thead>
                    <tr>
                        <th>Culture</th>
                        <th class="date_cell">Date</th>
                        <th>Time</th>
                        <th>Value</th>
                        <th>Setting</th>
                        <th width="60">Action</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </section>

    <div id="modal1" class="modal">
        <div class="modal-content">
            <h4>Delete all records</h4>
            <p>Are you sure that you want to delete all logs from the database?<br>
                This action can not be undone</p>
        </div>
        <div class="modal-footer">
            <a href="#!" class="modal-close btn-flat">Abort</a>
            <a href="#!" class="modal-close btn-flat" id="deleteAll">Proceed</a>
        </div>
    </div>
    <script src="materialize/js/materialize.min.js"></script>
    <script src="js/logs.js" defer></script>
</body>

</html>