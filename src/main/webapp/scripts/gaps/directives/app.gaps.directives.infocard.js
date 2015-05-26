

gaps.directive("infoCard", function () {
    return {
        restrict: "AE",
        scope: {
            infoData: '=',
            infoTitle: '@'
        },
        link: function (scope, element, attrs) {

            scope.$watch(watchObject, handleUpdate, true);

            function watchObject() {
                return scope.infoData;
            }
            
            function handleUpdate(infoDataValueOld, infoDataValueNew, $scope) {}

        },
        template: '<div class="col-md-3 col-sm-12 col-xs-12">' + 
                    '<div class="panel panel-primary text-center no-boder bg-color-green">' + 
                        '<div class="panel-footer back-footer-green">' + 
                            '{{infoTitle}}' + 
                        '</div>' + 
                        '<div class="panel-body">' + 
                            '<i class="fa fa-bar-chart-o fa-5x"></i>' + 
                            '<h3 ng-repeat="infoRow in infoData">{{infoRow.title}}: {{infoRow.value}}</h3>' + 
                        '</div>' + 
                    '</div>' + 
                '</div>'
    };
});

