/**
 * Main GAPS controller
 */
gaps.controller('gapscontroller', ['$scope', 'Socket', 'Notification', function ($scope, $socket, Notification) {
        /*
         * =====================================================================
         * Main functions
         * =====================================================================
         */
        $scope.processGraph = function () {
            $scope.clearNotifs(3000);
            $scope.notifyInfo('Loading graph...');

            // Little hack to give notification time to pop-up
            var interval = window.setInterval(function () {
                $scope.graphGeneration = $socket.getGraph($scope.graphSettings);

                $scope.graphGeneration.then(function (response) {
                    if (response.status === 200) {
                        $scope.load.dataLoaded = true;
                        $scope.initGraphView(response.data.graph);
                        $scope.initGraphStatistics(response.data);
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }).catch(function (e) {
                    $scope.notifyError(e.description, $('#modalLoadingError'));
                });
                window.clearInterval(interval);
            }, 1000);

        };
        $scope.computePaths = function () {
            $scope.notifyInfo('Computing paths...');

            var interval = window.setInterval(function () {

                $scope.resetPathStatisticsAndTable();
                $scope.resetGeneticStatisticsAndView();
                $scope.resetCompareStatisticsAndView();
                $scope.geneticEvolution = $socket.computePaths($scope.geneticSettings);
                $scope.geneticEvolution.then(function (response) {

                    if (response.status === 200) {
                        $scope.initPathStatistics();
                        $scope.evolve();
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    if (update.data && update.data.path) {
                        $scope.updatePathStatisticsData(update.data.path);
                    }
                });
                window.clearInterval(interval);
            }, 1000);
        };
        $scope.evolve = function () {
            $scope.geneticStatistics.startTimestamp = new Date();
            $scope.notifyInfo('Evolving...');

            var interval = window.setInterval(function () {
                $scope.geneticEvolution = $socket.evolve($scope.geneticSettings);

                $scope.geneticEvolution.then(function (response) {

                    if (response.status === 200) {
                        $scope.initGeneticStatistics();
                        $scope.load.evolutionComputed = true;

                        $scope.compare();
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    $scope.updateGeneticStatisticsData(update.data);
                });
                window.clearInterval(interval);
            }, 1000);
        };
        $scope.compare = function () {
            $scope.compareStatisticsKShortest.startTimestamp = new Date();
            $scope.notifyInfo('Comparing Results...');

            var interval = window.setInterval(function () {
                $scope.geneticResultsCompare = $socket.compare($scope.geneticSettings);

                $scope.geneticResultsCompare.then(function (response) {

                    if (response.status === 200) {
                        $scope.initCompareStatistics();
                        $scope.load.compareStatisticsLoaded = true;
                        $scope.notifySuccess('Done');
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    $scope.updateCompareStatistics(update.data, $scope.geneticStatistics);
                });
                window.clearInterval(interval);
            }, 1000);
        }

        /*
         * =====================================================================
         * View and statistics functions
         * =====================================================================
         */

        $scope.showGeneticSettingsAdvanced = function () {
            $('#geneticSettingsAdvancedModal').modal('show');
        };
        $scope.initGraphView = function ($data) {
            $scope.resetGraphView();
            $scope.resetGraphStatistics();
            $scope.resetPathStatisticsAndTable();
            $scope.resetGeneticStatisticsAndView();
            $scope.resetCompareStatisticsAndView();
            $scope.graph = jitInit($data);
            $(window).scrollTop($(window).scrollTop() + 1);
            $(window).scrollTop($(window).scrollTop() - 1);
            $scope.load.graphViewerLoaded = true;

            var $graphViewerToggle = $('#graph-viewer-toggle').bootstrapToggle({
                on: 'Hide',
                off: 'Show'
            });
            $graphViewerToggle.change(function () {
                $scope.hideGraphView();
            });
        };
        $scope.hideGraphView = function () {
            $scope.load.graphDisplayed = $('#graph-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.resetGraphView = function () {
            var graphWrapper = document.getElementById("infovis");
            if (graphWrapper && graphWrapper.firstChild) {
                while (graphWrapper.firstChild) {
                    graphWrapper.removeChild(graphWrapper.firstChild);
                }
            }
            var logWrapper = document.getElementById("log");
            if (logWrapper && logWrapper.firstChild) {
                while (logWrapper.firstChild) {
                    logWrapper.removeChild(logWrapper.firstChild);
                }
            }
            var labelWrapper = document.getElementById("inner-details");
            if (labelWrapper && labelWrapper.firstChild) {
                while (labelWrapper.firstChild) {
                    labelWrapper.removeChild(labelWrapper.firstChild);
                }
            }
            $scope.load.graphViewerLoaded = false;
        };
        $scope.initGraphStatistics = function ($data) {
            var table = $('#graph-direct-edges').DataTable();
            angular.forEach($data.edges, function (edgeValue, edgeKey) {
                table.row.add([edgeValue.data.id, edgeValue.nodeFrom, edgeValue.nodeTo, edgeValue.data.cost]);
            });
            table.draw();

            $scope.graphStatistics = $data.statistics;
            $scope.load.graphStatisticsLoaded = true;
            $scope.notifySuccess('Loaded');

            var $graphStatisticsToggle = $('#graph-statistics-viewer-toggle').bootstrapToggle({
                on: 'Hide',
                off: 'Show'
            });
            $graphStatisticsToggle.change(function () {
                $scope.hideGraphStatisticsView();
            });
        };
        $scope.hideGraphStatisticsView = function () {
            $scope.load.graphStatisticsDisplayed = $('#graph-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.resetGraphStatistics = function () {
            $scope.graphStatistics.numberOfNodes = 1;
            $scope.graphStatistics.numberOfEdges = 1;
            $scope.graphStatistics.minimumEdgeCost = 1;
            $scope.graphStatistics.maximumEdgeCost = 1;
            $scope.graphStatistics.totalEdgeCost = 1;
            $scope.graphStatistics.averageEdgeCost = 1;
            $scope.graphStatistics.averageEdgesPerNode = 1;
            var table = $('#graph-direct-edges').DataTable();
            table.clear();
            $scope.load.graphStatisticsLoaded = false;
        };
        $scope.initPathStatistics = function () {
            $scope.pathStatistics.averagePathCost =
                    $scope.pathStatistics.totalPathCost / $scope.pathStatistics.paths.length;
            $scope.load.pathStatisticsLoaded = true;
            $scope.notifySuccess('Paths Computed');

            var $pathStatisticsToggle = $('#path-statistics-viewer-toggle').bootstrapToggle({
                on: 'Hide',
                off: 'Show'
            });
            $pathStatisticsToggle.change(function () {
                $scope.hidePathStatisticsView();
            });
        };
        $scope.hidePathStatisticsView = function () {
            $scope.load.pathStatisticsDisplayed = $('#path-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.updatePathStatisticsData = function (path) {
            var table = $('graph-paths').DataTable();
            var numberOfEdges = path.length;
            var totalCost = 0;
            angular.forEach(path, function (edge) {
                if (edge.data && edge.data.cost) {
                    totalCost += parseInt(edge.data.cost);
                }
            });
            if (totalCost > $scope.pathStatistics.mostExpensivePathCost) {
                $scope.pathStatistics.mostExpensivePathCost = totalCost;
                $scope.pathStatistics.mostExpensivePath = path;
            }

            if (totalCost < $scope.pathStatistics.cheapestPathCost) {
                $scope.pathStatistics.cheapestPathCost = totalCost;
                $scope.pathStatistics.cheapestPath = path;
            }

            if (numberOfEdges > $scope.pathStatistics.longestPathEdgeNumber) {
                $scope.pathStatistics.longestPathEdgeNumber = numberOfEdges;
            }

            if (numberOfEdges < $scope.pathStatistics.shortestPathEdgeNumber) {
                $scope.pathStatistics.shortestPathEdgeNumber = numberOfEdges;
            }

            $scope.pathStatistics.totalPathCost += totalCost;
            $scope.pathStatistics.paths.push(path);
            var table = $('#graph-paths').DataTable();
            table.row.add([$scope.pathStatistics.counter, path.length, totalCost]);
            $scope.pathStatistics.counter += 1;
            table.draw();

            $scope.notifyInfo('Computing paths (' + $scope.pathStatistics.counter + ' / ' + $scope.geneticSettings.numberOfPaths + ')...');
        };
        $scope.resetPathStatisticsAndTable = function () {
            $scope.pathStatistics.counter = $scope.geneticSettings.sourceNode;
            $scope.pathStatistics.sourceNodeId = $scope.geneticSettings.destinationNode;
            $scope.pathStatistics.destinationNodeId = 0;
            $scope.pathStatistics.longestPathEdgeNumber = 10000000;
            $scope.pathStatistics.shortestPathEdgeNumber = 0;
            $scope.pathStatistics.totalPathCost = 0;
            $scope.pathStatistics.cheapestPathCost = 10000000;
            $scope.pathStatistics.mostExpensivePathCost = 0;
            $scope.pathStatistics.averagePathCost = 1;
            $scope.pathStatistics.paths = [];
            $scope.pathStatistics.cheapestPath = [];
            $scope.pathStatistics.mostExpensivePath = [];
            var table = $('#graph-paths').DataTable();
            table.clear();
            $scope.load.pathStatisticsLoaded = false;
        };
        $scope.initGeneticStatistics = function () {
            $scope.notifySuccess('Evolved');
            $scope.geneticStatistics.endTimestamp = new Date();
            $scope.geneticStatistics.evolutionDiffTimestamp = $scope.geneticStatistics.endTimestamp - $scope.geneticStatistics.startTimestamp;
            $scope.notifyInfo('Computing Statistics...');

            var interval = window.setInterval(function () {

                var $slider = $('#generation-slider').slider();
                $slider.on('slide', function (ev) {
                    $scope.sliderEvent(ev);
                });
                $slider.slider('setValue', 0);

                $('div.slider.slider-horizontal').css('width', '100%');
                $scope.evolutionChart.initialize('morris-bar-cost-chart',
                        $scope.geneticStatistics.generationChart);
                $scope.load.geneticStatisticsLoaded = true;

                window.clearInterval(interval);
                $scope.$apply();
            }, 1000);

            var $geneticStatisticsToggle = $('#genetic-statistics-viewer-toggle').bootstrapToggle({
                on: 'Hide',
                off: 'Show'
            });
            $geneticStatisticsToggle.change(function () {
                $scope.hideGeneticStatisticsView();
            });
        };
        $scope.hideGeneticStatisticsView = function () {
            $scope.load.geneticStatisticsDisplayed = $('#genetic-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.resetGeneticStatisticsAndView = function () {
            $scope.geneticStatistics.counter = 1;
            $scope.geneticStatistics.evolutionStage = 1;
            $scope.geneticStatistics.selectedGenerationIndex = 0;
            $scope.geneticStatistics.selectedGeneration = {};
            $scope.geneticStatistics.bestPathEdgeNumber = 0;
            $scope.geneticStatistics.bestPathFitness = 10000000;
            $scope.geneticStatistics.bestPathCost = 10000000;
            $scope.geneticStatistics.startTimestamp = {};
            $scope.geneticStatistics.endTimestamp = {};
            $scope.geneticStatistics.evolutionDiffTimestamp = {};
            $scope.geneticStatistics.generationChart = [];
            $scope.geneticStatistics.generations = [];
            $scope.geneticStatistics.bestPath = {};

            $scope.evolutionChart.clear('morris-bar-cost-chart');

            var $slider = $('div.slider.slider-horizontal');
            if ($slider && $slider.parentElement) {
                var parent = $slider.parentElement;
                while (parent.firstChild) {
                    parent.removeChild(parent.firstChild);
                }
            }
            $('div.slider.slider-horizontal').css('width', 'auto');

            $scope.load.geneticStatisticsLoaded = false;
            $scope.load.evolutionComputed = false;
        };
        $scope.updateGeneticStatisticsData = function (data) {
            if (data.bestChromosome) {
                if (JSON.stringify($scope.geneticStatistics.bestPath) === '{}'
                        || data.bestChromosome.cost < $scope.geneticStatistics.bestPath.cost) {
                    $scope.geneticStatistics.bestPath = data.bestChromosome;
                    $scope.geneticStatistics.evolutionStage = data.evolutionStage;
                    $scope.geneticStatistics.bestPathEdgeNumber = data.bestChromosome.path.length;
                    $scope.geneticStatistics.bestPathCost = data.bestChromosome.cost;
                    $scope.geneticStatistics.bestPathFitness = data.bestChromosome.fitness;
                }
            }
            $scope.geneticStatistics.generations.push(data);
            $scope.geneticStatistics.bestPath = data.bestChromosome;

            var generationDataChart = {};
            generationDataChart.y = 'Gen ' + data.evolutionStage;
            generationDataChart.a = data.endBestCost;
            $scope.geneticStatistics.generationChart.push(generationDataChart);

            $scope.notifyInfo('Evolving (' + data.evolutionStage + ' / ' + $scope.geneticSettings.numberOfEvolutions + ')...');
        };
        $scope.initCompareStatistics = function () {
            $scope.compareStatisticsKShortest.endTimestamp = new Date();
            $scope.compareStatisticsKShortest.compareDiffTimestamp =
                    $scope.compareStatisticsKShortest.endTimestamp - $scope.compareStatisticsKShortest.startTimestamp;

            var interval = window.setInterval(function () {

                $scope.compareChart.initialize('morris-bar-compare-chart', $scope.compareStatisticsKShortest.compareChart);
                $scope.load.compareStatisticsLoaded = true;

                window.clearInterval(interval);
            }, 1000);

            var $compareStatisticsToggle = $('#compare-statistics-viewer-toggle').bootstrapToggle({
                on: 'Hide',
                off: 'Show'
            });
            $compareStatisticsToggle.change(function () {
                $scope.hideCompareStatisticsView();
            });
        };
        $scope.hideCompareStatisticsView = function () {
            $scope.load.compareStatisticsDisplayed = $('#compare-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.resetCompareStatisticsAndView = function () {
            $scope.compareStatisticsKShortest.startTimestamp = {};
            $scope.compareStatisticsKShortest.endTimestamp = {};
            $scope.compareStatisticsKShortest.compareDiffTimestamp = {};
            $scope.compareStatisticsKShortest.compareChart = [];
            $scope.compareStatisticsKShortest.chromosomes = [];

            $scope.compareChart.clear('morris-bar-compare-chart');

            $scope.load.compareStatisticsLoaded = false;
        };
        $scope.updateCompareStatistics = function (data, geneticStatistics) {
            if (data) {
                $scope.compareStatisticsKShortest.chromosomes.push(data);

                var resultsCompareChart = {};
                resultsCompareChart.y = 'GAPS Best, KShortest #' + $scope.compareStatisticsKShortest.compareChart.length;
                resultsCompareChart.GAPS = geneticStatistics.bestPathCost;
                resultsCompareChart.KShortest = data.cost;

                $scope.compareStatisticsKShortest.compareChart.push(resultsCompareChart);
            }
        };


        /*
         * =====================================================================
         * Data and utility functions
         * =====================================================================
         */
        $scope.setActive = function (id) {
            angular.forEach($('.nav-menu-left'), function (item) {
                $(item).removeClass('active-menu');
            });
            $('#' + id).addClass('active-menu');
        };
        $scope.getEdgeClass = function (edgeCost) {
            var averageCostLowThreshold = $scope.graphStatistics.averageEdgeCost - ($scope.graphStatistics.averageEdgeCost / 4);
            var averageCostHighThreshold = $scope.graphStatistics.averageEdgeCost + ($scope.graphStatistics.averageEdgeCost / 4);
            if (edgeCost < averageCostLowThreshold) {
                return 'label-success';
            } else if (edgeCost >= averageCostLowThreshold && edgeCost <= averageCostHighThreshold) {
                return 'label-warning';
            } else {
                return 'label-danger';
            }
        };
        $scope.sliderEvent = function (event) {
            $scope.geneticStatistics.selectedGenerationIndex = event.value;
            if ($scope.geneticStatistics.generations[event.value]) {
                $scope.geneticStatistics.selectedGeneration =
                        $scope.geneticStatistics.generations[
                                $scope.geneticStatistics.selectedGenerationIndex];
            }

            if (window.fd && window.fd.graph && $scope.geneticStatistics && $scope.geneticStatistics.selectedGeneration
                    && $scope.geneticStatistics.selectedGeneration.bestChromosome) {
                var $path = $scope.geneticStatistics.selectedGeneration.bestChromosome.path;
                $scope.selectPath(window.fd, $path);
            }

            $scope.$apply();
        };
        $scope.notifyInfo = function (notification) {
            $scope.load.wip = true;
            $scope.load.wipType = notification;
        };
        $scope.notifySuccess = function (notification) {
            $scope.load.wip = false;
            $scope.load.wipType = '';
            Notification.success({message: notification, delay: $scope.notificationTime});
        };
        $scope.notifyError = function (notification, $modalElement) {
            $scope.load.wip = false;
            $scope.errorText = notification;
            $modalElement.modal('show');
        };
        // Due to problems with webkit transition detection, clear notifications manually
        $scope.clearNotifs = function (delay) {
            var interval = window.setInterval(function () {
                var notifys = angular.element(document.getElementsByClassName('killed'));

                if (notifys) {
                    angular.forEach(notifys, function (notify) {
                        notify.remove();
                    });
                }
            }, delay);
        };
        $scope.selectPath = function (jitGraph, path) {
            jitGraph.graph.eachNode(function (n) {
                n.unselect(GraphViewerOptions);
                n.unselectpath(GraphViewerOptions);
            });

            angular.forEach(path, function (edgeValue, edgeKey) {
                var nodeFrom = jitGraph.graph.getNode(edgeValue.nodeFrom);
                var nodeTo = jitGraph.graph.getNode(edgeValue.nodeTo);
                var edge = jitGraph.graph.getAdjacence(edgeValue.nodeFrom, edgeValue.nodeTo);
                jitGraph.canvas.getElement().style.cursor = 'move';

                if (nodeFrom) {
                    nodeFrom.selectpath(GraphViewerOptions);
                }

                if (nodeTo) {
                    nodeTo.selectpath(GraphViewerOptions);
                }

                if (edge) {
                    edge.selectpath(GraphViewerOptions);
                }
            });

            //trigger animation to final styles
            jitGraph.fx.animate({
                modes: ['node-property:dim',
                    'edge-property:lineWidth:color'],
                duration: GraphViewerOptions.selectAnimationDuration
            });
        };
        $scope.resetPathSelection = function () {
            if (window.fd && window.fd.graph) {
                window.fd.graph.eachNode(function (n) {
                    n.unselect(GraphViewerOptions);
                    n.unselectpath(GraphViewerOptions);
                });

                window.fd.fx.animate({
                    modes: ['node-property:dim',
                        'edge-property:lineWidth:color'],
                    duration: GraphViewerOptions.selectAnimationDuration
                });
            }

            $scope.geneticStatistics.selectedGeneration = {};
            $scope.geneticStatistics.selectedGenerationIndex = 0;
        };
        $scope.convertTime = function (timestamp) {
            if (timestamp !== undefined && timestamp !== null && timestamp != 'null') {
                var a = new Date(timestamp * 1);
                var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                var year = $.isNumeric(a.getFullYear()) ? a.getFullYear() : '0000';
                var month = $.isNumeric(a.getMonth()) ? months[a.getMonth()] : '00';
                var date = $.isNumeric(('0' + a.getDate()).slice(-2)) ? ('0' + a.getDate()).slice(-2) : '00';
                var hour = $.isNumeric(('0' + a.getHours()).slice(-2)) ? ('0' + a.getHours()).slice(-2) : '00';
                var min = $.isNumeric(('0' + a.getMinutes()).slice(-2)) ? ('0' + a.getMinutes()).slice(-2) : '00';
                var sec = $.isNumeric(('0' + a.getSeconds()).slice(-2)) ? ('0' + a.getSeconds()).slice(-2) : '00';
                var millis = $.isNumeric(('0' + a.getMilliseconds()).slice(-2)) ? ('0' + a.getMilliseconds()).slice(-2) : '00';
                var time = hour + ':' + min + ':' + sec + '.' + millis;
                return time;
            } else {
                return null;
            }
        };
        /* 
         * =====================================================================
         * Variables
         * =====================================================================
         */
        $scope.graph = {};
        // Keep track of what's been loaded
        $scope.load = {
            wip: false,
            wipType: '',
            dataLoaded: false,
            graphViewerLoaded: false,
            graphDisplayed: true,
            graphStatisticsLoaded: false,
            graphStatisticsDisplayed: true,
            pathStatisticsLoaded: false,
            pathStatisticsDisplayed: true,
            evolutionComputed: false,
            geneticStatisticsLoaded: false,
            geneticStatisticsDisplayed: true,
            compareStatisticsLoaded: false,
            compareStatisticsDisplayed: true
        };
        // Settings for graph generation
        $scope.graphSettings = {
            numberOfNodes: 30,
            numberOfNodesMax: 1000,
            numberOfEdges: 100,
            numberOfEdgesMax: 1000,
            minimumEdgeWeight: 1,
            maximumEdgeWeight: 100
        };
        // Settings for genetic algorithm
        $scope.geneticSettings = {
            sourceNode: 0,
            destinationNode: 29,
            numberOfPaths: $scope.graphSettings.numberOfEdges,
            numberOfEvolutions: 10000,
            stopConditionPercent: 100,
            minPopSizePercent: 50,
            keepPopSizeConstant: true,
            preserveFittestIndividual: true,
            comparePaths: 5
        };
        // Graph statistics per edge
        $scope.graphStatistics = {
            numberOfNodes: 1,
            numberOfEdges: 1,
            minimumEdgeCost: 1,
            maximumEdgeCost: 1,
            totalEdgeCost: 0,
            averageEdgeCost: 0.0,
            averageEdgesPerNode: 0.0
        };
        // Graph statistics per path
        $scope.pathStatistics = {
            counter: 1,
            sourceNodeId: $scope.geneticSettings.sourceNode,
            destinationNodeId: $scope.geneticSettings.destinationNode,
            longestPathEdgeNumber: 0,
            shortestPathEdgeNumber: 10000000,
            totalPathCost: 0,
            cheapestPathCost: 10000000,
            mostExpensivePathCost: 0,
            averagePathCost: 0,
            paths: [],
            cheapestPath: [],
            mostExpensivePath: []
        };
        // Genetic algorithm run statistics
        $scope.geneticStatistics = {
            counter: 1,
            evolutionStage: 1,
            selectedGenerationIndex: 0,
            selectedGeneration: {},
            sourceNodeId: $scope.geneticSettings.sourceNode,
            destinationNodeId: $scope.geneticSettings.destinationNode,
            bestPathEdgeNumber: 0,
            bestPathFitness: 10000000,
            bestPathCost: 10000000,
            startTimestamp: {},
            endTimestamp: {},
            evolutionDiffTimestamp: {},
            bestPath: {},
            generationChart: [],
            generations: []
        };
        // JGraphT KShortestPath comparison
        $scope.compareStatisticsKShortest = {
            startTimestamp: {},
            endTimestamp: {},
            compareDiffTimestamp: {},
            compareChart: [],
            chromosomes: []
        };
        // Websocket error messages
        $scope.errorText = '';
        // Notification duration
        $scope.notificationTime = 2000;
        // Charts
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
                var chartWrapper = document.getElementById(elementId);
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
                var chartWrapper = document.getElementById(elementId);
                if (chartWrapper && chartWrapper.firstChild) {
                    while (chartWrapper.firstChild) {
                        chartWrapper.removeChild(chartWrapper.firstChild);
                    }
                }
                $(chartWrapper).css('height', 'auto');

            }
        };
        
        $scope.updateKeepPopSizeConstant = function(open) {
            console.log(open);
        };

        $scope.models = {
            selected: null,
            lists: {
                "Available": [
                    {label: "SingleGeneMutator"},
                    {label: "OnePointCrossover"}
                ], 
                "Used": [
                    {label: "MultipleGeneMutator"},
                    {label: "CycleRemoveMutator"},
                    {label: "TwoPointCrossover"},
                    {label: "BestChromosomeSelector"}                    
                ]}
        };

        // Model to JSON for demo purpose
        $scope.$watch('models', function (model) {
            $scope.modelAsJson = angular.toJson(model, true);
        }, true);

    }]);