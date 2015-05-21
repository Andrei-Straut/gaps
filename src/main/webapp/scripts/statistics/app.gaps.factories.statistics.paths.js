

gaps.factory('PathStatistics', [function () {
        var Service = {};

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

        Service.getStatistics = function () {
            return _pathStatistics;
        };

        Service.setStatistics = function (pathStatistics) {
            _pathStatistics = pathStatistics;
        };
        
        Service.reset = function() {
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

        Service.add = function (pathStatistic) {
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

        Service.getPathCost = function (pathStatistic) {
            var totalCost = 0;
            angular.forEach(pathStatistic, function (edge) {
                if (edge && edge.cost) {
                    totalCost += parseInt(edge.cost);
                }
            });
            return totalCost;
        };
        
        Service.getEdgeClass = function (edgeCost) {
            var averageCostLowThreshold = _graphStatistics.averageEdgeCost - (_graphStatistics.averageEdgeCost / 4);
            var averageCostHighThreshold = _graphStatistics.averageEdgeCost + (_graphStatistics.averageEdgeCost / 4);
            if (edgeCost < averageCostLowThreshold) {
                return 'label-success';
            } else if (edgeCost >= averageCostLowThreshold && edgeCost <= averageCostHighThreshold) {
                return 'label-warning';
            } else {
                return 'label-danger';
            }
        };
        
        return Service;

    }]);