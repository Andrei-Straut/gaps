

gaps.factory('GraphStatistics', [function () {
        var Service = {};

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

        Service.getStatistics = function () {
            return _graphStatistics;
        };

        Service.setStatistics = function (graphStatistics) {
            _graphStatistics = graphStatistics;
        };
        
        Service.reset = function() {
            _graphStatistics.numberOfNodes = 1;
            _graphStatistics.numberOfEdges = 1;
            _graphStatistics.minimumEdgeCost = 1;
            _graphStatistics.maximumEdgeCost = 1;
            _graphStatistics.totalEdgeCost = 0;
            _graphStatistics.averageEdgeCost = 0.0;
            _graphStatistics.averageEdgesPerNode = 0.0;
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