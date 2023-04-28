<div >
  <h2>Requests</h2>
  <table class="table ">
    <thead>
      <tr>
        <th class="text-center">S.N.</th>
        <th class="text-center">Date</th>
        <th class="text-center">Barcode</th>
        <th class="text-center">Quantity</th>
        <th class="text-center">Requestor</th>
        <th class="text-center">Adress</th>
        <th class="text-center">Phone</th>
        <th class="text-center" colspan="2">Action</th>
      </tr>
    </thead>
    <?php
      include_once "../config/dbconnect.php";
      $sql="SELECT donations_requests.*, user.* 
            FROM user
            JOIN donations_requests 
            ON user.email = donations_requests.user
            WHERE donations_requests.isAccepted = 0;";
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
      <td><button type="button" class="btn btn-primary" style="height:40px" data-toggle="modal" data-target="#myModal" >Accept</button></td> 
      <td><button class="btn btn-danger" style="height:40px" onclick="refuseRequest('<?=$row['id']?>')">Reject</button></td>
    </tr>
    <?php
            $count=$count+1;
          }
        }
      ?>
  </table>