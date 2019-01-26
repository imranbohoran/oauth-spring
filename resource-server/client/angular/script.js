
var testClient = angular.module('testClient', []);

testClient.controller('uuidController' , function ($scope, $http) {
    $scope.home = "This is the UUID generator";

    function extractToken(code) {
        var params = window.location.search.substr(1).split('&');

        for (var i=0; i<params.length; i++) {
            var processingParam = params[i].split('=');

            if (processingParam[0] === code) {
                return processingParam[1];
            }
        }

        return undefined;
    }

    $scope.generateUUID = function () {
        console.log("UUID requested!");

        var token = extractToken('code');
        console.log("Working with token: "+ token);

        var config = {
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Authorization': 'Bearer '+ token
            }
        };

        $http.get("http://localhost:8091/example-resource-server/v1/api/uuid", config)
            .then(function (response) {
                console.log("Successful with data:"+ response.data);
                $scope.uuidValue = response.data;
            })
            .catch(function errorCallback(response) {
                console.log("Unable to perform get request: " + response);
                console.log("Currently at : " + window.location);
                if (response.status === 401) {
                    var returnUrl = window.location.origin + window.location.pathname;
                    location.href = 'http://localhost:8090/example-oauth-server/oauth/authorize?client_id=example_client_id&redirect_uri=http://localhost:8091/example-resource-server/v1/auth/authenticated?client_redirect_uri='+ returnUrl + '&response_type=code&grant_type=authorization_code';
                }
            })
            .finally(function () {
                console.log("Done!!")
            })
        ;
    };

});