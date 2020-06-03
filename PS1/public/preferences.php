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
    <link rel="stylesheet" href="css/preferences.css">
    <title>Greenhouse - Preferences</title>
</head>

<body>

<?php
include 'modules/navigation.php'
?>
    <section id="wall" class="card">
        <h4>Preferences</h4>
        <div class="form block">
            <form id="preferenceForm">
                <div class="input-field col s12">
                    <select id="kind">
                    </select>
                    <label>Greenhouse preferences</label>
                </div>
                <div class="input-field col s6">
                    <input name="light" id="light" type="number" required min="1" max="1023">
                    <label for="light">Light</label>
                </div>
                <div class="input-field col s6">
                    <input name="temperature" id="temperature" type="number" required min="1" max="1023">
                    <label for="temperature">Temperature</label>
                </div>
                <div class="input-field col s6">
                    <input name="water" id="water" type="number" required min="1" max="1023">
                    <label for="water">Water</label>
                </div>
                <input class="btn darken indigo" type="submit">
            </form>
        </div>
    </section>

    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <h4>Delete preference</h4>
            <p>Are you sure that you want to delete this preference from the database?<br>
                This will also delete all logs linked to it and this action can not be undone.</p>
        </div>
        <div class="modal-footer">
            <a href="#!" class="modal-close btn-flat">Abort</a>
            <a href="#!" class="modal-close btn-flat" id="deletePreference">Proceed</a>
        </div>
    </div>
    <script src="materialize/js/materialize.min.js"></script>
    <script src="js/preferences.js" defer></script>
</body>

</html>