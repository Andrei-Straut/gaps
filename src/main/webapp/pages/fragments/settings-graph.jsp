<div id="graph-settings">
    <a name="graph-settings"></a>
    <div class="col-md-6 col-sm-12 col-xs-12">

        <div class="panel panel-default">
            <div class="panel-heading">
                Step 1. Generate graph
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
                            ng-disabled="graphgenerationsettings.$invalid || load.wip" 
                            ng-click="processGraph();">Generate!</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
