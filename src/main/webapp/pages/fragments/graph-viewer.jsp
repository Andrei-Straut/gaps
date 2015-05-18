<div id="graph-viewer" ng-show="load.graphViewerLoaded">
    <div class="row">
        <a name="graph-viewer"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="graph-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                    </label>
                    Graph&nbsp;&nbsp;
                    (<span class="label label-default">Neutral Edge Cost</span>
                    <span class="label label-success">Low Edge Cost</span>
                    <span class="label label-warning">Medium Edge Cost</span>
                    <span class="label label-danger">High Edge Cost</span>)
                </div>
                <div id="graph-viewer-wrapper" class="panel-body" ng-show="load.graphDisplayed">
                    <div id="graph-viewer-vis-canvas"></div>
                </div>
            </div>
        </div>
    </div>

    <hr/>
</div>