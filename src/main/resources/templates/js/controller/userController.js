/**
 * Created by Medion on 2016-10-04.
 */
'use strict';

angular.module('leaveManagement', [])
    .controller('UserController', function($scope, $http) {
        $scope.messageRegister = "";
        $scope.registerDTO = {
            login:'',
            password:'',
            email:'',
            name:'',
            lastname:''
        };
        $scope.confirmPassword="";
        $http.get('/isAuthenticated').
            then(function(response) {
                $scope.greeting = response.data;

                if($scope.greeting.authenticated == false){
                    $("li.login").show();
                    $("li.register").show();
                    $("li.logout").hide();
                    $("li.settings").hide();
                    $("li.changePassword").hide();
                }
                if($scope.greeting.authenticated == true){
                    console.log($scope.greeting.login)
                    console.log("!!! " + $scope.greeting.login);
                    $("li.login").hide();
                    $("li.register").hide();
                    $("li.settings").show();
                    $("li.logout").show();
                    $("li.changePassword").show();
                }
            });
        $scope.register = function() {
            $http.post('/register',$scope.registerDTO)
                .then(function successCallback(response) {
                    $scope.messageRegister = "Account was created.";
                    $("form.css-form").hide();
                }, function errorCallback(error) {
                    $scope.messageRegister = "Error.";
                });


        }

})
.controller('usersListController', function ($scope,$http,$window, $location) {
    $scope.accessLevel=function(data){
        var accesslevel ='';
        for (var level in data) {
            accesslevel += data[level] + " ";
        }
        return accesslevel;
    }

    $scope.getUsersList = function(){
        $http.get('/usersListData')
            .then(function(response) {
                $scope.rowCollection = response.data;
            });
    }

    $scope.changeUserActiveStatus = function(data) {
        var account =[data.login, data.version];
        $http.post('/changeUserActiveStatus',account).then(function(response) {
            $scope.getUsersList();
        });

    }

    $scope.changeUserConfirmStatus = function(data) {
        var account =[data.login, data.version];
        $http.post('/changeUserConfirmStatus',account);
    }

    $scope.changeUserPassword = function(data) {
        $http.post('/changeUserPassword',data.login).success(function(response) {
            console.log(response);
            $window.location.href=response;
        }).error(function(){
            console.log("error changeUserPassword");
        });
    }

    $scope.changeUserData = function(data) {
        $http.post('/editUserAccount',data.login).success(function(response) {
            console.log(response);
            $window.location.href=response;
        }).error(function(){
            console.log("error changeUserData");
        });
    }

    $scope.changeUserRole = function(data) {
        $http.post('/changeUserRole',data.login).success(function(response) {
            $window.location.href=response;
        }).error(function(){
            console.log("error changeUserRole");
        });
    }

    $scope.getUsersList();
})

.controller('changeUserPasswordController', function ($scope,$http,$window) {
    $scope.password="";
    $scope.getUserAccount = function() {
        $http.get('/getUserAccount').success(function (response) {
            console.log("! " + response.login);
            $scope.userAccount = response;
        }).error(function () {
            console.log("error");
        });
    }
    $scope.saveUserPassword = function() {
        var data=[$scope.userAccount.login, $scope.password]
        $http.post('/saveUserPassword', data).success(function(response) {
            $scope.message=response;
        }).error(function(){
            console.log("error");
        });
    }

    $scope.return = function() {
        $window.location.href="/usersList";
    }

    $scope.getUserAccount();

})
    .controller('changeUserDataController', function ($scope,$http,$window) {
        $scope.yearCount=60;
        $scope.monthCount=12;
        $scope.dayCount=30;

        var year = [];
        var month = [];
        var day = [];
        for(var i=0 ;i<60;i++) {
            year[i] = {
                id: i,
                name: i,
                selected: false
            };
            if (i <= 30) {
                day[i] = {
                    id: i,
                    name: i,
                    selected: false
                };
                if (i <= 12) {
                    month[i] = {
                        id: i,
                        name: i,
                        selected: false
                    };
                }
            }
        }
        $scope.setSelectBox= function(){
            console.log($scope.modelExpirience.day[$scope.userAccount.expirienceYear].selected);
            $scope.modelExpirience.day[$scope.userAccount.expirienceYear].selected=true;
            console.log($scope.modelExpirience.day[$scope.userAccount.expirienceYear].selected);
        }
        $scope.modelExpirience={year,month,day}
        $scope.getUserAccount = function() {
            $http.get('/getUserAccount').success(function (response) {
                console.log("! " + response.login);
                $scope.userAccount = response;
                $scope.userAccount.startingDate=new Date($scope.userAccount.startingDate);
                $scope.setSelectBox();

            }).error(function () {
                console.log("error");
            });
        }
        $scope.saveUserAccount = function() {
            var data=[$scope.userAccount.login, $scope.userAccount.name,$scope.userAccount.lastname
                ,$scope.userAccount.email,$scope.userAccount.startingDate, $scope.userAccount.expirienceYear
                , $scope.userAccount.expirienceMonth, $scope.userAccount.expirienceDay]
            $http.post('/saveUserAccount', data).success(function(response) {
                $scope.message=response;
            }).error(function(){
                console.log("error");
            });
        }

        $scope.return = function() {
            $window.location.href="/usersList";
        }

        $scope.getUserAccount();

    })
.directive('compareTo', function () {
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