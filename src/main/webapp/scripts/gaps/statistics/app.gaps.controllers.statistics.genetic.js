

gaps.controller('geneticstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GraphStatistics', 'GeneticStatistics', 'CompareStatistics', 'Graph',
    function ($rootScope, $scope, Notification, GraphStatistics, GeneticStatistics, CompareStatistics, Graph) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;

        $scope.dataToggleId = '#genetic-statistics-viewer-toggle';
        $scope.sliderContainerId = '#div.slider.slider-horizontal';
        $scope.sliderId = '#generation-slider';
        $scope.statisticsInfoCardValue = [];
        $scope.selectedPathInfoCardValue = [];
        $scope.selectedPath = [];

        $scope.evolutionTimelineChartId = 'evolution-timeline';
        $scope.evolutionTimeline = null;
        $scope.evolutionTimelineOptions = {
            height: '400px',
            editable: false,
            clickToUse: true,
            showCurrentTime: false,
            showCustomTime: false,
            showMajorLabels: true,
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
            shaded: {
                orientation: 'bottom'
            },
            interpolation: false,
            sampling: false,
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
            $($scope.sliderId).attr('data-slider-max', 0);
            $($scope.sliderId).attr('data-slider-value', 0);
            $($scope.sliderId).attr('value', 0);
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

                $($scope.sliderId).attr('data-slider-max', (GeneticStatistics.getStatistics()).generations.length - 1);
                var $slider = $($scope.sliderId).slider();
                $slider.slider('setValue', 0);
                $slider.on('slide', function (ev) {
                    $scope.sliderEvent(ev);
                });

                $scope.evolutionTimelineOptions.start = geneticStatistics.startTimestamp;
                $scope.evolutionTimelineOptions.end = Date.now();
                $scope.evolutionTimelineOptions.min = geneticStatistics.startTimestamp;
                $scope.evolutionTimelineOptions.max = Date.now();
                $scope.evolutionTimelineOptions.zoomMin = 100;
                $scope.evolutionTimelineOptions.zoomMax = 1000 * 60 * 60;
                $scope.evolutionTimeline = new vis.Timeline(
                        document.getElementById($scope.evolutionTimelineChartId),
                        GeneticStatistics.getEvolutionDataSet(),
                        $scope.evolutionTimelineOptions);

                $scope.evolutionTimeline.on('select', function (properties) {
                    if (properties.items && properties.items[0]) {
                        var selectedChromosome = GeneticStatistics.getEvolutionDataSet()
                                .get(properties.items[0]);
                        if (selectedChromosome && selectedChromosome.path) {
                            var index = 0;
                            for (index = 0; index < GeneticStatistics.getEvolutionDataSet().length; index++) {
                                if ((GeneticStatistics.getEvolutionDataSet().get())[index].id ===
                                        selectedChromosome.id) {
                                    break;
                                }
                            }
                            $scope.selectGeneration(index);

                            var $slider = $($scope.sliderId).slider();
                            $slider.slider('setValue', index);
                        }
                    }
                });

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
            $scope.selectGeneration(event.value);
        };
        $scope.selectGapsBest = function () {
            var generations = GeneticStatistics.getGenerations();
            var generationIndex = 0;

            if (generations != null && generations.length > 0) {
                generationIndex = generations.length - 1;
            }

            $scope.selectGeneration(generationIndex, true);

            var $slider = $($scope.sliderId).slider();
            $slider.slider('setValue', generationIndex);
        };
        $scope.selectJgraphtBest = function () {
            var compareStatistics = CompareStatistics.getStatistics();
            var generationIndex = 0;

            if (Graph.getNetwork() && compareStatistics.chromosomes && compareStatistics.chromosomes.length >= 0) {
                generationIndex = 0;

                var chromosome = compareStatistics.chromosomes[generationIndex];
                var $path = (compareStatistics.chromosomes[generationIndex]).path;

                GeneticStatistics.setSelectedGenerationIndex(0);
                GeneticStatistics.setSelectedGeneration(chromosome);

                $scope.selectedPathInfoCardValue = [];
                $scope.selectedPathInfoCardValue.push({title: "Cost", value: chromosome.cost});
                $scope.selectedPathInfoCardValue.push({title: "Fitness", value: chromosome.fitness});
                $scope.selectedPathInfoCardValue.push({title: "Path Length", value: chromosome.path.length});

                $scope.selectPath(Graph.getNetwork(), $path);

                if ($scope.evolutionTimeline && $scope.evolutionTimeline !== null && $scope.evolutionTimeline !== undefined) {
                    $scope.evolutionTimeline.setSelection([]);
                }

                var $slider = $($scope.sliderId).slider();
                $slider.slider('setValue', 0);
            }
        };
        $scope.selectGeneration = function (generationIndex, skipApply) {

            GeneticStatistics.setSelectedGenerationIndex(generationIndex);
            var generation = GeneticStatistics.getGeneration(generationIndex);

            if (generation && generation !== null) {
                GeneticStatistics.setSelectedGeneration(generation);

                if (Graph.getNetwork() && generation.bestChromosome) {
                    var $path = generation.bestChromosome.path;

                    $scope.selectedPathInfoCardValue = [];
                    $scope.selectedPathInfoCardValue.push({title: "Generation", value: generation.evolutionStage});
                    $scope.selectedPathInfoCardValue.push({title: "Best Cost", value: generation.bestChromosome.cost});
                    $scope.selectedPathInfoCardValue.push({title: "Path Length", value: generation.bestChromosome.path.length});
                    $scope.selectedPathInfoCardValue.push({title: "Average Cost", value: generation.endAverageCost});
                    $scope.selectedPathInfoCardValue.push({title: "Best Fitness", value: generation.bestChromosome.fitness});
                    $scope.selectedPathInfoCardValue.push({title: "Average Fitness", value: generation.endAverageFitness});

                    $scope.selectPath(Graph.getNetwork(), $path);
                }

                if (!skipApply || !skipApply == true) {
                    $scope.$apply();
                }

                if ($scope.evolutionTimeline && $scope.evolutionTimeline !== null && $scope.evolutionTimeline !== undefined) {
                    var dataSetItems = GeneticStatistics.getEvolutionDataSet().get();
                    var selectedItem = dataSetItems[generationIndex];

                    if (selectedItem && selectedItem.id) {
                        $scope.evolutionTimeline.setSelection(selectedItem.id);
                    }
                }
            }
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

            $scope.selectedPath = path;
        };

        $scope.resetPathSelection = function () {

            GeneticStatistics.setSelectedGenerationIndex(0);
            GeneticStatistics.setSelectedGeneration(null);

            var $slider = $($scope.sliderId).slider();
            $slider.slider('setValue', 0);

            if ($scope.evolutionTimeline && $scope.evolutionTimeline !== null && $scope.evolutionTimeline !== undefined) {
                $scope.evolutionTimeline.setSelection([]);
            }

            if (Graph.getNetwork()) {
                Graph.getNetwork().selectNodes([]);
                Graph.getNetwork().selectEdges([]);
            }
            $scope.selectedPath = [];
            $scope.selectedPathInfoCardValue = [];
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