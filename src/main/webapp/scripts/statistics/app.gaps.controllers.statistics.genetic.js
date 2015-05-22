

gaps.controller('geneticstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GeneticStatistics', 'Graph',
    function ($rootScope, $scope, Notification, GeneticStatistics, Graph) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;

        $scope.costChartElementId = 'morris-bar-cost-chart';

        $scope.dataToggleId = '#genetic-statistics-viewer-toggle';
        $scope.sliderContainerId = '#div.slider.slider-horizontal';
        $scope.sliderId = '#generation-slider';

        $scope.getStatistics = function () {
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

        $rootScope.$on('geneticDataUpdated', function (event, $data) {
            GeneticStatistics.add($data);
        });

        $rootScope.$on('geneticDataLoaded', function (event, $data) {
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;
            $scope.initView();
        });

        $scope.resetData = function () {
            GeneticStatistics.reset();
        };

        $scope.resetView = function () {
            $scope.evolutionChart.clear($scope.costChartElementId);
            var $slider = $($scope.sliderContainerId);

            if ($slider && $slider.parentElement) {
                var parent = $slider.parentElement;
                while (parent.firstChild) {
                    parent.removeChild(parent.firstChild);
                }
            }
            $($scope.sliderContainerId).css('width', 'auto');

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function () {
            GeneticStatistics.markEvolutionEnd();

            var interval = window.setInterval(function () {
                var geneticStatistics = GeneticStatistics.getStatistics();
                $scope.$apply();

                var $slider = $($scope.sliderId).slider();
                $slider.on('slideStop', function (ev) {
                    $scope.sliderEvent(ev);
                });
                $slider.slider('setValue', 0);

                $scope.evolutionChart.initialize($scope.costChartElementId,
                        geneticStatistics.generationChart);
console.log(geneticStatistics);
                var date = new Date();
                var options = {
                    height: '400px',
                    editable: false,
                    clickToUse: true,
                    showCurrentTime: false,
                    showCustomTime: false,
                    min: -500,
                    max: (geneticStatistics.evolutionStage * 10),
                    zoomMin: 150,
                    zoomMax: (geneticStatistics.evolutionStage * 5)
                };
                var timeline = new vis.Timeline(document.getElementById('evolution-timeline'), GeneticStatistics.getDataSet(), options);

                window.clearInterval(interval);

                Notification.success({message: 'Evolved', delay: 2000});
            }, 1000);

            var $geneticStatisticsToggle = $($scope.dataToggleId).bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $geneticStatisticsToggle.change(function () {
                $scope.hideView();
            });
        };

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
        };

        $scope.sliderEvent = function (event) {
            var geneticStatistics = GeneticStatistics.getStatistics();

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

            var geneticStatistics = GeneticStatistics.getStatistics();
            geneticStatistics.selectedGeneration = {};
            geneticStatistics.selectedGenerationIndex = 0;
        };

        $scope.getEvolutionStartTime = function () {
            return GeneticStatistics.getEvolutionStartTime();
        };

        $scope.getEvolutionEndTime = function () {
            return GeneticStatistics.getEvolutionEndTime();
        };

        $scope.getEvolutionDiffTime = function () {
            return GeneticStatistics.getEvolutionDiffTime();
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
    }]);