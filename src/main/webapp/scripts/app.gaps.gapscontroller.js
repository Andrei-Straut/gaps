/**
 * Main GAPS controller
 */
gaps.controller('gapscontroller', ['$rootScope', '$scope', 'Socket', 'Statistics',
    function ($rootScope, $scope, $socket, Statistics) {

        $scope.init = function () {
            var $graphGenerationType = $('#graphGenerationType').bootstrapToggle({
                on: 'Static',
                off: 'Random'
            });
            $('#graphGenerationType').prop('checked', $scope.graphSettings.isStatic).change();
            $graphGenerationType.change(function () {
                $scope.graphSettings.isStatic = $('#graphGenerationType').prop('checked');
            });
        };

        $scope.getStatistics = function () {
            return Statistics;
        };

        /*
         * =====================================================================
         * Main functions
         * =====================================================================
         */
        $scope.uploadGraph = function () {
            $scope.clearNotifs(3000);
            Statistics.resetAllStatistics();
            $scope.notifyInfo('Uploading graph...');

            var interval = window.setInterval(function () {
                $('#graphSettingsAdvancedModal').modal('hide');

                $scope.graphUpload = $socket.uploadGraph($scope.graphJson);
                $scope.graphUpload.then(function (response) {
                    if (response.status === 200) {
                        $rootScope.$broadcast('resetViews', {});

                        $scope.initGraphView(response.data);
                        Statistics.setGraphStatistics(response.data.graph.edges);

                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                        $rootScope.$broadcast('graphDataLoaded', response.data.graph.edges);
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }).catch(function (e) {
                    $scope.notifyError(e.description, $('#modalLoadingError'));
                });

                window.clearInterval(interval);
            }, 1000);

        };
        $scope.processGraph = function () {
            $scope.clearNotifs(3000);
            Statistics.resetAllStatistics();
            $scope.notifyInfo('Loading graph...');

            // Little hack to give notification time to pop-up
            var interval = window.setInterval(function () {
                $scope.graphGeneration = $socket.getGraph($scope.graphSettings);
                $scope.graphGeneration.then(function (response) {
                    if (response.status === 200) {
                        $rootScope.$broadcast('resetViews', {});

                        $scope.initGraphView(response.data);
                        Statistics.setGraphStatistics(response.data.graph.edges);

                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                        $rootScope.$broadcast('graphDataLoaded', response.data.graph.edges);

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
            Statistics.resetPathStatistics();
            $scope.notifyInfo('Computing paths...');

            var interval = window.setInterval(function () {
                $scope.geneticEvolution = $socket.computePaths($scope.geneticSettings);
                $scope.geneticEvolution.then(function (response) {

                    if (response.status === 200) {
                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                        $rootScope.$broadcast('pathDataLoaded', {});
                        $scope.evolve();
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    if (update.data && update.data.path) {
                        Statistics.addPathStatistic(update.data.path);
                        $rootScope.$broadcast('pathDataUpdated', update.data.path);
                        $scope.notifyInfo('Computing paths (' + (Statistics.getPathStatistics()).counter + ' / '
                                + $scope.geneticSettings.numberOfPaths + ')...');
                    }
                });
                window.clearInterval(interval);
            }, 1000);
        };
        $scope.evolve = function () {
            Statistics.resetGeneticStatistics();
            Statistics.markEvolutionStart();
            $scope.notifyInfo('Evolving...');

            var interval = window.setInterval(function () {
                $scope.geneticEvolution = $socket.evolve($scope.geneticSettings);
                $scope.geneticEvolution.then(function (response) {

                    if (response.status === 200) {
                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                        $rootScope.$broadcast('geneticDataLoaded', response.data);
                        $scope.compare();
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    Statistics.addGeneticStatistic(update.data);
                    $rootScope.$broadcast('geneticDataUpdated', update.data);
                    $scope.notifyInfo('Evolving (' + update.data.evolutionStage + ' / ' + $scope.geneticSettings.numberOfEvolutions + ')...');
                });
                window.clearInterval(interval);
            }, 1000);
        };
        $scope.compare = function () {
            Statistics.resetCompareStatistics();
            Statistics.markCompareStart();
            $scope.notifyInfo('Comparing Results...');

            var interval = window.setInterval(function () {
                $scope.geneticResultsCompare = $socket.compare($scope.geneticSettings);
                $scope.geneticResultsCompare.then(function (response) {

                    if (response.status === 200) {
                        $rootScope.$broadcast('compareDataLoaded', {});
                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                    } else {
                        $scope.notifyError(response.description, $('#modalLoadingError'));
                    }
                }, function (error) {
                    $scope.notifyError(error.description, $('#modalLoadingError'));
                }, function (update) {
                    Statistics.addCompareStatistic(update.data);
                    $rootScope.$broadcast('compareDataUpdated', update.data);
                });
                window.clearInterval(interval);
            }, 1000);
        };

        /*
         * =====================================================================
         * View and statistics functions
         * =====================================================================
         */
        $scope.initGeneticSettingsAdvanced = function () {
            var $keepPopSizeConstantToggle = $('#keepPopSizeConstant').bootstrapToggle({
                on: 'Yes',
                off: 'No'
            });
            $('#keepPopSizeConstant').prop('checked', $scope.geneticSettings.keepPopSizeConstant).change();
            $keepPopSizeConstantToggle.change(function () {
                $scope.geneticSettings.keepPopSizeConstant = $('#keepPopSizeConstant').prop('checked');
            });

            var $alwaysCalculateFitnessToggle = $('#alwaysCalculateFitness').bootstrapToggle({
                on: 'Yes',
                off: 'No'
            });
            $('#alwaysCalculateFitness').prop('checked', $scope.geneticSettings.alwaysCalculateFitness).change();
            $alwaysCalculateFitnessToggle.change(function () {
                $scope.geneticSettings.alwaysCalculateFitness = $('#alwaysCalculateFitness').prop('checked');
            });

            var $preserveFittestIndividualToggle = $('#preserveFittestIndividual').bootstrapToggle({
                on: 'Yes',
                off: 'No'
            });
            $('#preserveFittestIndividual').prop('checked', $scope.geneticSettings.preserveFittestIndividual).change();
            $preserveFittestIndividualToggle.change(function () {
                $scope.geneticSettings.preserveFittestIndividual = $('#preserveFittestIndividual').prop('checked');
            });
        };
        $scope.previewGraph = function () {
            $scope.load.graphPreviewerLoaded = true;

            var interval = window.setInterval(function () {
                var graphPreviewWrapper = $('div#graph-viewer-vis-preview');

                if (graphPreviewWrapper && graphPreviewWrapper[0]) {
                    graphPreviewWrapper = graphPreviewWrapper[0];
                }

                if (graphPreviewWrapper && graphPreviewWrapper.firstChild) {
                    while (graphPreviewWrapper.firstChild) {
                        graphPreviewWrapper.removeChild(graphPreviewWrapper.firstChild);
                    }
                }

                try {
                    var parsedJson = JSON.parse($('#graphJson').val());
                    $scope.graphJson.graph = parsedJson;
                    $scope.uploadJsonValid = true;

                    $scope.initGraphPreview($scope.graphJson.graph);
                } catch (e) {
                    $scope.uploadJsonValid = false;
                }

                window.clearInterval(interval);
            }, 200);
        };
        $scope.checkGraph = function () {
            try {
                var parsedJson = JSON.parse($('#graphJson').val());
                $scope.graphJson.graph = parsedJson;
                $scope.uploadJsonValid = true;

                return true;
            } catch (e) {
                $scope.uploadJsonValid = false;

                return false;
            }

            return false;
        };

        $scope.initGraphPreview = function ($data) {
            var previewContainer = document.getElementById('graph-viewer-vis-preview');
            new vis.Network(previewContainer, $data, $scope.visPreviewOptions);
        };
        $scope.initGraphView = function ($data) {
            $rootScope.$broadcast('resetViews', {});
            $scope.load.graphViewerLoaded = true;

            var interval = window.setInterval(function () {
                var container = document.getElementById('graph-viewer-vis-canvas');
                var network = new vis.Network(container, $data.graph, $scope.visOptions);

                /*
                 * If number of nodes / edges is large enough, disable stabilization after a while in order to speed graph loading
                 */
                var stabilizationInterval = window.setInterval(function () {
                    network.freezeSimulation(true);
                    network.setOptions({freezeForStabilization: true});
                    network.storePositions();
                    console.log('Network animation disabled');
                    window.clearInterval(stabilizationInterval);
                }, $scope.stabilizationIntervalMs);

                window.graph = network;

                window.clearInterval(interval);
            }, 500);

            $scope.nodeIds = [];
            console.log($data.graph.nodes);
            angular.forEach($data.graph.nodes, function (graphNode, key) {
                if (graphNode && graphNode.id) {
                    $scope.nodeIds.push(graphNode.id);
                }
            });

            if ($scope.nodeIds.length && $scope.nodeIds.length > 0) {
                $scope.geneticSettings.sourceNode = $scope.nodeIds[0];
                $scope.geneticSettings.destinationNode = $scope.nodeIds[$scope.nodeIds.length - 1];
            }

            var $graphViewerToggle = $('#graph-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
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
            var graphWrapper = $("#graph-viewer-wrapper");

            if (graphWrapper && graphWrapper[0]) {
                graphWrapper = graphWrapper[0];
            }

            if (graphWrapper && graphWrapper.firstChild) {
                while (graphWrapper.firstChild) {
                    graphWrapper.removeChild(graphWrapper.firstChild);
                }
            }

            $scope.load.graphViewerLoaded = false;
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
            var graphStatistics = Statistics.getGraphStatistics();
            var averageCostLowThreshold = graphStatistics.averageEdgeCost - (graphStatistics.averageEdgeCost / 4);
            var averageCostHighThreshold = graphStatistics.averageEdgeCost + (graphStatistics.averageEdgeCost / 4);
            if (edgeCost < averageCostLowThreshold) {
                return 'label-success';
            } else if (edgeCost >= averageCostLowThreshold && edgeCost <= averageCostHighThreshold) {
                return 'label-warning';
            } else {
                return 'label-danger';
            }
        };
        $scope.notifyInfo = function (notification) {
            $scope.load.wip = true;
            $scope.load.wipType = notification;
        };
        $scope.notifyError = function (notification, $modalElement) {
            $scope.load.wip = false;
            $scope.errorText = notification;
            $modalElement.modal('show');
        };
        // Due to problems with webkit transition detection, clear notifications manually
        $scope.clearNotifs = function (delay) {
            var interval = window.setInterval(function () {
                var notifys = angular.element($('.killed'));
                if (notifys) {
                    angular.forEach(notifys, function (notify) {
                        notify.remove();
                    });
                }
            }, delay);
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
        // Keep a copy of the graph in JSON format
        $scope.graphJson = {
            graph: {
                nodes: [
                    {id: 0, name: 'Node 0', label: 'Node 0'},
                    {id: 1, name: 'Node 1', label: 'Node 1'},
                    {id: 2, name: 'Node 2', label: 'Node 2'},
                    {id: 3, name: 'Node 3', label: 'Node 3'},
                    {id: 4, name: 'Node 4', label: 'Node 4'}
                ],
                edges: [
                    {from: 0, to: 1, cost: 1, weight: 1, label: 1},
                    {from: 1, to: 2, cost: 2, weight: 2, label: 2},
                    {from: 2, to: 3, cost: 3, weight: 3, label: 3},
                    {from: 3, to: 4, cost: 4, weight: 4, label: 4},
                    {from: 0, to: 2, cost: 2, weight: 2, label: 2},
                    {from: 3, to: 4, cost: 4, weight: 4, label: 4}
                ]
            }};
        $scope.graphJsonString = JSON.stringify($scope.graphJson.graph, null, 2);
        $scope.uploadJsonValid = true;
        $scope.nodeIds = [];

        // Keep track of what's been loaded
        $scope.load = {
            wip: false,
            wipType: '',
            graphViewerLoaded: false,
            graphPreviewerLoaded: false,
            graphDisplayed: true
        };
        // Settings for graph generation
        $scope.graphSettings = {
            numberOfNodes: 30,
            numberOfNodesMax: 1000,
            numberOfEdges: 100,
            numberOfEdgesMax: 1000,
            minimumEdgeWeight: 1,
            maximumEdgeWeight: 100,
            isStatic: true
        };
        $scope.stabilizationIntervalMs = 20000;
        $scope.visOptions = {
            width: '100%',
            height: '400px',
            zoomable: true,
            navigation: true,
            keyboard: false,
            stabilize: false,
            stabilizationIterations: 20,
            edges: {
                style: 'arrow',
                fontSize: 25,
                color: {
                    color: '#428bca',
                    highlight: '#00CE6F'
                },
                width: 2,
                widthSelectionMultiplier: 5
            },
            nodes: {
                shape: 'rect',
                radius: 35,
                radiusMin: 35,
                radiusMax: 50,
                borderWidth: 2,
                borderWidthSelected: 3,
                fontColor: 'white',
                fontSize: 25,
                color: {
                    background: '#428bca',
                    border: '#357ebd',
                    highlight: {
                        background: '#00CE6F',
                        border: '#428bca'
                    }
                },
                scaleFontWithValue: true
            }
        };

        $scope.visPreviewOptions = {
            width: '100%',
            height: '200px',
            zoomable: false,
            navigation: false,
            keyboard: false,
            edges: {
                style: 'line',
                fontSize: 15,
                color: {
                    color: '#428bca',
                    highlight: '#00CE6F'
                },
                width: 2
            },
            nodes: {
                shape: 'rect',
                radius: 15,
                radiusMin: 20,
                radiusMax: 20,
                borderWidth: 1,
                borderWidthSelected: 2,
                fontColor: 'white',
                fontSize: 15,
                color: {
                    background: '#428bca',
                    border: '#357ebd',
                    highlight: {
                        background: '#00CE6F',
                        border: '#428bca'
                    }
                },
                scaleFontWithValue: false
            }
        };
        // Settings for genetic algorithm
        $scope.geneticSettings = {
            sourceNode: 0,
            destinationNode: 29,
            numberOfPaths: $scope.graphSettings.numberOfEdges,
            numberOfEvolutions: 10000,
            minPopSizePercent: 100,
            stopConditionPercent: 30,
            reportEveryXGenerations: 1000,
            alwaysCalculateFitness: true,
            keepPopSizeConstant: false,
            preserveFittestIndividual: true,
            comparePaths: 5,
            mutators: {
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
            }
        };
        // Websocket error messages
        $scope.errorText = '';
        // Notification duration
        $scope.notificationTime = 2000;
        // Charts

        // Model to JSON for demo purpose
        $scope.$watch('geneticSettings.mutators', function (model) {
            $scope.modelAsJson = angular.toJson(model, true);
        }, true);
    }]);