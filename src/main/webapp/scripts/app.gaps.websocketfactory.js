/**
 * Websocket factory
 */
gaps.factory('Socket', ['$q', '$rootScope', function ($q, $rootScope) {
        // We return this object to anything injecting our service
        var Service = {};
        // Keep all pending requests here until they get responses
        var callbacks = {};
        // Create a unique callback ID to map requests to responses
        var currentCallbackId = 0;
        // Create our websocket object with the address to the websocket
        var ws = new WebSocket(wsURL);

        ws.onopen = function (event) {
            console.log("Socket opened!");
        };
        ws.onmessage = function (message) {
            listener(JSON.parse(message.data));
        };
        ws.onclose = function (event) {
            console.log("Connection closed");
        };
        Service.getGraph = function (graphSettings) {
            var request = {
                type: "GetGraph",
                data: graphSettings
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.computePaths = function (geneticSettings) {
            var request = {
                type: "ComputePaths",
                data: geneticSettings
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.evolve = function (geneticSettings) {
            var request = {
                type: "Evolve",
                data: geneticSettings
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.compare = function (geneticSettings) {
            var request = {
                type: "Compare",
                data: geneticSettings
            };
            var promise = sendRequest(request);
            return promise;
        };
        function sendRequest(request) {
            var defer = $q.defer();
            var callbackId = getCallbackId();
            callbacks[callbackId] = {
                time: new Date(),
                cb: defer
            };
            request.callback_id = callbackId;

            if (ws.readyState == 0 || ws.readyState == 2 || ws.readyState == 3) {
                var interval = window.setInterval(function () {
                    var response = {};
                    response.callback_id = callbackId;
                    response.status = 410;
                    response.isEnded = true;
                    response.description = 'Connection was closed in the meantime, please try and refresh the page';
                    listener(response);

                    window.clearInterval(interval);
                }, 1000);
            } else {
                console.log('Sending request', request);
                ws.send(JSON.stringify(request));
            }

            return defer.promise;
        }

        function listener(data) {
            var messageObj = data;
            // If an object exists with callback_id in our callbacks object, resolve it
            if (callbacks.hasOwnProperty(messageObj.callback_id)) {
                $rootScope.$apply(callbacks[messageObj.callback_id].cb.notify(messageObj));
                if (messageObj.callback_id && messageObj.isEnded && messageObj.isEnded === true) {
                    $rootScope.$apply(callbacks[messageObj.callback_id].cb.resolve(messageObj));
                    console.log("Received response: ", messageObj);
                    console.log("Request with id " + messageObj.callback_id + " completed");
                    delete callbacks[messageObj.callbackID];
                } else {
                    console.log("Received update: ", messageObj);
                }
                //If not, we have a standalone message, log it
            } else {
                console.log("Received message: ", messageObj);
            }
        }

        // This creates a new callback ID for a request
        function getCallbackId() {
            currentCallbackId += 1;
            return currentCallbackId;
        }

        return Service;
    }]);

