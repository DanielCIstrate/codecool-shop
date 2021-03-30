import {dataHandler} from "/static/js/data_handler.js";

window.addEventListener('load', function(event) {
    dataHandler.init();
}, false);


window.onload = function() {
    let addToCartButtons = document.querySelectorAll(".addToCartButton");
    let emptyCartConfirmButton = document.querySelector("#emptyCartConfirm");
    let cartCounter = document.querySelector(".cart-basket")
    let cartIcon = document.querySelector("#cartIconAnchor")
    let productImages = document.querySelectorAll(".itemImage")
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

    async function handleCartIconClick(event) {
        let data = dataHandler.getSessionItem("data");
        if (data.hasOwnProperty("orderId")) {
            await dataHandler.updateOrderOnServer();
            cartIcon.href = `/view-cart?orderId=${data["orderId"]}`
        }
    }


    function imageHandleOnMouseOver(event) {
        let productId = parseInt(event.target.dataset.productId);
        console.log("Product Id is " + productId);
        event.target.src='/static/img/product_change_' + productId + '.jpg';
    }

    function imageHandleOnMouseOut(event) {
        let productId = parseInt(event.target.dataset.productId);
        console.log("Product Id is " + productId);
        event.target.src='/static/img/product_' + productId + '.jpg';
    }


    productImages.forEach(image => {
        image.addEventListener(
            'mouseenter',
            event => imageHandleOnMouseOver(event)
        )
    });

    productImages.forEach(image =>{
        image.addEventListener(
            'mouseleave',
            event => imageHandleOnMouseOut(event)
        )
    });


    addToCartButtons.forEach(button => {
        button.addEventListener(
            'click',
            (event) => handleAddToCartClick(event, event.target)
        )
    });

    emptyCartConfirmButton.addEventListener(
        'click',
        (event) => handleEmptyCartClick(event));

    cartIcon.addEventListener(
        'click',
        (event) => handleCartIconClick(event)
    );





}
