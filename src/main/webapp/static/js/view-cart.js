import {dataHandler} from "/static/js/data_handler.js";

window.addEventListener(
    'load',
    function(event) {
         dataHandler.init();
    },
    false
);

let data = dataHandler.getSessionItem("data");

let checkOutButton = document.querySelector(".checkoutButton");

function handleCheckOutButtonClick(event) {
    dataHandler.checkOutOrder();
}

checkOutButton.addEventListener('click', (event) => handleCheckOutButtonClick(event))