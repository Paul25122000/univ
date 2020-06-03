<?php
session_start();

if (!isset($_SESSION["loggedin"]) || $_SESSION["loggedin"] !== true) {
    header("location: login.php");
    exit;
}

require_once "modules/config.php";

$new_password = $confirm_password = "";
$new_password_err = $confirm_password_err = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {

    if (empty(trim($_POST["new_password"]))) {
        $new_password_err = ", insert new password";
    } elseif (strlen(trim($_POST["new_password"])) < 8) {
        $new_password_err = ", password must contain at least 8 characters";
    } else {
        $new_password = trim($_POST["new_password"]);
    }

    if (empty(trim($_POST["confirm_password"]))) {
        $confirm_password_err = ", confirm password";
    } else {
        $confirm_password = trim($_POST["confirm_password"]);
        if (empty($new_password_err) && ($new_password != $confirm_password)) {
            $confirm_password_err = ", passwords does not match";
        }
    }

    if (empty($new_password_err) && empty($confirm_password_err)) {
        $sql = "UPDATE GH_users SET password = ? WHERE id = ?";

        if ($stmt = mysqli_prepare($conn, $sql)) {
            mysqli_stmt_bind_param($stmt, "si", $param_password, $param_id);

            $param_password = password_hash($new_password, PASSWORD_DEFAULT);
            $param_id = $_SESSION["id"];

            if (mysqli_stmt_execute($stmt)) {
                session_destroy();
                header("location: login.php");
                exit();
            } else {
                echo "Oops! Something went wrong. Please try again later.";
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
    <title>Resetează parola</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="materialize/css/materialize.min.css">
    <link rel="shortcut icon" href="favicon.png" type="image/x-icon">
    <link rel="stylesheet" href="css/admin.css">
</head>

<body>
    <div class="d_fl a_c j_c authentification">
        <div class="wrapper card">
            <h4 class="center">Resetează parola</h4>
            <div class="row">
                <form class="col s12" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                    <div class="input-field col s12">
                        <input type="password" id="password" minlength="8" name="new_password" class="validate <?php echo (!empty($new_password_err)) ? 'invalid' : ''; ?>" value="<?php echo $new_password; ?>">
                        <i class="material-icons">vpn_key</i>
                        <label for="password">New password</label>
                        <span class="helper-text" data-error="Invalid<?php echo $new_password_err; ?>" data-success="Valid"></span>
                    </div>
                    <div class="input-field col s12">
                        <input type="password" minlength="8" id="confirm_password" name="confirm_password" class="validate <?php echo (!empty($confirm_password_err)) ? 'invalid' : ''; ?>">
                        <i class="material-icons">spellcheck</i>
                        <label for="confirm_password">Confirm password</label>
                        <span class="helper-text" data-error="Invalid<?php echo $confirm_password_err; ?>" data-success="Valid"></span>
                    </div>
                    <div class="d_fl a_c j_sb col s12">
                        <a href="index.php">Cancel</a>
                        <input type="submit" class="btn indigo darken" value="Change">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="materialize/js/materialize.min.js"></script>
</body>

</html>