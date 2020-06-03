<?php
require_once "../modules/config.php";

$username = $password = "";
$username_err = $password_err = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    $credentials = json_decode(file_get_contents('php://input'), false);

    if (empty(trim($credentials->username))) {
        echo json_encode("insert username");
    } else {
        $username = trim($credentials->username);
    }

    if (empty(trim($credentials->password))) {
        echo json_encode("insert password");
    } else {
        $password = trim($credentials->password);
    }

    if (empty($username_err) && empty($password_err)) {
        $sql = "SELECT id, username, password FROM GH_users WHERE username = ?";

        if ($stmt = mysqli_prepare($conn, $sql)) {
            mysqli_stmt_bind_param($stmt, "s", $param_username);

            $param_username = $username;

            if (mysqli_stmt_execute($stmt)) {
                mysqli_stmt_store_result($stmt);

                if (mysqli_stmt_num_rows($stmt) == 1) {
                    mysqli_stmt_bind_result($stmt, $id, $username, $hashed_password);
                    if (mysqli_stmt_fetch($stmt)) {
                        if (password_verify($password, $hashed_password)) {
                            if (!isset($_COOKIE["PHPSESSID"])) {
                                session_start();
                            }

                            $_SESSION["loggedin"] = true;
                            $_SESSION["id"] = $id;
                            $_SESSION["username"] = $username;
                            echo json_encode("session started");
                        } else {
                            echo json_encode("incorrect password");
                        }
                    }
                } else {
                    echo json_encode("account with this username does not exist");
                }
            } else {
                echo json_encode("Something went wrong. Please try again later.");
            }
            mysqli_stmt_close($stmt);
        }
    }
    mysqli_close($conn);
} else {
    header("HTTP/1.0 403 Forbidden");
}
