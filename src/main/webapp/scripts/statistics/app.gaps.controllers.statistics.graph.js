

gaps.controller('graphstatisticscontroller', ['$rootScope', '$scope', 'Notification', 'GraphStatistics',
    function ($rootScope, $scope, Notification, GraphStatistics) {

        $scope.statisticsLoaded = false;
        $scope.statisticsDisplayed = true;
        $scope.dataToggleId = '#graph-statistics-viewer-toggle';
        $scope.dataTableId = '#graph-statistics-direct-edges';
        $scope.statisticsInfoCardValue = [];

        $scope.getStatistics = function () {
            return GraphStatistics.getStatistics();
        };

        $rootScope.$on('resetViews', function (event, $data) {
            $scope.resetData();
            $scope.resetView();
        });

        $rootScope.$on('graphDataLoaded', function (event, $data) {
            GraphStatistics.setStatistics($data.statistics);
            
            $scope.statisticsLoaded = true;
            $scope.statisticsDisplayed = true;

            $scope.initView($data);
        });

        $scope.resetData = function () {
            GraphStatistics.reset();
        };

        $scope.resetView = function () {
            var table = $($scope.dataTableId).DataTable();
            table.clear();
            $scope.statisticsLoaded = false;
            $scope.statisticsDisplayed = false;
        };

        $scope.initView = function ($data) {
            var table = $($scope.dataTableId).DataTable();
            angular.forEach($data.graph.edges, function (edgeValue, edgeKey) {
                table.row.add([edgeValue.id, edgeValue.from, edgeValue.to, edgeValue.label]);
            });
            table.draw();
            
            $scope.statisticsInfoCardValue = $scope.buildInfoCardValue(GraphStatistics.getStatistics());

            var $graphStatisticsToggle = $($scope.dataToggleId).bootstrapToggle({
                on: 'Visible',
                off: 'Hidden'
            });
            $graphStatisticsToggle.change(function () {
                $scope.hideView();
            });
            Notification.success({message: 'Loaded', delay: 2000});
        };
        
        $scope.buildInfoCardValue = function($data) {
            var value = [];
            value.push({title: "Number of edges", value: $data.numberOfEdges});
            value.push({title: "Total edge cost", value: $data.totalEdgeCost});
            value.push({title: "Most expensive edge", value: $data.maximumEdgeCost});
            value.push({title: "Cheapest edge", value: $data.minimumEdgeCost});
            value.push({title: "Average edge cost", value: $data.averageEdgeCost});
            value.push({title: "Average node connectivity", value: $data.averageEdgesPerNode});
            
           return value;
        };

        $scope.hideView = function () {
            $scope.statisticsDisplayed = $($scope.dataToggleId).prop('checked');
            $scope.$apply();
        };
    }]);