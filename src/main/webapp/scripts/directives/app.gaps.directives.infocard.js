

gaps.directive("infoCard", function () {
    return {
        restrict: "AE",
        scope: {
            infoData: '=',
            infoTitle: '@'
        },
        templateUrl: 'scripts/templates/infoCardTemplate.jsp',
        link: function (scope, element, attrs) {

            scope.$watch(
                    watchObject, handleUpdate, true);

            function watchObject() {
                return scope.infoData;
            }
            
            function handleUpdate(infoDataValueOld, infoDataValueNew, $scope) {}

        }
    };
});

