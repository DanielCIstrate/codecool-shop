
export let dataHandler = {
    _data: {},
    _api_get: function (url, callback) {
        // loads data from API, parses it and calls the callback with it

        fetch(url, {
            method: 'GET',
            credentials: 'same-origin'
        })
            .then(response => response.json())  // parse the response as JSON
            .then(json_response => callback(json_response));  // Call the `callback` with the returned object
    },
    _api_post: function (url, data, callback) {

        // sends the data to the API, and calls callback function
        fetch(url, {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
        })
            .then(callback(data));
    },

    addLineItemInOrderJSON(productId, quantity) {
        if (this._data.hasOwnProperty("order")){
            let foundLine = [... this._data["order"]]
                .filter(product => product.hasOwnProperty("productId") && product["productId"] === productId);
            if (foundLine.length > 0) {
                if (foundLine[0]["quantity"]) {
                    foundLine[0]["quantity"] += quantity;
                }
                else {
                    foundLine[0]["quantity"] = quantity;
                }
            }
            else {
                this._data["order"].push({"productId": productId, "quantity": quantity});
            }

        }
        else {
            this.createNewOrderOnServer();
            this._data["order"] = [];
            this._data["order"].push({"productId": productId, "quantity": quantity});
        }
        this.setSessionItem("data", this._data)
    },

    getTotalQuantityForOrder() {
        if (this._data.hasOwnProperty("order")){
            let total = [... this._data["order"]]
                .reduce((accumulator, element) => accumulator + element["quantity"], 0);
            return total;
        }
        else { return 0; }
    },

    createNewOrderOnServer() {
        let userId = this.getSessionItem('userId');
        this._api_post(
            "/create-order",
            {"userId": userId, "name": Date.now().toString()},
            () => {});
    },

    setSessionItem(name, value) {
        var mySession;
        try {
            mySession = JSON.parse(localStorage.getItem('codecool-shop'));
        } catch (e) {
            console.log(e);
            mySession = {};
        }

        mySession[name] = value;

        mySession = JSON.stringify(mySession);

        localStorage.setItem('codecool-shop', mySession);
    },

    getSessionItem(name) {
        var mySession = localStorage.getItem('codecool-shop');
        if (mySession) {
            try {
                mySession = JSON.parse(mySession);
                return mySession[name];
            } catch (e) {
                console.log(e);
            }
        }
    },

    restoreSession(session) {
        if (session) {
            if (session.hasOwnProperty("data")) {
                this._data = session["data"]
            }
        }
    },






}