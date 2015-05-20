/**
 * Graph factory
 */
gaps.factory('Graph', [function () {
        var Service = {};
        var _network;

        Service.getContainer = function () {
            return _container;
        };

        Service.setContainer = function (container) {
            _container = container;
        };

        Service.setContainerById = function (containerElementId) {
            var container = document.getElementById(containerElementId);
            _container = container;
        };

        Service.getOptions = function () {
            return _options;
        };

        Service.setOptions = function (options) {
            _options = options;
        };

        Service.getGraphData = function () {
            return _graphData;
        };

        Service.setGraphData = function (graphData) {
            _graphData = graphData;
        };

        Service.init = function (container, graphData, options) {
            var network = new vis.Network(container, graphData, options);

            return network;
        };

        Service.getNetwork = function () {
            return _network;
        };

        Service.setNetwork = function (network) {
            _network = network;
        };

        Service.getPreviewOptions = function () {
            return _previewOptions;
        };

        Service.getDefaultOptions = function () {
            return _defaultOptions;
        };

        Service.getDrawOptions = function () {
            return _drawOptions;
        };

        Service.getDefaultGraphData = function () {
            return _defaultGraphData;
        };

        Service.getDrawGraphData = function () {
            return _drawGraphData;
        };

        Service.setDrawGraphData = function (drawGraphData) {
            _drawGraphData = drawGraphData;
        };

        var _defaultOptions = {
            clickToUse: true,
            width: '100%',
            height: '400px',
            zoomable: true,
            navigation: true,
            keyboard: false,
            stabilizationIterations: 20,
            physics: {
                barnesHut: {
                    enabled: true,
                    springLength: 250
                }
            },
            smoothCurves: {
                dynamic: false,
                type: 'continuous',
                roundness: 0
            },
            edges: {
                style: 'arrow',
                fontSize: 25,
                color: {
                    color: '#428bca',
                    highlight: '#00CE6F'
                },
                width: 2,
                widthSelectionMultiplier: 4
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

        var _previewOptions = {
            clickToUse: true,
            width: '100%',
            height: '200px',
            zoomable: false,
            navigation: false,
            keyboard: false,
            smoothCurves: {
                dynamic: false,
                type: 'continuous',
                roundness: 0
            },
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

        var _drawOptions = {
            width: '100%',
            height: '400px',
            zoomable: true,
            navigation: false,
            keyboard: false,
            smoothCurves: {
                dynamic: false,
                type: 'continuous',
                roundness: 0
            },
            physics: {
                enabled: false
            },
            dataManipulation: true,
            edges: {
                style: 'arrow',
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
            },
            onAdd: function (data, callback) {
                data.label = 'Node 1';
                var span = document.getElementById('operation');
                var labelInput = document.getElementById('graph-element-label');
                var saveButton = document.getElementById('saveButton');
                var cancelButton = document.getElementById('cancelButton');
                var div = document.getElementById('network-popUp');
                span.innerHTML = "Add Node";
                labelInput.value = data.label;
                saveButton.onclick = saveData.bind(this, data, callback);
                cancelButton.onclick = clearPopUp.bind();
                div.style.display = 'block';
            },
            onEdit: function (data, callback) {
                var span = document.getElementById('operation');
                var labelInput = document.getElementById('graph-element-label');
                var saveButton = document.getElementById('saveButton');
                var cancelButton = document.getElementById('cancelButton');
                var div = document.getElementById('network-popUp');
                span.innerHTML = "Edit Node";
                labelInput.value = data.label;
                saveButton.onclick = saveData.bind(this, data, callback);
                cancelButton.onclick = clearPopUp.bind();
                div.style.display = 'block';
            },
            onConnect: function (data, callback) {
                data.label = '1';
                var span = document.getElementById('operation');
                var labelInput = document.getElementById('graph-element-label');
                var saveButton = document.getElementById('saveButton');
                var cancelButton = document.getElementById('cancelButton');
                var div = document.getElementById('network-popUp');
                span.innerHTML = "Add Edge";
                labelInput.value = data.label;
                saveButton.onclick = saveData.bind(this, data, callback);
                cancelButton.onclick = clearPopUp.bind();
                div.style.display = 'block';
            }
        };

        var _defaultGraphData = {
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
        };

        var _drawGraphData = {
            nodes: [],
            edges: []
        };

        var clearPopUp = function () {
            var saveButton = document.getElementById('saveButton');
            var cancelButton = document.getElementById('cancelButton');
            saveButton.onclick = null;
            cancelButton.onclick = null;
            var div = document.getElementById('network-popUp');
            div.style.display = 'none';
        };

        var saveData = function (data, callback) {
            var idInput = document.getElementById('node-id');
            var labelInput = document.getElementById('graph-element-label');
            var div = document.getElementById('network-popUp');
            data.label = labelInput.value;

            _drawGraphData.nodes = _drawGraphData.nodes.filter(function (node) {
                return node.id !== data.id;
            });

            _drawGraphData.edges = _drawGraphData.edges.filter(function (edge) {
                return edge.id !== data.id;
            });

            if (data.from && data.to) {
                var edge = {
                    id: data.id,
                    from: data.from,
                    to: data.to,
                    cost: parseInt(data.label),
                    weight: parseInt(data.label)
                };

                _drawGraphData.edges.push(edge);
            } else {
                var node = {
                    id: data.id,
                    name: data.label,
                    label: data.label
                };
                _drawGraphData.nodes.push(node);
            }

            clearPopUp();
            callback(data);
        };

        return Service;
    }]);

