/**
 * Statistics factory
 */
gaps.factory('CompareStatistics', ['GeneticStatistics', function (GeneticStatistics) {
        var Service = {};

        // JGraphT KShortestPath comparison
        var _compareStatistics = {
            startTimestamp: {},
            endTimestamp: {},
            compareDiffTimestamp: {},
            compareChart: [],
            chromosomes: []
        };

        Service.getStatistics = function () {
            return _compareStatistics;
        };

        Service.getGeneticStatistics = function () {
            return GeneticStatistics;
        };

        Service.setStatistics = function (compareStatistics) {
            _compareStatistics = compareStatistics;
        };

        Service.add = function (compareStatistic) {
            if (compareStatistic) {
                _compareStatistics.chromosomes.push(compareStatistic);
                var compChartLength = _compareStatistics.compareChart.length;
                var genCostChartLength = GeneticStatistics.getStatistics().costs.length;
                var cost = GeneticStatistics.getStatistics().bestPathCost;

                var resultsCompareChart = {};
                resultsCompareChart.y = 'GAPS #' +
                        compChartLength +
                        ', KShortest #' +
                        compChartLength;

                if (genCostChartLength > compChartLength + 1) {
                    cost = GeneticStatistics.getStatistics().costs[genCostChartLength - (compChartLength + 1)];
                }

                resultsCompareChart.GAPS = cost;
                resultsCompareChart.KShortest = compareStatistic.cost;

                _compareStatistics.compareChart.push(resultsCompareChart);
            }
        };

        Service.reset = function () {
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
        
        Service.getCompareStartTime = function() {
            return convertTime(_compareStatistics.startTimestamp);
        };
        
        Service.getCompareEndTime = function() {
            return convertTime(_compareStatistics.endTimestamp);
        };
        
        Service.getCompareDiffTime = function() {
            return convertTime(_compareStatistics.endTimestamp - _compareStatistics.startTimestamp);
        };
        
        var convertTime = function (timestamp) {
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

        return Service;
    }]);