import {dataHandler} from "/static/js/data_handler.js";

window.onload = function() {
    let addToCartButtons = document.querySelectorAll(".addToCartButton");
    let cartCounter = document.querySelector(".cart-basket")

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
