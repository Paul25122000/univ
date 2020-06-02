<?php

include '../../modules/config.php';
$method = $_SERVER['REQUEST_METHOD'];

function listData()
{
    global $conn;
    try {
        $stmt = $conn->prepare("SELECT * FROM GH_preferences");
        $stmt->execute();
        $stmt->bind_result($id, $name, $selected, $light, $temperature, $water);
        $json_array = [];
        while ($stmt->fetch()) {
            $data = [
                'id' => $id,
                'name' => $name,
                'selected' => $selected,
                'light' => $light,
                'temperature' => $temperature,
                'water' => $water
            ];
            array_push($json_array, $data);
        }
        echo json_encode($json_array);
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

    $sql = "INSERT INTO GH_preferences (`name`, `currentId`, `light`, `temperature`, `water`)
          VALUES ('$record->name', '$record->currentId', '$record->light', '$record->temperature', '$record->water')";
    if ($conn->query($sql) === TRUE) {
        echo json_encode($record);
    } else {
        echo json_encode($conn->error);
    }
}

function deleteRecord($record)
{
    global $conn;
    $sql = "DELETE GH_preferences, GH_logs 
            FROM GH_preferences INNER JOIN GH_logs
            WHERE GH_preferences.id=$record->id AND GH_logs.culture_id=$record->id";
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
