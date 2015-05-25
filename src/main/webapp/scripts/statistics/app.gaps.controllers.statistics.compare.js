

gaps.controller('comparestatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GeneticStatistics', 'CompareStatistics',
    function ($rootScope, $scope, Notification, GeneticStatistics, CompareStatistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;
        $scope.statisticsInfoCardValue = [];

        $scope.compareChartElementId = 'morris-bar-compare-chart';

        $scope.dataToggleId = '#compare-statistics-viewer-toggle';

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

            $scope.compareChart.clear($scope.compareChartElementId);

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function () {
            CompareStatistics.markCompareEnd();

            var interval = window.setInterval(function () {
                var compareStatistics = CompareStatistics.getStatistics();
                $scope.statisticsInfoCardValue = $scope.buildInfoCardValue(compareStatistics);
                $scope.$apply();
                $scope.compareChart.initialize($scope.compareChartElementId, compareStatistics.compareChart);
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
        
        $scope.getCompareStartTime = function() {
            return CompareStatistics.getCompareStartTime();
        };
        
        $scope.getCompareEndTime = function() {
            return CompareStatistics.getCompareEndTime();
        };
        
        $scope.getCompareDiffTime = function() {
            return CompareStatistics.getCompareDiffTime();
        };
        
        $scope.getWinner = function(jGraphTBestCost, gapsBestCost) {
            if(jGraphTBestCost < gapsBestCost) {
                return 'JGraphT';
            }
            
            return 'GAPS';
        };

        $scope.compareChart = {
            chart: function (element, data) {
                jQuery('#main-menu').metisMenu();
                jQuery(window).bind("load resize", function () {
                    if (jQuery(this).width() < 768) {
                        jQuery('div.sidebar-collapse').addClass('collapse');
                    } else {
                        jQuery('div.sidebar-collapse').removeClass('collapse');
                    }
                });
                Morris.Bar({
                    element: element,
                    data: data,
                    xkey: 'y',
                    ykeys: ['GAPS', 'JGraphT'],
                    labels: ['GAPS', 'JGraphT'],
                    hideHover: 'auto',
                    resize: true
                });
            },
            initialize: function (element, data) {
                $scope.compareChart.chart(element, data);
            },
            clear: function (elementId) {
                var chartWrapper = $('#' + elementId);

                if (chartWrapper && chartWrapper[0]) {
                    chartWrapper = chartWrapper[0];
                }

                if (chartWrapper && chartWrapper.firstChild) {
                    while (chartWrapper.firstChild) {
                        chartWrapper.removeChild(chartWrapper.firstChild);
                    }
                }
                $(chartWrapper).css('height', 'auto');
            }
        };
    }]);

