

gaps.factory('GeneticStatistics', [function () {
        var Service = {};
        var _evolutionDataSet = new vis.DataSet();
        var _costsDataSet = new vis.DataSet();

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
            generations: []
        };

        Service.getStatistics = function () {
            return _geneticStatistics;
        };

        Service.setStatistics = function (geneticStatistics) {
            _geneticStatistics = geneticStatistics;
        };
        
        Service.getGeneration = function(generationIndex){
            if(_geneticStatistics.generations && _geneticStatistics.generations.length > generationIndex) {
                return _geneticStatistics.generations[generationIndex];
            }
            
            return null;
        };
        
        Service.getGenerations = function() {
            if(_geneticStatistics.generations) {
                return _geneticStatistics.generations;
            }
            
            return null;
        };

        Service.getSelectedGeneration = function () {
            return _geneticStatistics.selectedGeneration;
        };

        Service.setSelectedGeneration = function (selectedGeneration) {
            _geneticStatistics.selectedGeneration = selectedGeneration;
        };

        Service.getSelectedGenerationIndex = function () {
            return _geneticStatistics.selectedGenerationIndex;
        };

        Service.setSelectedGenerationIndex = function (selectedGenerationIndex) {
            _geneticStatistics.selectedGenerationIndex = selectedGenerationIndex;
        };

        Service.getEvolutionDataSet = function () {
            return _evolutionDataSet;
        };

        Service.getCostsDataSet = function () {
            return _costsDataSet;
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
            _geneticStatistics.generations = [];
            _geneticStatistics.bestPath = {};
            _evolutionDataSet.clear();
            _costsDataSet.clear();
        };

        Service.add = function (geneticStatistic) {
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

            var evolutionDataPoint = {
                id: (geneticStatistic.evolutionStage + geneticStatistic.timeStamp),
                start: geneticStatistic.timeStampRaw,
                content: getDataPointContent(geneticStatistic),
                path: geneticStatistic.bestChromosome.path
            };
            _evolutionDataSet.add(evolutionDataPoint);
            
            var costDataPoint = {
                x: (geneticStatistic.evolutionStage),
                y: _geneticStatistics.bestPath.cost,
                label: {
                    content: _geneticStatistics.bestPath.cost
                }
            };
            _costsDataSet.add(costDataPoint);
            
            _geneticStatistics.generations.push(geneticStatistic);
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
        
        var getDataPointContent = function(geneticStatistic) {
            var content = '';
            content = content + '<b>' + geneticStatistic.evolutionStage + '</b><br/>';
            content = content + 'Cost: ' + geneticStatistic.endBestCost/* + '<br/>';
            content = content + 'Average cost: ' + geneticStatistic.endAverageCost + '<br/>';
            content = content + 'Time: ' + geneticStatistic.timeStamp*/;
            
            return content;
        };

        return Service;

    }]);

