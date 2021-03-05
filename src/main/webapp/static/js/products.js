import {dataHandler} from "/static/js/data_handler.js";

window.addEventListener('load', function(event) {
    dataHandler.init();
}, false);


window.onload = function() {
    let addToCartButtons = document.querySelectorAll(".addToCartButton");
    let emptyCartConfirmButton = document.querySelector("#emptyCartConfirm");
    let cartCounter = document.querySelector(".cart-basket")
    cartCounter.innerText = dataHandler.getTotalQuantityForOrder();

    function handleAddToCartClick(event, targetButton) {
        let productIdForButton = parseInt(targetButton.dataset.productId);
        let productNameForButton = targetButton.dataset.productName
        alert(`${productNameForButton} added to cart`);
        dataHandler.addLineItemInOrderJSON(productIdForButton, 1);
        cartCounter.innerText = dataHandler.getTotalQuantityForOrder();
    }

    function handleEmptyCartClick(event) {
        dataHandler.clearOrder();
        cartCounter.innerText = 0;
    }

    addToCartButtons.forEach(button => {
        button.addEventListener(
            'click',
            (event) => handleAddToCartClick(event, event.target)
        )
    });

    emptyCartConfirmButton.addEventListener(
        'click',
        (event) => handleEmptyCartClick(event))





}
