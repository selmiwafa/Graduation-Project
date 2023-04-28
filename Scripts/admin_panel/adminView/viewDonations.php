
<div >
  <h3>List of donations</h3>
  <table class="table ">
    <thead>
      <tr>
        <th class="text-center">S.N.</th>
        <th class="text-center">Date</th>
        <th class="text-center">Barcode</th>
        <th class="text-center">Quantity</th>
        <th class="text-center">Donor</th>
        <th class="text-center">Adress</th>
        <th class="text-center">Phone</th>
      </tr>
    </thead>
    <?php
      include_once "../config/dbconnect.php";
      $sql="SELECT donations.*, user.* 
            FROM user
            JOIN donations
            ON user.email = donations.user
            WHERE donations.isAffected = 0;";
      $result=$conn-> query($sql);
      $count=1;
      if ($result-> num_rows > 0){
        while ($row=$result-> fetch_assoc()) {
    ?>
    <tr>
      <td><?=$count?></td>
      <td><?=$row["date"]?></td>   
      <td><?=$row["barcode"]?></td>   
      <td><?=$row["quantity"]?></td>   
      <td><?=$row["user"]?></td>
      <td><?=$row["adress"]?></td>   
      <td><?=$row["number"]?></td>
      </tr>
      <?php
            $count=$count+1;
          }
        }
      ?>
  </table>

  
</div>
   