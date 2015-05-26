

gaps.controller('geneticstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GraphStatistics', 'GeneticStatistics', 'Graph',
    function ($rootScope, $scope, Notification, GraphStatistics, GeneticStatistics, Graph) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;

        $scope.dataToggleId = '#genetic-statistics-viewer-toggle';
        $scope.sliderContainerId = '#div.slider.slider-horizontal';
        $scope.sliderId = '#generation-slider';
        $scope.statisticsInfoCardValue = [];

        $scope.evolutionTimelineChartId = 'evolution-timeline';
        $scope.evolutionTimeline = null;
        $scope.evolutionTimelineOptions = {
            height: '400px',
            editable: false,
            clickToUse: true,
            showCurrentTime: false,
            showCustomTime: false,
            showMajorLabels: false,
            min: -500,
            start: -500,
            zoomMin: 150,
            zoomMax: 5
        };

        $scope.costChartElementId = 'costs-timeline';
        $scope.costChart = null;
        $scope.costChartOptions = {
            height: '400px',
            editable: false,
            clickToUse: true,
            showCurrentTime: false,
            showCustomTime: false,
            showMajorLabels: false,
            min: 0,
            max: 1000,
            start: 0,
            end: 1000,
            zoomMin: 150,
            zoomMax: 1000
        };

        $scope.getStatistics = function () {
            return GeneticStatistics.getStatistics();
        };

        $scope.getGraphStatistics = function () {
            return GraphStatistics;
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
            var $slider = $($scope.sliderContainerId);

            if ($slider && $slider.parentElement) {
                var parent = $slider.parentElement;
                while (parent.firstChild) {
                    parent.removeChild(parent.firstChild);
                }
            }
            $($scope.sliderContainerId).css('width', 'auto');

            if ($scope.evolutionTimeline && $scope.evolutionTimeline !== null && $scope.evolutionTimeline !== undefined) {
                $scope.evolutionTimeline.destroy();
                $scope.evolutionTimeline = null;
            }

            if ($scope.costChart && $scope.costChart !== null && $scope.costChart !== undefined) {
                $scope.costChart.destroy();
                $scope.costChart = null;
            }

            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function () {
            GeneticStatistics.markEvolutionEnd();

            var interval = window.setInterval(function () {
                var geneticStatistics = GeneticStatistics.getStatistics();
                $scope.statisticsInfoCardValue = $scope.buildInfoCardValue(geneticStatistics);

                $scope.$apply();

                var $slider = $($scope.sliderId).slider();
                $slider.on('slideStop', function (ev) {
                    $scope.sliderEvent(ev);
                });
                $slider.slider('setValue', 0);

                $scope.evolutionTimelineOptions.zoomMax = (geneticStatistics.evolutionStage * 5);
                $scope.evolutionTimeline = new vis.Timeline(
                        document.getElementById($scope.evolutionTimelineChartId),
                        GeneticStatistics.getEvolutionDataSet(),
                        $scope.evolutionTimelineOptions);

                $scope.costChartOptions.max = geneticStatistics.evolutionStage + 1000;
                $scope.costChartOptions.end = geneticStatistics.evolutionStage + 1000;
                $scope.costChartOptions.zoomMax = geneticStatistics.evolutionStage + 1000;
                $scope.costChart = new vis.Graph2d(document.getElementById(
                        $scope.costChartElementId),
                        GeneticStatistics.getCostsDataSet(),
                        $scope.costChartOptions);

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

        $scope.buildInfoCardValue = function ($data) {
            var value = [];
            value.push({title: "Found in generation", value: $data.evolutionStage});
            value.push({title: "Cost", value: $data.bestPathCost});
            value.push({title: "Fitness", value: $data.bestPathFitness});
            value.push({title: "Path Length", value: $data.bestPathEdgeNumber});
            value.push({title: "Evolution Started", value: $scope.getEvolutionStartTime()});
            value.push({title: "Evolution Finished", value: $scope.getEvolutionEndTime()});
            value.push({title: "Evolution Time", value: $scope.getEvolutionDiffTime()});

            return value;
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
    }]);