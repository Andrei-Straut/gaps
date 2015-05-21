

gaps.factory('GeneticStatistics', [function () {
        var Service = {};

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
            generations: [],
            costs: []
        };

        Service.getStatistics = function () {
            return _geneticStatistics;
        };

        Service.setStatistics = function (geneticStatistics) {
            _geneticStatistics = geneticStatistics;
        };

        Service.reset = function () {
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
            _geneticStatistics.costs = [];
        };

        Service.add = function (geneticStatistic) {
            if (geneticStatistic.bestChromosome) {
                if (JSON.stringify(_geneticStatistics.bestPath) === '{}'
                        || geneticStatistic.bestChromosome.cost < _geneticStatistics.bestPath.cost) {
                    _geneticStatistics.bestPath = geneticStatistic.bestChromosome;
                    _geneticStatistics.evolutionStage = geneticStatistic.evolutionStage;
                    _geneticStatistics.bestPathEdgeNumber = geneticStatistic.bestChromosome.path.length;
                    _geneticStatistics.bestPathCost = geneticStatistic.bestChromosome.cost;

                    if (_geneticStatistics.costs.length === 0 ||
                            _geneticStatistics.costs[_geneticStatistics.costs.length - 1] !== geneticStatistic.bestChromosome.cost) {
                        _geneticStatistics.costs.push(geneticStatistic.bestChromosome.cost);
                    }

                    _geneticStatistics.bestPathFitness = geneticStatistic.bestChromosome.fitness;
                }
            }

            _geneticStatistics.generations.push(geneticStatistic);

            var generationDataChart = {};
            generationDataChart.y = 'Gen ' + geneticStatistic.evolutionStage;
            generationDataChart.a = geneticStatistic.endBestCost;

            _geneticStatistics.generationChart.push(generationDataChart);
        };

        Service.markEvolutionStart = function () {
            _geneticStatistics.startTimestamp = new Date();
        };

        Service.markEvolutionEnd = function () {
            _geneticStatistics.endTimestamp = new Date();
            _geneticStatistics.evolutionDiffTimestamp = _geneticStatistics.endTimestamp - _geneticStatistics.startTimestamp;
        };
        
        Service.getEvolutionStartTime = function() {
            return convertTime(_geneticStatistics.startTimestamp);
        };
        
        Service.getEvolutionEndTime = function() {
            return convertTime(_geneticStatistics.endTimestamp);
        };
        
        Service.getEvolutionDiffTime = function() {
            return convertTime( _geneticStatistics.endTimestamp - _geneticStatistics.startTimestamp);
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

