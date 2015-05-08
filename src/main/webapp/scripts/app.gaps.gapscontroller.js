/**
 * Main GAPS controller
 */
gaps.controller('gapscontroller', ['$rootScope', '$scope', 'Socket', 'Notification', 'Statistics',
    function ($rootScope, $scope, $socket, Notification, Statistics) {

        $scope.getStatistics = function () {
            return Statistics;
        };

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
                        $scope.initGraphView(response.data.graph);
                        Statistics.setGraphStatistics(response.data.statistics);

                        $scope.load.wip = false;
                        $scope.load.wipType = '';
                        $rootScope.$broadcast('graphDataLoaded', response.data);
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
        $scope.initGraphView = function ($data) {
            $rootScope.$broadcast('resetViews', {});
            $scope.resetGraphView();
            jitInit($data);
            $(window).scrollTop($(window).scrollTop() + 1);
            $(window).scrollTop($(window).scrollTop() - 1);
            $scope.load.graphViewerLoaded = true;
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
            var graphWrapper = $("#infovis");
            if (graphWrapper && graphWrapper.firstChild) {
                while (graphWrapper.firstChild) {
                    graphWrapper.removeChild(graphWrapper.firstChild);
                }
            }
            var logWrapper = $("#log");
            if (logWrapper && logWrapper.firstChild) {
                while (logWrapper.firstChild) {
                    logWrapper.removeChild(logWrapper.firstChild);
                }
            }
            var labelWrapper = $("#inner-details");
            if (labelWrapper && labelWrapper.firstChild) {
                while (labelWrapper.firstChild) {
                    labelWrapper.removeChild(labelWrapper.firstChild);
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
        // Keep track of what's been loaded
        $scope.load = {
            wip: false,
            wipType: '',
            graphViewerLoaded: false,
            graphDisplayed: true,
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
            minPopSizePercent: 100,
            stopConditionPercent: 100,
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