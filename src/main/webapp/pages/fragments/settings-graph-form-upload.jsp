<div class="modal fade" 
     id="graphSettingsAdvancedModal" 
     tabindex="-1" role="dialog" 
     aria-labelledby="graphSettingsAdvancedModalTitle" 
     aria-hidden="true" 
     style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="graphSettingsAdvancedModalTitle">Upload graph</h4>
            </div>
            <div class="modal-body">
                <form name="graphsettings" novalidate>
                    <%@include file="settings-graph-form-upload-text.jsp" %>
                </form>
            </div>
            <div class="modal-footer">
                <button 
                    id="graphUploadButton" 
                    class="btn btn-primary" 
                    type="button" 
                    ng-disabled="!uploadJsonValid"
                    ng-click="uploadGraph();">Upload</button>
                <button id="graphUploadCancelButton" type="button" class="btn btn-warning" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>