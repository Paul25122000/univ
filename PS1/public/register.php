<?php

session_start();

if (isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true) {
    header("location: index.php");
    exit;
}

require_once "modules/config.php";

$username = $password = $confirm_password = "";
$username_err = $password_err = $confirm_password_err = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    if (empty(trim($_POST["username"]))) {
        $username_err = ", insert username";
    } else {
        $sql = "SELECT id FROM GH_users WHERE username = ?";

        if ($stmt = mysqli_prepare($conn, $sql)) {
            mysqli_stmt_bind_param($stmt, "s", $param_username);

            $param_username = trim($_POST["username"]);

            if (mysqli_stmt_execute($stmt)) {
                mysqli_stmt_store_result($stmt);

                if (mysqli_stmt_num_rows($stmt) == 1) {
                    $username_err = ", this username is taken";
                } else {
                    $username = trim($_POST["username"]);
                }
            } else {
                echo "Something went wrong. Please try again later.";
            }

            mysqli_stmt_close($stmt);
        }
    }

    if (empty(trim($_POST["password"]))) {
        $password_err = ", insert password.";
    } elseif (strlen(trim($_POST["password"])) < 8) {
        $password_err = ", password must contain at least 8 characters";
    } else {
        $password = trim($_POST["password"]);
    }

    if (empty(trim($_POST["confirm_password"]))) {
        $confirm_password_err = ", confirm password";
    } else {
        $confirm_password = trim($_POST["confirm_password"]);
        if (empty($password_err) && ($password != $confirm_password)) {
            $confirm_password_err = ", passwords does not match";
        }
    }

    if (empty($username_err) && empty($password_err) && empty($confirm_password_err)) {

        $sql = "INSERT INTO GH_users (username, password) VALUES (?, ?)";

        if ($stmt = mysqli_prepare($conn, $sql)) {
            mysqli_stmt_bind_param($stmt, "ss", $param_username, $param_password);

            $param_username = $username;
            $param_password = password_hash($password, PASSWORD_DEFAULT);

            if (mysqli_stmt_execute($stmt)) {
                header("location: login.php");
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
    <title>Register</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="materialize/css/materialize.min.css">
    <link rel="shortcut icon" href="favicon.png" type="image/x-icon">
    <link rel="stylesheet" href="css/admin.css">
</head>

<body>
    <div class="d_fl a_c j_c authentification">
        <div class="wrapper card">
            <h4 class="center">Register</h4>
            <div class="row">
                <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                    <div class="input-field col s12">
                        <input autocomplete type="text" minlength="8" id="username" name="username" class="validate <?php echo (!empty($username_err)) ? 'invalid' : ''; ?>" value="<?php echo $username; ?>">
                        <i class="material-icons">person</i>
                        <label for="username">Username</label>
                        <span class="helper-text" data-error="Invalid<?php echo $username_err; ?>" data-success="Valid">
                        </span>
                    </div>
                    <div class="input-field col s12">
                        <input autocomplete type="password" id="password" minlength="8" name="password" class="validate <?php echo (!empty($password_err)) ? 'invalid' : ''; ?>" value="<?php echo $password; ?>">
                        <i class="material-icons">vpn_key</i>
                        <label for="password">Password</label>
                        <span class="helper-text" data-error="Invalid<?php echo $password_err; ?>" data-success="Valid"></span>
                    </div>
                    <div class="input-field col s12">
                        <input type="password" minlength="8" id="confirm_password" name="confirm_password" class="validate <?php echo (!empty($confirm_password_err)) ? 'invalid' : ''; ?>">
                        <i class="material-icons">spellcheck</i>
                        <label for="confirm_password">Confirm password</label>
                        <span class="helper-text" data-error="Invalid<?php echo $confirm_password_err; ?>" data-success="Valid"></span>
                    </div>
                    <div class="d_fl a_c j_sb col s12">
                        <div class="input-field col s6">
                            <a href="login.php">Already have an account?</a>
                        </div>
                        <div class="input-field col s6">
                            <input class="btn darken indigo " type="submit" value="Submit">
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </div>
    <script src="materialize/js/materialize.min.js"></script>
</body>

</html>