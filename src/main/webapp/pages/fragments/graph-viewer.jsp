<div id="graph-viewer" ng-show="load.dataLoaded">
    <div class="row">
        <a name="graph-viewer"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="graph-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Hide" data-off="Show">
                    </label>
                    Graph
                </div>
                <div id="infovis-wrapper" class="panel-body" ng-show="load.graphDisplayed">
                    <div id="log" class="log"></div>

                    <div id="infovis" class="infovis"></div>

                    <div id="inner-details" class="inner-details"></div>
                </div>
            </div>
        </div>
    </div>

    <hr/>
</div>