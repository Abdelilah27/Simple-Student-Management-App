<?php

$conn = mysqli_connect("localhost", "id18641398_university1", "TigIUWlNo0OB>SD", "id18641398_university");

$result = array();
$result['data'] = array();
$select="SELECT * FROM Etudiant";
$responce=mysqli_query($conn, $select);

while($row = mysqli_fetch_array($responce)){
    $index['id'] = $row['0'];
    $index['nom'] = $row['1'];
    $index['prenom'] = $row['2'];
    $index['ville'] = $row['3'];
    $index['sexe'] = $row['4'];
    $index['upload'] = $row['5'];
    array_push($result['data'], $index);
    
}

$result["success"]="1";

echo json_encode($result) ;
mysqli_close($$conn);



