function getWeather() {
    $.ajax({
        url: "/weather",
        type: "GET",
        dataType: "json",
        success: function (data) {
            if (data.result.response.header.resultCode === "00") {
                displayWeather(data);
            } else {
                alert("Failed to get weather information.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Failed to get weather information: " + errorThrown);
        }
    });
}

function displayWeather(data) {
    let weather = data.result.response.body.items.item;
    let html = "<ul>";
    for (let i = 0; i < weather.length; i++) {
        html += "<li>" + weather[i].baseDate + " " + weather[i].baseTime + " " + weather[i].category + " " + weather[i].fcstValue + "</li>";
    }
    html += "</ul>";
    $("#weather").html(html);
}