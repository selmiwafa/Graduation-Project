//donation
function showDonations() {
  $.ajax({
    url: "./adminView/viewDonations.php",
    method: "post",
    data: { record: 1 },
    success: function (data) {
      $(".allContent-section").html(data);
    },
  });
}
function refuseDonation(id) {
  $.ajax({
    url: "./controller/donDeleteController.php",
    method: "post",
    data: { record: id },
    success: function (data) {
      alert("Donation Successfully deleted");
      $("form").trigger("reset");
      showDonations();
    },
  });
}

//request
function refuseRequest(id) {
  $.ajax({
    url: "./controller/donDeleteController.php",
    method: "post",
    data: { record: id },
    success: function (data) {
      alert("Donation Successfully deleted");
      $("form").trigger("reset");
      showRequests();
    },
  });
}
function acceptRequest(id) {
  $.ajax({
    url: "./controller/donDeleteController.php",
    method: "post",
    data: { record: id },
    success: function (data) {
      alert("Donation Successfully deleted");
      $("form").trigger("reset");
      showRequests();
    },
  });
}
function showRequests() {
  $.ajax({
    url: "./adminView/viewRequests.php",
    method: "post",
    data: { record: 1 },
    success: function (data) {
      $(".allContent-section").html(data);
    },
  });
}
