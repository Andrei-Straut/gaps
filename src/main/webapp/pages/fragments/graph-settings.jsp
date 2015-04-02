<div class="row">
    <div id="graph-settings">
        <a name="graph-settings"></a>
        <div class="col-md-6 col-sm-12 col-xs-12">

            <div class="panel panel-default">
                <div class="panel-heading">
                    Step 1. Generate graph: 
                </div>
                <div class="panel-body">
                    <form name="graphgenerationsettings" novalidate>
                        <div class="col-md-6">
                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             graphgenerationsettings.numberOfNodes.$error.required
                                             || !graphgenerationsettings.numberOfNodes.$valid}">
                                <label class="control-label" for="numberOfNodes">Number of Nodes</label>
                                <input required
                                       id="numberOfNodes" 
                                       name="numberOfNodes" 
                                       class="form-control"
                                       type="number"                                     
                                       ng-model="graphSettings.numberOfNodes" 
                                       min="1"
                                       max="{{graphSettings.numberOfNodesMax}}">
                                <label class="control-label" 
                                       for="numberOfNodes" 
                                       ng-show="graphgenerationsettings.numberOfNodes.$error.required">
                                    Number of nodes must always be specified
                                </label>
                                <label class="control-label" 
                                       for="numberOfNodes" 
                                       ng-show="!graphgenerationsettings.numberOfNodes.$valid">
                                    Number of nodes must be between 1 and 1000
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             graphgenerationsettings.numberOfEdges.$error.required
                                             || !graphgenerationsettings.numberOfEdges.$valid}">
                                <label class="control-label" for="numberOfEdges">Number of Edges</label>
                                <input required
                                       id="numberOfEdges" 
                                       name="numberOfEdges" 
                                       class="form-control"
                                       type="number"                                     
                                       ng-model="graphSettings.numberOfEdges" 
                                       min="{{graphSettings.numberOfNodes}}"
                                       max="{{graphSettings.numberOfEdgesMax}}">
                                <label class="control-label" 
                                       for="numberOfEdges" 
                                       ng-show="graphgenerationsettings.numberOfEdges.$error.required">
                                    Number of edges must always be specified
                                </label>
                                <label class="control-label" 
                                       for="numberOfEdges" 
                                       ng-show="!graphgenerationsettings.numberOfEdges.$valid">
                                    Number of edges must be between {{graphSettings.numberOfNodes}} and {{graphSettings.numberOfEdgesMax}}
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : !graphgenerationsettings.minimumEdgeWeight.$valid}">                                
                                <label class="control-label" for="minimumEdgeWeight">Minimum Edge Weight</label>
                                <input 
                                    id="minimumEdgeWeight" 
                                    name="minimumEdgeWeight" 
                                    class="form-control" 
                                    type="number"
                                    ng-model="graphSettings.minimumEdgeWeight" 
                                    min="1"
                                    max="{{graphSettings.maximumEdgeWeight}}"
                                    placeholder="1">
                                <label class="control-label" 
                                       for="minimumEdgeWeight" 
                                       ng-show="!graphgenerationsettings.minimumEdgeWeight.$valid">
                                    Minimum edge weight must be lower than or equal to minimum edge weight
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : !graphgenerationsettings.maximumEdgeWeight.$valid}">
                                <label class="control-label" for="maximumEdgeWeight">Maximum Edge Weight</label>
                                <input 
                                    id="maximumEdgeWeight" 
                                    name="maximumEdgeWeight" 
                                    class="form-control" 
                                    type="number"
                                    ng-model="graphSettings.maximumEdgeWeight" 
                                    min="{{graphSettings.minimumEdgeWeight}}"
                                    placeholder="100">
                                <label class="control-label" 
                                       for="maximumEdgeWeight" 
                                       ng-show="!graphgenerationsettings.maximumEdgeWeight.$valid">
                                    Maximum edge weight must be higher than or equal to minimum edge weight
                                </label>
                            </div>

                            <button 
                                id="graph-viewer-generate-graph" 
                                class="btn btn-primary btn-lg"
                                type="submit" 
                                ng-disabled="graphgenerationsettings.$invalid" 
                                ng-click="processGraph();">Generate!</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div id="genetic-settings">
        <a name="genetic-settings"></a>
        <div class="col-md-6 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Step 2. Evolve paths: 
                </div>
                <div class="panel-body">
                    <form name="geneticsettings" novalidate>
                        <div class="col-md-6">
                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             geneticsettings.sourceNode.$error.required
                                             || !geneticsettings.sourceNode.$valid}">
                                <label class="control-label" for="sourceNode">Source Node Id</label>
                                <input required
                                       id="sourceNode"
                                       name="sourceNode"
                                       class="form-control"
                                       type="number"
                                       ng-model="geneticSettings.sourceNode"
                                       ng-disabled="!(load.graphStatisticsLoaded)"
                                       min="0"
                                       max="{{graphSettings.numberOfNodes}}"
                                       disabled>
                                <label class="control-label" 
                                       for="sourceNode" 
                                       ng-show="geneticsettings.sourceNode.$error.required">
                                    Source Node ID must always be specified
                                </label>
                                <label class="control-label" 
                                       for="sourceNode" 
                                       ng-show="!geneticsettings.sourceNode.$valid">
                                    Source Node ID must be between 0 and number of nodes - 1
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             geneticsettings.destinationNode.$error.required
                                             || !geneticsettings.destinationNode.$valid}">
                                <label class="control-label" for="destinationNode">Destination Node Id</label>
                                <input required 
                                       id="destinationNode" 
                                       name="destinationNode"
                                       class="form-control"
                                       type="number"
                                       ng-model="geneticSettings.destinationNode"
                                       ng-disabled="!(load.graphStatisticsLoaded)"
                                       min="0"
                                       max="{{graphSettings.numberOfNodes}}"
                                       disabled>
                                <label class="control-label" 
                                       for="destinationNode" 
                                       ng-show="geneticsettings.destinationNode.$error.required">
                                    Destination Node ID must always be specified
                                </label>
                                <label class="control-label" 
                                       for="destinationNode" 
                                       ng-show="!geneticsettings.destinationNode.$valid">
                                    Destination Node ID must be between 0 and number of nodes - 1
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             geneticsettings.numberOfPaths.$error.required
                                             || !geneticsettings.numberOfPaths.$valid}">
                                <label class="control-label" for="numberOfPaths">Population size start</label>
                                <input required
                                    id="numberOfPaths" 
                                    name="numberOfPaths"
                                    class="form-control"
                                    type="number"
                                    ng-model="geneticSettings.numberOfPaths"
                                    ng-disabled="!(load.graphStatisticsLoaded)"
                                    min="1"
                                    disabled>
                                <label class="control-label" 
                                       for="numberOfPaths" 
                                       ng-show="!geneticsettings.numberOfPaths.$valid">
                                    There must be at least one individual for evolution to start
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             geneticsettings.numberOfEvolutions.$error.required
                                             || !geneticsettings.numberOfEvolutions.$valid}">
                                <label class="control-label" for="numberOfEvolutions">Number of Evolutions</label>
                                <input required
                                    id="numberOfEvolutions" 
                                    name="numberOfEvolutions"
                                    class="form-control" 
                                    type="number"
                                    ng-model="geneticSettings.numberOfEvolutions"
                                    ng-disabled="!(load.graphStatisticsLoaded)"
                                    min="100"
                                    disabled>
                                <label class="control-label" 
                                       for="numberOfEvolutions" 
                                       ng-show="!geneticsettings.numberOfEvolutions.$valid">
                                    Number of generations must be high enough to ensure genetic evolution
                                </label>
                            </div>

                            <div class="form-group"
                                 ng-class="{'has-error' : 
                                             geneticsettings.stopConditionPercent.$error.required
                                             || !geneticsettings.stopConditionPercent.$valid}">
                                <label class="control-label" for="stopConditionPercent">Stop condition percent (%)</label>
                                <input required
                                    id="stopConditionPercent" 
                                    name="stopConditionPercent"
                                    class="form-control" 
                                    type="number"
                                    ng-model="geneticSettings.stopConditionPercent" 
                                    ng-disabled="!(load.graphStatisticsLoaded)"
                                    min="0"
                                    max="100"
                                    disabled>
                                <label class="control-label" 
                                       for="stopConditionPercent" 
                                       ng-show="!geneticsettings.stopConditionPercent.$valid">
                                    Percent must be between 0 and 100
                                </label>
                            </div>

                            <button id="graph-viewer-start-evolution"
                                    type="submit"
                                    class="btn btn-primary btn-lg"
                                    ng-disabled="!(load.graphStatisticsLoaded) || graphgenerationsettings.$invalid"
                                    ng-click="computePaths();">Evolve!</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<hr/>