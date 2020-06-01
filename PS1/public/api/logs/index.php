<?php
include '../../modules/config.php';
$method = $_SERVER['REQUEST_METHOD'];

$limit = isset($_GET['limit']) ? $_GET['limit'] : 10;

function listData()
{
    global $conn, $limit;
    try {
        $stmt = $conn->prepare("SELECT * FROM `GH_logs` ORDER BY `timestamp` DESC LIMIT $limit");
        $stmt->execute();
        $stmt->bind_result($id, $timestamp, $light_value, $light_set, $temperature_value, $temperature_set, $water_value, $water_set);
        $json_array = [];
        while ($stmt->fetch()) {
            $data = [
            	'id' => $id,
            	'timestamp' => $timestamp,
            	'lightValue' => $light_value,
            	'lightSet' => $light_set,
            	'temperatureValue' => $temperature_value,
            	'temperatureSet' => $temperature_set,
            	'waterValue' => $water_value,
            	'waterSet' => $water_set
            ];
            array_push($json_array, $data);
        }
        echo json_encode($json_array);
    } catch (mysqli_sql_exception $e) {
        echo json_encode(["MySQLi Error Code: " => $e->getCode(), "Exception Msg: " => $e->getMessage()]);
        exit();
    }
};

function postRecord($record)
{
    global $conn;
    $date = new DateTime("now", new DateTimeZone('Europe/Bucharest') );
    $timestamp = $date->format('Y-m-d H:i:s');

    $sql = "INSERT INTO `GH_logs` (`timestamp`, `light_value`, `light_set`, `temperature_value`, `temperature_set`, `water_value`, `water_set`)
          VALUES ('$timestamp',
          '$record->lightValue',
          '$record->lightSet',
          '$record->temperatureValue',
          '$record->temperatureSet',
          '$record->waterValue',
          '$record->waterSet')";
    if ($conn->query($sql) === TRUE) {
        echo json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

function deleteRecord($record)
{
    global $conn;
    $deleteAll = isset($_GET["deleteAll"]);
    if($deleteAll)
        $sql = "DELETE FROM `GH_logs` WHERE 1";
    else
        $sql = "DELETE FROM `GH_logs` WHERE id=$record->id";
    if ($conn->query($sql) === TRUE) {
        echo $deleteAll ? json_encode("Table has been successfully truncated") : json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

function updateRecord($record)
{
    global $conn, $table, $name, $count;
    $sql = "UPDATE `$table` SET `$count`='$record->count',  `$name`='$record->name' WHERE id = $record->id";
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
