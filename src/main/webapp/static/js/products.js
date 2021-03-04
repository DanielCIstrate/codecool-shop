import {dataHandler} from "/static/js/data_handler.js";

window.addEventListener('load', function(e) {
    var mySession = localStorage.getItem('codecool-shop');
    if (mySession) {
        try {
            mySession = JSON.parse(localStorage.getItem('codecool-shop'));
        } catch (e) {
            console.log(e);
            mySession = {};
        }
        dataHandler.restoreSession(mySession);
    } else {
        localStorage.setItem('codecool-shop', '{}');
    }

    dataHandler.setSessionItem('userId', 1); //should change on each window load

    // if (!dataHandler.getSessionItem("stable_key")) {
    //    dataHandler.setSessionItem("stable_key", defaultValue)
    // }
}, false);


window.onload = function() {
    let addToCartButtons = document.querySelectorAll(".addToCartButton");
    let cartCounter = document.querySelector(".cart-basket")
    cartCounter.innerText = dataHandler.getTotalQuantityForOrder();

    function handleAddToCartClick(event, targetButton) {
        let productIdForButton = parseInt(targetButton.dataset.productId);
        alert(`You've clicked the button for product id ${productIdForButton}`);
        dataHandler.addLineItemInOrderJSON(productIdForButton, 1);
        cartCounter.innerText = dataHandler.getTotalQuantityForOrder();
    }

    addToCartButtons.forEach(button => {
        button.addEventListener(
            'click',
            (event) => handleAddToCartClick(event, event.target)
        )
    });


}
