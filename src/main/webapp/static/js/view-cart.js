import {dataHandler} from "/static/js/data_handler.js";

window.addEventListener('load', function(event) {
   dataHandler.init();
}, false);

let data = dataHandler.getSessionItem("data");
if (data.hasOwnProperty("orderId")) {
   dataHandler.updateOrderOnServer();
}