<div id="genetic-settings">
    <a name="genetic-settings"></a>
    <div class="col-md-6 col-sm-12 col-xs-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                Step 2. Evolve paths
                &nbsp;
                <button id="genetic-settings-advanced" 
                        name="geneticSettingsAdvanced"
                        type="button"
                        class="btn btn-primary btn-sm"
                        href="#"
                        data-toggle="modal"
                        data-target="#geneticSettingsAdvancedModal"
                        >
                    Advanced</button>
            </div>
            <div class="panel-body">
                <form name="geneticsettings" novalidate>
                    <div class="col-md-6">
                        <%@include file="settings-genetic-form.jsp" %>
                        
                        <button id="graph-viewer-start-evolution"
                                type="submit"
                                class="btn btn-primary btn-lg"
                                ng-disabled="!(load.graphStatisticsLoaded) || geneticsettings.$invalid || load.wip"
                                ng-click="computePaths();">Evolve!</button>
                    </div>
                </form>
            </div>

            <div class="modal fade" 
                 id="geneticSettingsAdvancedModal" 
                 tabindex="-1" role="dialog" 
                 aria-labelledby="geneticSettingsAdvancedModalTitle" 
                 aria-hidden="true" 
                 style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title" id="geneticSettingsAdvancedModalTitle">Advanced Settings</h4>
                        </div>
                        <form name="geneticsettingsadvanced" novalidate>
                            <div class="modal-body">
                                <%@include file="settings-genetic-form-advanced.jsp" %>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" data-dismiss="modal">Save Changes</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>