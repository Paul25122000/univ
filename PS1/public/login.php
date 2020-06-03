<?php
session_start();

if (isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true) {
    header("location: index.php");
    exit;
}

require_once "modules/config.php";

$username = $password = "";
$username_err = $password_err = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    if (empty(trim($_POST["username"]))) {
        $username_err = ", insert username";
    } else {
        $username = trim($_POST["username"]);
    }

    if (empty(trim($_POST["password"]))) {
        $password_err = ", insert password";
    } else {
        $password = trim($_POST["password"]);
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
                            session_start();

                            $_SESSION["loggedin"] = true;
                            $_SESSION["id"] = $id;
                            $_SESSION["username"] = $username;

                            header("location: index.php");
                        } else {
                            $password_err = ", incorrect password.";
                        }
                    }
                } else {
                    $username_err = ", account with this username does not exist";
                }
            } else {
                echo "Something went wrong. Please try again later.";
            }

            mysqli_stmt_close($stmt);
        }
    }

    mysqli_close($conn);
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="materialize/css/materialize.min.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="shortcut icon" href="favicon.png" type="image/x-icon">
</head>

<body>
    <div class="d_fl a_c j_c authentification">
        <div class="wrapper card">
            <h4 class="center">Login</h4>
            <div class="row">
                <form class="col s12" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                    <div class="input-field col s12">
                        <input autocomplete type="text" minlength="8" id="username" name="username" class="validate <?php echo (!empty($username_err)) ? 'invalid' : ''; ?>" value="<?php echo $username; ?>">
                        <i class="material-icons">person</i>
                        <label for="username">Username</label>
                        <span class="helper-text" data-error="Invalid<?php echo $username_err; ?>" data-success="Valid">
                        </span>
                    </div>
                    <div class="input-field col s12">
                        <input autocomplete type="password" minlength="8" id="password" name="password" class="validate <?php echo (!empty($password_err)) ? 'invalid' : ''; ?>">
                        <i class="material-icons">vpn_key</i>
                        <label for="password">Password</label>
                        <span class="helper-text" data-error="Invalid<?php echo $password_err; ?>" data-success="Valid">
                        </span>
                    </div>
                    <div class="d_fl a_c j_sb col s12">
                        <a href="register.php">Register</a>
                        <a href="reset_password.php">Forgot</a>
                        <input type="submit" class="btn indigo darken margin_zero" value="Login">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="materialize/js/materialize.min.js"></script>
</body>

</html>