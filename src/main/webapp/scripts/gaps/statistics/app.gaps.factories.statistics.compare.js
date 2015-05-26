/**
 * Statistics factory
 */
gaps.factory('CompareStatistics', ['GeneticStatistics', function (GeneticStatistics) {
        var Service = {};

        var _compareDataSet = new vis.DataSet();

        var _compareDataSetGroups = new vis.DataSet();
        _compareDataSetGroups.add({id: 0, content: "GAPS"});
        _compareDataSetGroups.add({id: 1, content: "JGraphT"});

        // JGraphT KShortestPath comparison
        var _compareStatistics = {
            startTimestamp: {},
            endTimestamp: {},
            compareDiffTimestamp: {},
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

        Service.getDataSet = function () {
            return _compareDataSet;
        };

        Service.getDataSetGroups = function () {
            return _compareDataSetGroups;
        };

        Service.add = function (compareStatistic) {
            if (compareStatistic) {
                _compareStatistics.chromosomes.push(compareStatistic);
                var xPoint = _compareDataSet.length / 2;
                var costs = GeneticStatistics.getCostsDataSet().distinct('y');
                var cost = GeneticStatistics.getStatistics().bestPathCost;

                if (costs.length > xPoint) {
                    cost = costs[costs.length - (xPoint + 1)];
                }

                var compareDataPointGaps = {
                    x: xPoint,
                    y: cost,
                    group: 0,
                    label: {
                        content: 'GAPS: ' + cost
                    }
                };
                var compareDataPointJGraphT = {
                    x: xPoint,
                    y: compareStatistic.cost,
                    group: 1,
                    label: {
                        content: 'JGraphT: ' + compareStatistic.cost
                    }
                };
                _compareDataSet.add(compareDataPointGaps);
                _compareDataSet.add(compareDataPointJGraphT);
            }
        };

        Service.reset = function () {
            _compareStatistics.startTimestamp = {};
            _compareStatistics.endTimestamp = {};
            _compareStatistics.compareDiffTimestamp = {};
            _compareStatistics.chromosomes = [];
            _compareDataSet.clear();
        };

        Service.markCompareStart = function () {
            _compareStatistics.startTimestamp = new Date();
        };

        Service.markCompareEnd = function () {
            _compareStatistics.endTimestamp = new Date();
            _compareStatistics.compareDiffTimestamp = _compareStatistics.endTimestamp - _compareStatistics.startTimestamp;
        };

        Service.getCompareStartTime = function () {
            return convertTime(_compareStatistics.startTimestamp);
        };

        Service.getCompareEndTime = function () {
            return convertTime(_compareStatistics.endTimestamp);
        };

        Service.getCompareDiffTime = function () {
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