

gaps.controller('comparestatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GeneticStatistics', 'CompareStatistics',
    function ($rootScope, $scope, Notification, GeneticStatistics, CompareStatistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;
        $scope.statisticsInfoCardValue = [];

        $scope.dataToggleId = '#compare-statistics-viewer-toggle';
        $scope.compareChartElementId = 'compare-timeline';
        $scope.compareChart = null;
        $scope.compareChartOptions = {
            height: '400px',
            editable: false,
            clickToUse: true,
            showCurrentTime: false,
            showCustomTime: false,
            showMajorLabels: false,
            style: 'bar',
            stack: false,
            legend: true,
            barChart: {
                sideBySide: true
            },
            drawPoints: true,
            min: -1,
            max: 2,
            start: -1,
            end: 2,
            zoomMin: 2,
            zoomMax: 2
        };

        $scope.getStatistics = function () {
            return CompareStatistics.getStatistics();
        };

        $scope.getGeneticStatistics = function () {
            return GeneticStatistics.getStatistics();
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

        $rootScope.$on('pathDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('geneticDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('compareDataUpdated', function (event, $data) {
            CompareStatistics.add($data);
        });

        $rootScope.$on('compareDataLoaded', function (event, $data) {
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;
            $scope.initView();
        });

        $scope.resetData = function () {
            CompareStatistics.reset();
        };

        $scope.resetView = function () {
            var table = $($scope.dataTableId).DataTable();
            table.clear();

            if ($scope.compareChart && $scope.compareChart !== null && $scope.compareChart !== undefined) {
                $scope.compareChart.destroy();
                $scope.compareChart = null;
            }

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function () {
            CompareStatistics.markCompareEnd();

            var interval = window.setInterval(function () {
                var compareStatistics = CompareStatistics.getStatistics();
                $scope.statisticsInfoCardValue = $scope.buildInfoCardValue(compareStatistics);
                $scope.$apply();

                $scope.compareChartOptions.max = (CompareStatistics.getDataSet().length / 2);
                $scope.compareChartOptions.end = (CompareStatistics.getDataSet().length / 2);
                $scope.compareChartOptions.zoomMin = (CompareStatistics.getDataSet().length / 2);
                $scope.compareChartOptions.zoomMax = (CompareStatistics.getDataSet().length / 2);
                $scope.compareChart = new vis.Graph2d(
                        document.getElementById($scope.compareChartElementId),
                        CompareStatistics.getDataSet().get(),
                        CompareStatistics.getDataSetGroups(),
                        $scope.compareChartOptions);

                window.clearInterval(interval);

                Notification.success({message: 'Done', delay: 2000});
            }, 1000);
            var $compareStatisticsToggle = $($scope.dataToggleId).bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $compareStatisticsToggle.change(function () {
                $scope.hideView();
            });
        };

        $scope.buildInfoCardValue = function ($data) {
            var value = [];
            var winner = $scope.getWinner($data.chromosomes[0].cost, ($scope.getGeneticStatistics()).bestPathCost);

            value.push({title: "Best Path Cost", value: $data.chromosomes[0].cost});
            value.push({title: "Best Path Fitness", value: $data.chromosomes[0].fitness});
            value.push({title: "Winner", value: winner});
            value.push({title: "Search Started", value: $scope.getCompareStartTime()});
            value.push({title: "Search Finished", value: $scope.getCompareEndTime()});
            value.push({title: "Total Time", value: $scope.getCompareDiffTime()});

            return value;
        };

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
        };

        $scope.getCompareStartTime = function () {
            return CompareStatistics.getCompareStartTime();
        };

        $scope.getCompareEndTime = function () {
            return CompareStatistics.getCompareEndTime();
        };

        $scope.getCompareDiffTime = function () {
            return CompareStatistics.getCompareDiffTime();
        };

        $scope.getWinner = function (jGraphTBestCost, gapsBestCost) {
            if (jGraphTBestCost < gapsBestCost) {
                return 'JGraphT';
            }

            return 'GAPS';
        };
    }]);

