import {dataHandler} from "/static/js/data_handler.js";


async function handleUpdateOrderBtnClick(event) {
    await dataHandler.updateOrderOnServer();
    location.reload();
}

window.addEventListener('load', function(event) {

    function increment(triggerButton) {
      let element = triggerButton.parentElement.firstElementChild;
      element.value =
          parseInt(element.value) + 1;
      let productIdForButton = parseInt(element.dataset.productId);
      dataHandler.addLineItemInOrderJSON(productIdForButton, 1);
   }

   function decrement(triggerButton) {
      let element = triggerButton.parentElement.firstElementChild;
      if (parseInt(element.value) >= 1) {
         element.value =
             parseInt(element.value) - 1;
         let productIdForButton = parseInt(element.dataset.productId);
         dataHandler.addLineItemInOrderJSON(productIdForButton, -1);
      }
   }

   dataHandler.init();

   let incrementButtons = document.querySelectorAll(".incrementButton");
   let decrementButtons = document.querySelectorAll(".decrementButton");
   let updateOrderButton = document.querySelector(".updateOrderButton");

   decrementButtons.forEach(button => {
           button.addEventListener(
               'click',
               (event) => decrement(event.target)
           )
       });

   incrementButtons.forEach(button => {
              button.addEventListener(
                  'click',
                  (event) => increment(event.target)
              )
          });

   updateOrderButton.addEventListener(
       'click',
       (event) => handleUpdateOrderBtnClick(event)
   )

}, false);

// let data = dataHandler.getSessionItem("data");





let data = dataHandler.getSessionItem("data");

let checkOutButton = document.querySelector(".checkoutButton");

function handleCheckOutButtonClick(event) {
    dataHandler.checkOutOrder();
}

checkOutButton.addEventListener('click', (event) => handleCheckOutButtonClick(event))

