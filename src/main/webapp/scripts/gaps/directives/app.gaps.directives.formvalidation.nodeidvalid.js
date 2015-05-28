

gaps.directive("nodeIdValid", function () {
    return {
        restrict: "A",
        require: "ngModel",
        link: function ($scope, $element, $attrs, ngModel) {
            $scope.$watch($attrs.ngModel, function (value) {
                var isValid = true;

                if ($scope.nodeIds && $scope.nodeIds.length > 0 
                        && value !== undefined && value.trim().replace(' ', '') !== '') {
                    isValid = ($scope.nodeIds.indexOf(value) > -1) ? true : false;
                }
                
                ngModel.$setValidity('nodeidvalid', isValid);
            });
        }
    };
});

