/**
 * Statistics factory
 */
gaps.factory('Statistics', ['$rootScope', function ($rootScope) {
        var Service = {};
        var _sourceNodeId = 0;
        var _destinationNodeId = 1;
        var _graphStatisticsLoaded = false;
        var _pathStatisticsLoaded = false;
        var _geneticStatisticsLoaded = false;
        var _compareStatisticsLoaded = false;

        // Graph statistics per edge
        var _graphStatistics = {
            numberOfNodes: 1,
            numberOfEdges: 1,
            minimumEdgeCost: 1,
            maximumEdgeCost: 1,
            totalEdgeCost: 0,
            averageEdgeCost: 0.0,
            averageEdgesPerNode: 0.0
        };

        // Graph statistics per path
        var _pathStatistics = {
            counter: 1,
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
        var _geneticStatistics = {
            counter: 1,
            evolutionStage: 1,
            selectedGenerationIndex: 0,
            selectedGeneration: {},
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
        var _compareStatistics = {
            startTimestamp: {},
            endTimestamp: {},
            compareDiffTimestamp: {},
            compareChart: [],
            chromosomes: []
        };
        
        Service.getGraphStatisticsLoaded = function() {
            return _graphStatisticsLoaded;
        };

        Service.setGraphStatisticsLoaded = function(statisticsLoaded) {
            _graphStatisticsLoaded = statisticsLoaded;
        };
        
        Service.getPathStatisticsLoaded = function() {
            return _pathStatisticsLoaded;
        };

        Service.setPathStatisticsLoaded = function(statisticsLoaded) {
            _pathStatisticsLoaded = statisticsLoaded;
        };
        
        Service.getGeneticStatisticsLoaded = function() {
            return _geneticStatisticsLoaded;
        };

        Service.setGeneticStatisticsLoaded = function(statisticsLoaded) {
            _geneticStatisticsLoaded = statisticsLoaded;
        };
        
        Service.getCompareStatisticsLoaded = function() {
            return _compareStatisticsLoaded;
        };

        Service.setCompareStatisticsLoaded = function(statisticsLoaded) {
            _compareStatisticsLoaded = statisticsLoaded;
        };

        Service.getSourceNodeId = function () {
            return _sourceNodeId;
        };

        Service.setSourceNodeId = function (sourceNodeId) {
            _sourceNodeId = sourceNodeId;
        };

        Service.getDestinationNodeId = function () {
            return _destinationNodeId;
        };

        Service.setDestinationNodeId = function (destinationNodeId) {
            _destinationNodeId = destinationNodeId;
        };

        Service.getGraphStatistics = function () {
            return _graphStatistics;
        };

        Service.setGraphStatistics = function (graphStatistics) {
            Service.resetGraphStatistics();
            _graphStatistics = graphStatistics;
        };

        Service.resetGraphStatistics = function () {
            _graphStatistics.numberOfNodes = 1;
            _graphStatistics.numberOfEdges = 1;
            _graphStatistics.minimumEdgeCost = 1;
            _graphStatistics.maximumEdgeCost = 1;
            _graphStatistics.totalEdgeCost = 1;
            _graphStatistics.averageEdgeCost = 1;
            _graphStatistics.averageEdgesPerNode = 1;
        };

        Service.getPathStatistics = function () {
            return _pathStatistics;
        };

        Service.setPathStatistics = function (pathStatistics) {
            _pathStatistics = pathStatistics;
        };

        Service.addPathStatistic = function (pathStatistic) {
            var numberOfEdges = pathStatistic.length;
            var totalCost = Service.getPathCost(pathStatistic);

            if (totalCost > _pathStatistics.mostExpensivePathCost) {
                _pathStatistics.mostExpensivePathCost = totalCost;
                _pathStatistics.mostExpensivePath = pathStatistic;
            }

            if (totalCost < _pathStatistics.cheapestPathCost) {
                _pathStatistics.cheapestPathCost = totalCost;
                _pathStatistics.cheapestPath = pathStatistic;
            }

            if (numberOfEdges > _pathStatistics.longestPathEdgeNumber) {
                _pathStatistics.longestPathEdgeNumber = numberOfEdges;
            }

            if (numberOfEdges < _pathStatistics.shortestPathEdgeNumber) {
                _pathStatistics.shortestPathEdgeNumber = numberOfEdges;
            }

            _pathStatistics.totalPathCost += totalCost;
            _pathStatistics.paths.push(pathStatistic);
            _pathStatistics.counter += 1;

            _pathStatistics.averagePathCost =
                    _pathStatistics.totalPathCost / _pathStatistics.paths.length;
        };

        Service.resetPathStatistics = function () {
            _pathStatistics.counter = 1;
            _pathStatistics.longestPathEdgeNumber = 10000000;
            _pathStatistics.shortestPathEdgeNumber = 0;
            _pathStatistics.totalPathCost = 0;
            _pathStatistics.cheapestPathCost = 10000000;
            _pathStatistics.mostExpensivePathCost = 0;
            _pathStatistics.averagePathCost = 1;
            _pathStatistics.paths = [];
            _pathStatistics.cheapestPath = [];
            _pathStatistics.mostExpensivePath = [];
        };

        Service.getPathCost = function (pathStatistic) {
            var totalCost = 0;
            angular.forEach(pathStatistic, function (edge) {
                if (edge.data && edge.data.cost) {
                    totalCost += parseInt(edge.data.cost);
                }
            });
            return totalCost;
        };

        Service.getGeneticStatistics = function () {
            return _geneticStatistics;
        };

        Service.setGeneticStatistics = function (geneticStatistics) {
            _geneticStatistics = geneticStatistics;
        };

        Service.addGeneticStatistic = function (geneticStatistic) {
            if (geneticStatistic.bestChromosome) {
                if (JSON.stringify(_geneticStatistics.bestPath) === '{}'
                        || geneticStatistic.bestChromosome.cost < _geneticStatistics.bestPath.cost) {
                    _geneticStatistics.bestPath = geneticStatistic.bestChromosome;
                    _geneticStatistics.evolutionStage = geneticStatistic.evolutionStage;
                    _geneticStatistics.bestPathEdgeNumber = geneticStatistic.bestChromosome.path.length;
                    _geneticStatistics.bestPathCost = geneticStatistic.bestChromosome.cost;
                    _geneticStatistics.bestPathFitness = geneticStatistic.bestChromosome.fitness;
                }
            }
            _geneticStatistics.generations.push(geneticStatistic);
            _geneticStatistics.bestPath = geneticStatistic.bestChromosome;

            var generationDataChart = {};
            generationDataChart.y = 'Gen ' + geneticStatistic.evolutionStage;
            generationDataChart.a = geneticStatistic.endBestCost;

            _geneticStatistics.generationChart.push(generationDataChart);
        };

        Service.resetGeneticStatistics = function () {
            _geneticStatistics.counter = 1;
            _geneticStatistics.evolutionStage = 1;
            _geneticStatistics.selectedGenerationIndex = 0;
            _geneticStatistics.selectedGeneration = {};
            _geneticStatistics.bestPathEdgeNumber = 0;
            _geneticStatistics.bestPathFitness = 10000000;
            _geneticStatistics.bestPathCost = 10000000;
            _geneticStatistics.startTimestamp = {};
            _geneticStatistics.endTimestamp = {};
            _geneticStatistics.evolutionDiffTimestamp = {};
            _geneticStatistics.generationChart = [];
            _geneticStatistics.generations = [];
            _geneticStatistics.bestPath = {};
        };

        Service.markEvolutionStart = function () {
            _geneticStatistics.startTimestamp = new Date();
        };

        Service.markEvolutionEnd = function () {
            _geneticStatistics.endTimestamp = new Date();
            _geneticStatistics.evolutionDiffTimestamp = _geneticStatistics.endTimestamp - _geneticStatistics.startTimestamp;
        };

        Service.getCompareStatistics = function () {
            return _compareStatistics;
        };

        Service.setCompareStatistics = function (compareStatistics) {
            _compareStatistics = compareStatistics;
        };

        Service.addCompareStatistic = function (compareStatistic) {
            if (compareStatistic) {
                _compareStatistics.chromosomes.push(compareStatistic);

                var resultsCompareChart = {};
                resultsCompareChart.y = 'GAPS Best, KShortest #' + _compareStatistics.compareChart.length;
                resultsCompareChart.GAPS = _geneticStatistics.bestPathCost;
                resultsCompareChart.KShortest = compareStatistic.cost;

                _compareStatistics.compareChart.push(resultsCompareChart);
            }
        };

        Service.resetCompareStatistics = function () {
            _compareStatistics.startTimestamp = {};
            _compareStatistics.endTimestamp = {};
            _compareStatistics.compareDiffTimestamp = {};
            _compareStatistics.compareChart = [];
            _compareStatistics.chromosomes = [];
        };

        Service.markCompareStart = function () {
            _compareStatistics.startTimestamp = new Date();
        };

        Service.markCompareEnd = function () {
            _compareStatistics.endTimestamp = new Date();
            _compareStatistics.compareDiffTimestamp = _compareStatistics.endTimestamp - _compareStatistics.startTimestamp;
        };
        
        Service.resetAllStatistics = function() {
            Service.resetGraphStatistics();
            Service.resetPathStatistics();
            Service.resetGeneticStatistics();
            Service.resetCompareStatistics();
        };

        return Service;
    }]);