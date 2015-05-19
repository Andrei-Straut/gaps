
<style type="text/css">
    table.legend_table {
        font-size: 11px;
        border-width:1px;
        border-color:#d3d3d3;
        border-style:solid;
    }
    table.legend_table,td {
        border-width:1px;
        border-color:#d3d3d3;
        border-style:solid;
        padding: 2px;
    }
    div.table_content {
        width:80px;
        text-align:center;
    }
    div.table_description {
        width:100px;
    }

    #operation {
        font-size:28px;
    }
    #network-popUp {
        display:none;
        position:absolute;
        top:350px;
        left:170px;
        z-index:299;
        width:250px;
        height:120px;
        background-color: #f9f9f9;
        border-style:solid;
        border-width:3px;
        border-color: #5394ed;
        padding:10px;
        text-align: center;
    }
</style>

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
                    <span id="operation">node</span> <br>
                    <table style="margin:auto;"><tr>
                            <td>id</td><td><input id="node-id" value="new value"></td>
                        </tr>
                        <tr>
                            <td>label</td><td><input id="node-label" value="new value"> </td>
                        </tr>
                    </table>
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
                    ng-click="drawGraph();">Upload</button>
                <button id="graphDrawCancelButton" type="button" class="btn btn-warning" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>