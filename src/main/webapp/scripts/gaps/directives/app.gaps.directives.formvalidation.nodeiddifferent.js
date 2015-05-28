

gaps.directive("nodeIdDifferent", function () {
    return {
        restrict: "A",
        require: "ngModel",
        link: function ($scope, $element, $attrs, ngModel) {
            $scope.$watch($attrs.ngModel, function (value) {
                var isValid = true;

                if ($scope.geneticSettings && $scope.geneticSettings.sourceNode && $scope.geneticSettings.destinationNode) {
                    if($scope.geneticSettings.sourceNode == $scope.geneticSettings.destinationNode) {
                        isValid = false;
                    }
                }
                
                ngModel.$setValidity('nodeiddifferent', isValid);
            });
        }
    };
});

