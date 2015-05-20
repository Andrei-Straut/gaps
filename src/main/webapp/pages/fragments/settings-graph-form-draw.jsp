
<div class="modal fade" 
     id="graphSettingsDrawModal" 
     tabindex="-1" role="dialog" 
     aria-labelledby="graphSettingsDrawModalTitle" 
     aria-hidden="true" 
     style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="graphSettingsDrawModalTitle">Draw graph</h4>
            </div>
            <div class="modal-body">
                <div id="network-popUp">
                    <span id="operation">node</span> <br />
                    <table style="margin:auto;">
                        <tr>
                            <td>Data</td>
                            <td><input id="graph-element-label" value="new value"> </td>
                        </tr>
                    </table>
                    <span>*Data: label (string) for nodes, integer value for edges</span> <br/>
                    <input type="button" value="save" id="saveButton"/>
                    <input type="button" value="cancel" id="cancelButton"/>
                </div>
                <div id="graph-viewer-vis-canvas-draw"></div>
                <p id="selection"></p>
            </div>
            <div class="modal-footer">
                <button 
                    id="graphUploadButton" 
                    class="btn btn-primary" 
                    type="button" 
                    ng-click="uploadDrawGraph();">Upload</button>
                <button id="graphDrawCancelButton" type="button" class="btn btn-warning" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>