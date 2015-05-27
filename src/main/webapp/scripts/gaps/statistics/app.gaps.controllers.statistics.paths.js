
gaps.controller('pathsstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GraphStatistics', 'PathStatistics',
    function ($rootScope, $scope, Notification, GraphStatistics, PathStatistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;
        $scope.dataToggleId = '#path-statistics-viewer-toggle';
        $scope.dataTableId = '#graph-paths';
        $scope.statisticsInfoCardValue = [];

        $scope.getStatistics = function () {
            return PathStatistics.getStatistics();
        };

        $scope.getGraphStatistics = function () {
            return GraphStatistics;
        };

        $rootScope.$on('resetViews', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('resetPathGeneticViews', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('graphDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('pathDataUpdated', function (event, $data) {
            if ($data && $data.path) {
                PathStatistics.add($data.path);
            }

            $scope.updateView($data);
        });

        $rootScope.$on('pathDataLoaded', function (event, $data) {
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;
            $scope.initView();
        });

        $scope.resetData = function () {
            PathStatistics.reset();
        };

        $scope.resetView = function () {
            var table = $($scope.dataTableId).DataTable();
            table.clear();

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function ($data) {
            var interval = window.setInterval(function () {
                $scope.statisticsInfoCardValue = $scope.buildInfoCardValue(PathStatistics.getStatistics());
                
                var $pathStatisticsToggle = $($scope.dataToggleId).bootstrapToggle({
                    on: 'Visible',
                    off: 'Hidden'
                });
                $pathStatisticsToggle.change(function () {
                    $scope.hideView();
                });
                window.clearInterval(interval);
            }, 1000);
            Notification.success({message: 'Paths Computed', delay: 2000});
        };

        $scope.updateView = function ($data) {
            var pathStatistics = PathStatistics.getStatistics();

            if ($data && $data.path) {
                var table = $($scope.dataTableId).DataTable();
                table.row.add([pathStatistics.counter, $data.path.length, PathStatistics.getPathCost($data.path)]);

                table.draw();
            }
        };
        
        $scope.buildInfoCardValue = function($data) {
            var value = [];
            value.push({title: "Number of paths", value: $data.paths.length});
            value.push({title: "Total path cost", value: $data.totalPathCost});
            value.push({title: "Cheapest path cost", value: $data.cheapestPathCost});
            value.push({title: "Cheapest path length", value: $data.cheapestPath.length});
            value.push({title: "Most expensive path cost", value: $data.mostExpensivePathCost});
            value.push({title: "Most expensive path length", value: $data.mostExpensivePath.length});
            value.push({title: "Average path cost", value: $data.averagePathCost});
            
           return value;
        };

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
        };

    }]);