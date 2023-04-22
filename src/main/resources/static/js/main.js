function showMapModal(hospitalName, lat, lng) {
  var mapModal = document.getElementById("mapModal");
  var span = document.getElementsByClassName("close")[0];

  // Open the modal
  mapModal.style.display = "block";

  // Initialize the map with the given latitude and longitude
  var map = new google.maps.Map(document.getElementById("map"), {
    zoom: 14,
    center: { lat: lat, lng: lng },
  });

  // Add a marker with the given latitude and longitude
  var marker = new google.maps.Marker({
    position: { lat: lat, lng: lng },
    map: map,
    title: hospitalName,
  });

  // Close the modal when the user clicks on the close button
  span.onclick = function () {
    mapModal.style.display = "none";
  };

  // Close the modal when the user clicks outside of the modal content
  window.onclick = function (event) {
    if (event.target == mapModal) {
      mapModal.style.display = "none";
    }
  };
}