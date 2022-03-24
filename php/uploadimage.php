<?php
$servername = "localhost"; 
$username = "id18641398_university1";
$password = "TigIUWlNo0OB>SD";
$dbname = "id18641398_university";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$nom = $_POST['nom'];
$prenom = $_POST['prenom'];
$ville = $_POST['ville'];
$sexe = $_POST['sexe'];

$target_dir = "images/";
$image = $_POST['upload'];
$imageStore = rand()."_".time().".jpeg";
$target_dir = $target_dir . "/" .$imageStore;
file_put_contents($target_dir, base64_decode($image));

$result = array();
$select = "INSERT INTO `Etudiant` (`id`, `nom`, `prenom`, `ville`, `sexe`, `upload`)
VALUES (NULL, '$nom', '$prenom', '$ville', '$sexe', '$imageStore')";
$responce = mysqli_query($conn, $select);


if ($responce) {
    echo "Nouvel enregistrement créé avec succès";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
