<?php

include '../../modules/config.php';
$method = $_SERVER['REQUEST_METHOD'];


$date = new DateTime("now", new DateTimeZone('Europe/Bucharest') );
$timestamp = $date->format('Y-m-d H:i:s');

function listData()
{
    global $conn, $table;
    try {
        $stmt = $conn->prepare("SELECT * FROM GH_preferences");
        $stmt->execute();
        $stmt->bind_result($id, $current, $currentId, $light, $temperature, $water);
        $stmt->fetch();
        $data = [
        	'id' => $id,
        	'current' => $current,
        	'time' => $currentId,
        	'light' => $light,
        	'temperature' => $temperature,
        	'water' => $water
        ];
        echo json_encode($data);
    } catch (mysqli_sql_exception $e) {
        echo "MySQLi Error Code: " . $e->getCode() . "<br />";
        echo "Exception Msg: " . $e->getMessage();
        exit();
    }
};

function postRecord($record)
{
    global $conn;
    $t = time();
    $submit_time = date("Y-m-d-H:i:s", $t);

    $sql = "INSERT INTO GH_preferences (`current`, `currentId`, `light`, `temperature`, `water`)
          VALUES ('$record->current', '$record->currentId', '$record->light', '$record->temperature', '$record->water')";
    if ($conn->query($sql) === TRUE) {
        echo json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

function deleteRecord($record)
{
    global $conn;
    $sql = "DELETE FROM GH_preferences WHERE id =$record->id";
    if ($conn->query($sql) === TRUE) {
        echo json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

function updateRecord($record)
{
    global $conn;
    $sql = "UPDATE `preferences` SET `light`='$record->light', `temperature`='$record->temperature',  `water`='$record->water' WHERE id = $record->id";
    if ($conn->query($sql) === TRUE) {
        echo json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

switch ($method) {
    case 'PUT':
        $json = file_get_contents('php://input');
        $obj = json_decode($json, false);
        postRecord($obj);
        break;
    case 'GET':
        listData();
        break;
    case 'DELETE':
        $json = file_get_contents('php://input');
        $obj = json_decode($json, false);
        deleteRecord($obj);
        break;
    case 'UPDATE':
        $json = file_get_contents('php://input');
        $obj = json_decode($json, false);
        updateRecord($obj);
        break;
    default:
        listData();
        break;
}
$conn->close();
