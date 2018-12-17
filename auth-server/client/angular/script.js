// $scope.uuid = function () {
//     $http.get('http://localhost:8080/v1/api/uuid')
//         .success(function(response) {
//             $scope.results = response;
//         })
//         .error(function(error) {
//             console.log("error is : " + JSON.stringify(error))
//         })
// }


var testClient = angular.module('testClient', []);

testClient.controller('uuidController' , function ($scope, $http) {
    $scope.home = "This is the UUID generator";

    $scope.generateUUID = function () {
        console.log("UUID requested!");
        $http.get("http://localhost:8080/v1/api/uuid", { headers: {'Access-Control-Allow-Origin': '*'}})
            .then(function successCallback(response){
                $scope.response = response;
            }, function errorCallback(response){
                console.log("Unable to perform get request");
            });
    };

});