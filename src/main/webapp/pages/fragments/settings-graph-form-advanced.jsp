
<div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
        <div class="form-group" 
             ng-class="{'has-error' : !uploadJsonValid}">
            <label class="control-label" for="graphJson">Paste your graph here:</label>
            <textarea 
                id="graphJson" 
                ng-model="graphJsonString"
                ng-change="checkGraph()"
                class="form-control" 
                rows="10"></textarea>
            <label class="control-label" for="graphJson" ng-show="!uploadJsonValid">Pasted graph is not valid JSON</label>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
        <div class="form-group">
            <button 
                id="graph-viewer-preview-graph" 
                class="btn btn-primary btn-sm"
                type="button" 
                ng-disabled="!uploadJsonValid"
                ng-click="previewGraph();">Validate and preview</button>
        </div>

        <div class="form-group">
            <div id="infovis-wrapper-preview" class="panel-body" ng-show="load.graphPreviewerLoaded">
                <div id="infovis-preview" class="infovis"></div>
            </div>
        </div>
    </div>
</div>