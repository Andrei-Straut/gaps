
gaps.controller('pathsstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'Statistics',
    function ($rootScope, $scope, Notification, Statistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;
        $scope.dataToggleId = '#path-statistics-viewer-toggle';
        $scope.dataTableId = '#graph-paths';

        $scope.getStatistics = function () {
            return Statistics;
        };

        $rootScope.$on('resetViews', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('graphDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('pathDataUpdated', function (event, $data) {
            if ($data && $data.path) {
                Statistics.addPathStatistic($data.path);
            }

            $scope.updateView($data);
        });

        $rootScope.$on('pathDataLoaded', function (event, $data) {
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;
            $scope.initView();
        });

        $scope.resetData = function () {
            Statistics.resetPathStatistics();
        };

        $scope.resetView = function () {
            var table = $($scope.dataTableId).DataTable();
            table.clear();

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function ($data) {
            var interval = window.setInterval(function () {
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
            var pathStatistics = Statistics.getPathStatistics();

            if ($data && $data.path) {
                var table = $($scope.dataTableId).DataTable();
                table.row.add([pathStatistics.counter, $data.path.length, Statistics.getPathCost($data.path)]);

                table.draw();
            }
        };

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
        };

    }]);