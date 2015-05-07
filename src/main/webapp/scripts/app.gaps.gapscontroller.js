/**
 * Main GAPS controller
 */
gaps.controller('gapscontroller', ['$scope', 'Socket', 'Notification', 'Statistics', function ($scope, $socket, Notification, Statistics) {
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
            Statistics.markEvolutionStart();

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
            Statistics.markCompareStart();
            
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
                    $scope.updateCompareStatistics(update.data, Statistics.getGeneticStatistics());
                });
                window.clearInterval(interval);
            }, 1000);
        }

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
            Statistics.setGraphStatistics($data.statistics);

            var table = $('#graph-direct-edges').DataTable();
            angular.forEach($data.edges, function (edgeValue, edgeKey) {
                table.row.add([edgeValue.data.id, edgeValue.nodeFrom, edgeValue.nodeTo, edgeValue.data.cost]);
            });
            table.draw();
            $scope.load.graphStatisticsLoaded = true;
            $scope.notifySuccess('Loaded');
            var $graphStatisticsToggle = $('#graph-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
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
            Statistics.resetGraphStatistics();
            var table = $('#graph-direct-edges').DataTable();
            table.clear();
            $scope.load.graphStatisticsLoaded = false;
        };
        $scope.initPathStatistics = function () {
            $scope.load.pathStatisticsLoaded = true;
            $scope.notifySuccess('Paths Computed');
            var $pathStatisticsToggle = $('#path-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
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
            Statistics.addPathStatistic(path);
            var pathStatistics = Statistics.getPathStatistics();

            var table = $('#graph-paths').DataTable();
            table.row.add([pathStatistics.counter, path.length, Statistics.getPathCost(path)]);

            table.draw();
            $scope.notifyInfo('Computing paths (' + pathStatistics.counter + ' / ' + $scope.geneticSettings.numberOfPaths + ')...');
        };
        $scope.resetPathStatisticsAndTable = function () {
            Statistics.resetPathStatistics();

            var table = $('#graph-paths').DataTable();
            table.clear();
            $scope.load.pathStatisticsLoaded = false;
        };
        $scope.initGeneticStatistics = function () {
            $scope.notifySuccess('Evolved');

            Statistics.markEvolutionEnd();

            $scope.notifyInfo('Computing Statistics...');
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
                $scope.load.geneticStatisticsLoaded = true;
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
            $scope.load.geneticStatisticsDisplayed = $('#genetic-statistics-viewer-toggle').prop('checked');
            $scope.$apply();
        };
        $scope.resetGeneticStatisticsAndView = function () {
            Statistics.resetGeneticStatistics();

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
            Statistics.addGeneticStatistic(data);
            $scope.notifyInfo('Evolving (' + data.evolutionStage + ' / ' + $scope.geneticSettings.numberOfEvolutions + ')...');
        };
        $scope.initCompareStatistics = function () {
            Statistics.markCompareEnd();
            
            var interval = window.setInterval(function () {
                var compareStatistics = Statistics.getCompareStatistics();
                $scope.compareChart.initialize('morris-bar-compare-chart', compareStatistics.compareChart);                
                $scope.load.compareStatisticsLoaded = true;
                window.clearInterval(interval);
            }, 1000);
            var $compareStatisticsToggle = $('#compare-statistics-viewer-toggle').bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
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
            Statistics.resetCompareStatistics();
            $scope.compareChart.clear('morris-bar-compare-chart');
            $scope.load.compareStatisticsLoaded = false;
        };
        $scope.updateCompareStatistics = function (data) {
            Statistics.addCompareStatistic(data);
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
        $scope.sliderEvent = function (event) {
            var geneticStatistics = Statistics.getGeneticStatistics();

            geneticStatistics.selectedGenerationIndex = event.value;
            if (geneticStatistics.generations[event.value]) {
                geneticStatistics.selectedGeneration =
                        geneticStatistics.generations[
                                geneticStatistics.selectedGenerationIndex];
            }

            if (window.fd && window.fd.graph && geneticStatistics && geneticStatistics.selectedGeneration
                    && geneticStatistics.selectedGeneration.bestChromosome) {
                var $path = geneticStatistics.selectedGeneration.bestChromosome.path;
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

            var geneticStatistics = Statistics.getGeneticStatistics();
            geneticStatistics.selectedGeneration = {};
            geneticStatistics.selectedGenerationIndex = 0;
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
        // Model to JSON for demo purpose
        $scope.$watch('geneticSettings.mutators', function (model) {
            $scope.modelAsJson = angular.toJson(model, true);
        }, true);
    }]);