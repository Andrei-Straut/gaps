

gaps.controller('comparestatisticscontroller', ['$rootScope', '$scope', 'Notification', 'Statistics',
    function ($rootScope, $scope, Notification, Statistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;

        $scope.compareChartElementId = 'morris-bar-compare-chart';

        $scope.dataToggleId = '#compare-statistics-viewer-toggle';

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

        $rootScope.$on('pathDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('geneticDataLoaded', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('compareDataUpdated', function (event, $data) {
            Statistics.addCompareStatistic($data);
        });

        $rootScope.$on('compareDataLoaded', function (event, $data) {
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;
            $scope.initView();
        });

        $scope.resetData = function () {
            Statistics.resetCompareStatistics();
        };

        $scope.resetView = function () {
            var table = $($scope.dataTableId).DataTable();
            table.clear();

            $scope.compareChart.clear($scope.compareChartElementId);

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function () {
            Statistics.markCompareEnd();

            var interval = window.setInterval(function () {
                var compareStatistics = Statistics.getCompareStatistics();
                Statistics.setCompareStatisticsLoaded(true);
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

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
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
                    ykeys: ['GAPS', 'KShortest'],
                    labels: ['GAPS', 'KShortest'],
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

