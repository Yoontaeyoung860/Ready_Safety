function initMap() {
  const map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: 36.8, lng: 127.7 }, // 기본 위치를 한국 중심부로 설정
    zoom: 7, // 기본 확대 레벨 설정
  });
}

function filterLocations() {
  const province = document.getElementById("province").value;
  const city = document.getElementById("city").value;

  const locationTexts = document.getElementsByClassName("location-text");
  for (let i = 0; i < locationTexts.length; i++) {
    const locationText = locationTexts[i];
    const locationProvince = locationText.getAttribute("data-province");
    const locationCity = locationText.getAttribute("data-city");

    if ((!province || province === locationProvince) && (!city || city === locationCity)) {
      locationText.style.display = "block";
      locationText.onclick = function () {
        const lat = parseFloat(locationText.getAttribute("data-lat"));
        const lng = parseFloat(locationText.getAttribute("data-lng"));

        const map = new google.maps.Map(document.getElementById("map"), {
          center: { lat: lat, lng: lng },
          zoom: 15,
        });
        const marker = new google.maps.Marker({
          position: { lat: lat, lng: lng },
          map: map,
        });
      };
    } else {
      locationText.style.display = "none";
      locationText.onclick = null;
    }
  }
}

window.onload = initMap;
