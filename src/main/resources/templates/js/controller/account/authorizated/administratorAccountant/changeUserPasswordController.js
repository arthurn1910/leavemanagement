/**
 * Created by arthurn on 10.11.16.
 */
angular.module('leaveManagement')
    .controller('changeUserPasswordController', function ($scope,$http,$window) {
        $scope.password="";
        $scope.confirmPassword="";

        $scope.getUserAccount = function() {
            $http.get('/getUserAccount').then(function successCallback(response) {
                $scope.userAccount = response.data;
            }, function errorCallback(response) {
                $window.location.href = response.data;
            });

        }
        $scope.saveUserPassword = function() {
            var data=[$scope.userAccount.login, $scope.password]
            $http.post('/saveUserPassword', data).then(function successCallback(response) {
                $scope.message=response.data;
            }, function errorCallback(response) {
                $window.location.href = response.data;
            });
        }

        $scope.return = function() {
            $window.location.href="/usersList";
        }

        $scope.getUserAccount();
}).directive('compareTo', function () {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {

            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };

            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    }
});