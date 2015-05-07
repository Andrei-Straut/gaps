

gaps.controller('statisticscontroller', ['$rootScope', '$scope', 'Notification', 'Statistics', function ($rootScope, $scope, Notification, Statistics) {
        
        $scope.graphStatisticsDisplayed = true;
        $scope.pathStatisticsDisplayed = true;
        $scope.geneticStatisticsDisplayed = true;
        
        $scope.getStatistics = function() {
            return Statistics;
        };
        
        $rootScope.$on('graphDataLoaded', function(event, $data) {
            $scope.resetGraphStatistics();
            $scope.initGraphStatistics($data);
        });
        
        $rootScope.$on('pathDataUpdated', function(event, $data) {
            $scope.updatePathStatistics($data);
        });
        
        $rootScope.$on('pathDataLoaded', function(event, $data) {
            $scope.initPathStatistics();
        });
        
        $rootScope.$on('geneticDataUpdated', function(event, $data) {
        });
        
        $rootScope.$on('geneticDataLoaded', function(event, $data) {
            $scope.initGeneticStatistics();
        });
        
        $scope.resetGraphStatistics = function() {
            var table = $('#graph-direct-edges').DataTable();
            table.clear();
            Statistics.setGraphStatisticsLoaded(false);
            
            $scope.resetPathStatistics();
            $scope.resetGeneticStatistics();
        };
        
        $scope.initGraphStatistics = function ($data) {
            var table = $('#graph-direct-edges').DataTable();
            angular.forEach($data.edges, function (edgeValue, edgeKey) {
                table.row.add([edgeValue.data.id, edgeValue.nodeFrom, edgeValue.nodeTo, edgeValue.data.cost]);
            });
            table.draw();
            
            $scope.graphStatisticsLoaded = true;
            Notification.success({message: 'Loaded', delay: 2000});
            var $graphStatisticsToggle = $('#graph-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $graphStatisticsToggle.change(function () {
                $scope.hideGraphStatistics();
            });
            Statistics.setGraphStatisticsLoaded(true);
        };
        
        $scope.hideGraphStatistics = function () {
            $scope.graphStatisticsDisplayed = $('#graph-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        
        $scope.resetPathStatistics = function () {
            var table = $('#graph-paths').DataTable();
            table.clear();
            Statistics.setPathStatisticsLoaded(false);
            
            $scope.resetGeneticStatistics();
        };
        
        $scope.updatePathStatistics = function (path) {
            var pathStatistics = Statistics.getPathStatistics();

            var table = $('#graph-paths').DataTable();
            table.row.add([pathStatistics.counter, path.length, Statistics.getPathCost(path)]);

            table.draw();
        };
        
        $scope.initPathStatistics = function () {
            $scope.pathStatisticsLoaded = true;
            Notification.success({message: 'Paths Computed', delay: 2000});
            var $pathStatisticsToggle = $('#path-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $pathStatisticsToggle.change(function () {
                $scope.hidePathStatisticsView();
            });
            Statistics.setPathStatisticsLoaded(true);
        };
        
        $scope.hidePathStatistics = function () {
            $scope.pathStatisticsDisplayed = $('#path-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        
        $scope.resetGeneticStatistics = function () {
            $scope.evolutionChart.clear('morris-bar-cost-chart');
            var $slider = $('div.slider.slider-horizontal');
            if ($slider && $slider.parentElement) {
                var parent = $slider.parentElement;
                while (parent.firstChild) {
                    parent.removeChild(parent.firstChild);
                }
            }
            $('div.slider.slider-horizontal').css('width', 'auto');
            Statistics.setGeneticStatisticsLoaded(false);
            $scope.load.evolutionComputed = false;
        };
        
        $scope.initGeneticStatistics = function () {
            Statistics.markEvolutionEnd();

            //$scope.notifyInfo('Computing Statistics...');
            var interval = window.setInterval(function () {
                var geneticStatistics = Statistics.getGeneticStatistics();

                var $slider = $('#generation-slider').slider();
                $slider.on('slide', function (ev) {
                    $scope.sliderEvent(ev);
                });
                $slider.slider('setValue', 0);
                $('div.slider.slider-horizontal').css('width', '100%');
                $scope.evolutionChart.initialize('morris-bar-cost-chart',
                        geneticStatistics.generationChart);
                Statistics.setGeneticStatisticsLoaded(true);
                window.clearInterval(interval);
                $scope.$apply();
            }, 1000);
            var $geneticStatisticsToggle = $('#genetic-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $geneticStatisticsToggle.change(function () {
                $scope.hideGeneticStatisticsView();
            });
        };
        
        $scope.hideGeneticStatisticsView = function () {
            $scope.geneticStatisticsDisplayed = $('#genetic-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
}]);

