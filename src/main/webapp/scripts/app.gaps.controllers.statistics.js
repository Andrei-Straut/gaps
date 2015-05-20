

gaps.controller('statisticscontroller', ['$rootScope', '$scope', 'Notification', 'Statistics', 'Graph',
    function ($rootScope, $scope, Notification, Statistics, Graph) {

        $scope.graphStatisticsDisplayed = true;
        $scope.pathStatisticsDisplayed = true;
        $scope.geneticStatisticsDisplayed = true;
        $scope.compareStatisticsDisplayed = true;

        $scope.getStatistics = function () {
            return Statistics;
        };

        $rootScope.$on('resetViews', function (event, $data) {
            Statistics.resetGraphStatistics();
            Statistics.resetPathStatistics();
            Statistics.resetGeneticStatistics();
            Statistics.resetCompareStatistics();

            $scope.resetGraphStatistics();
            $scope.resetPathStatistics();
            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
        });

        $rootScope.$on('graphDataLoaded', function (event, $data) {
            $scope.resetGraphStatistics();
            $scope.resetPathStatistics();
            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
            $scope.initGraphStatistics($data);
        });

        $rootScope.$on('pathDataUpdated', function (event, $data) {
            $scope.updatePathStatistics($data);
        });

        $rootScope.$on('pathDataLoaded', function (event, $data) {
            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
            $scope.initPathStatistics();
        });

        $rootScope.$on('geneticDataUpdated', function (event, $data) {
        });

        $rootScope.$on('geneticDataLoaded', function (event, $data) {
            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
            $scope.initGeneticStatistics();
        });

        $rootScope.$on('compareDataLoaded', function (event, $data) {
            $scope.resetCompareStatistics();
            $scope.initCompareStatistics();
        });

        $scope.initGraphStatistics = function ($data) {
            Statistics.setGraphStatisticsLoaded(true);
            var table = $('#graph-direct-edges').DataTable();
            angular.forEach($data, function (edgeValue, edgeKey) {
                table.row.add([edgeValue.id, edgeValue.from, edgeValue.to, edgeValue.label]);
            });
            table.draw();

            var $graphStatisticsToggle = $('#graph-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $graphStatisticsToggle.change(function () {
                $scope.hideGraphStatistics();
            });
            Notification.success({message: 'Loaded', delay: 5000});
        };

        $scope.hideGraphStatistics = function () {
            $scope.graphStatisticsDisplayed = $('#graph-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };

        $scope.resetGraphStatistics = function () {
            var table = $('#graph-direct-edges').DataTable();
            table.clear();
            Statistics.setGraphStatisticsLoaded(false);

            $scope.resetPathStatistics();
            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
        };

        $scope.updatePathStatistics = function (path) {
            var pathStatistics = Statistics.getPathStatistics();

            var table = $('#graph-paths').DataTable();
            table.row.add([pathStatistics.counter, path.length, Statistics.getPathCost(path)]);

            table.draw();
        };

        $scope.initPathStatistics = function () {
            Statistics.setPathStatisticsLoaded(true);

            var $pathStatisticsToggle = $('#path-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $pathStatisticsToggle.change(function () {
                $scope.hidePathStatistics();
            });
            Notification.success({message: 'Paths Computed', delay: 2000});
        };

        $scope.hidePathStatistics = function () {
            $scope.pathStatisticsDisplayed = $('#path-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };

        $scope.resetPathStatistics = function () {
            var table = $('#graph-paths').DataTable();
            table.clear();
            Statistics.setPathStatisticsLoaded(false);

            $scope.resetGeneticStatistics();
            $scope.resetCompareStatistics();
        };

        $scope.initGeneticStatistics = function () {
            Statistics.markEvolutionEnd();

            var interval = window.setInterval(function () {
                var geneticStatistics = Statistics.getGeneticStatistics();
                Statistics.setGeneticStatisticsLoaded(true);
                $scope.$apply();

                var $slider = $('#generation-slider').slider();
                $slider.on('slideStop', function (ev) {
                    $scope.sliderEvent(ev);
                });
                $slider.slider('setValue', 0);
                $('div.slider.slider-horizontal').css('width', '100%');
                $scope.evolutionChart.initialize('morris-bar-cost-chart',
                        geneticStatistics.generationChart);
                window.clearInterval(interval);
            }, 1000);
            var $geneticStatisticsToggle = $('#genetic-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $geneticStatisticsToggle.change(function () {
                $scope.hideGeneticStatistics();
            });
        };

        $scope.hideGeneticStatistics = function () {
            $scope.geneticStatisticsDisplayed = $('#genetic-statistics-viewer-toggle').prop('checked');
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
            $scope.resetCompareStatistics();
        };

        $scope.initCompareStatistics = function () {
            Statistics.markCompareEnd();

            var interval = window.setInterval(function () {
                var compareStatistics = Statistics.getCompareStatistics();
                Statistics.setCompareStatisticsLoaded(true);
                $scope.$apply();
                $scope.compareChart.initialize('morris-bar-compare-chart', compareStatistics.compareChart);
                window.clearInterval(interval);

                Notification.success({message: 'Done', delay: 2000});
            }, 1000);
            var $compareStatisticsToggle = $('#compare-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $compareStatisticsToggle.change(function () {
                $scope.hideCompareStatistics();
            });
        };

        $scope.hideCompareStatistics = function () {
            $scope.compareStatisticsDisplayed = $('#compare-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };

        $scope.resetCompareStatistics = function () {
            $scope.compareChart.clear('morris-bar-compare-chart');
            Statistics.setCompareStatisticsLoaded(false);
        };

        $scope.sliderEvent = function (event) {
            var geneticStatistics = Statistics.getGeneticStatistics();

            geneticStatistics.selectedGenerationIndex = event.value;
            if (geneticStatistics.generations[event.value]) {
                geneticStatistics.selectedGeneration =
                        geneticStatistics.generations[
                                geneticStatistics.selectedGenerationIndex];
            }

            if (Graph.getNetwork() && geneticStatistics && geneticStatistics.selectedGeneration
                    && geneticStatistics.selectedGeneration.bestChromosome) {
                var $path = geneticStatistics.selectedGeneration.bestChromosome.path;
                $scope.selectPath(Graph.getNetwork(), $path);
            }

            $scope.$apply();
        };

        $scope.selectPath = function (graph, path) {
            var nodeSelectionArray = [];
            var edgeSelectionArray = [];
            if (path) {
                angular.forEach(path, function (edgeValue, edgeKey) {
                    nodeSelectionArray.push(edgeValue.from);
                    if (edgeValue && edgeValue.id) {
                        edgeSelectionArray.push(edgeValue.id);
                    }
                });
                if ((path[path.length - 1]) && (path[path.length - 1]).to) {
                    nodeSelectionArray.push((path[path.length - 1]).to);
                }
            }

            graph.selectNodes(nodeSelectionArray);
            graph.selectEdges(edgeSelectionArray);
        };

        $scope.resetPathSelection = function () {
            if (Graph.getNetwork()) {
                Graph.getNetwork().selectNodes([]);
                Graph.getNetwork().selectEdges([]);
            }

            var geneticStatistics = Statistics.getGeneticStatistics();
            geneticStatistics.selectedGeneration = {};
            geneticStatistics.selectedGenerationIndex = 0;
        };

        $scope.evolutionChart = {
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
                    ykeys: ['a'],
                    labels: ['Best Chromosome'],
                    hideHover: 'auto',
                    resize: true
                });
            },
            initialize: function (element, data) {
                $scope.evolutionChart.chart(element, data);
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

