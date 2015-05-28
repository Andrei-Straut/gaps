
<div class="row">
    <div class="col-md-6 col-sm-12 col-xs-12">
        <%@include file="settings-genetic-form.jsp" %>
    </div>

    <div class="col-md-6 col-sm-12 col-xs-12">
        <div class="form-group"
             ng-class="{'has-error':
                         geneticsettings.reportGenerations.$error.required
                         || !geneticsettings.reportGenerations.$valid}">
            <label class="control-label" for="reportGenerations">Report Every X Generations</label>
            <input required
                   id="reportGenerations" 
                   name="reportGenerations"
                   class="form-control" 
                   data-toggle="tooltip" 
                   data-original-title="Report state once every X generations. When a change in state happens, report will be sent regardless of this setting"
                   type="number"
                   min="1"
                   max="{{geneticSettings.numberOfEvolutions}}"
                   ng-model="geneticSettings.reportEveryXGenerations" 
                   ng-disabled="!load.graphViewerLoaded"
                   disabled>
            <label class="control-label" 
                   for="reportGenerations" 
                   ng-show="!geneticsettings.reportGenerations.$valid">
                Report setting must be between 1 (report ALL generations) and maximum number of evolutions
            </label>
        </div>
        
        <div class="form-group"
             ng-class="{'has-error':
                         geneticsettings.minPopSizePercent.$error.required
                         || !geneticsettings.minPopSizePercent.$valid}">
            <label class="control-label" for="minPopSizePercent">Minimum Population Size (%)</label>
            <input required
                   id="minPopSizePercent" 
                   name="minPopSizePercent"
                   class="form-control" 
                   data-toggle="tooltip" 
                   data-original-title="Minimum population size, with regard to initial population"
                   type="number"
                   min="0"
                   max="100"
                   ng-model="geneticSettings.minPopSizePercent" 
                   ng-disabled="!load.graphViewerLoaded"
                   disabled>
            <label class="control-label" 
                   for="minPopSizePercent" 
                   ng-show="!geneticsettings.minPopSizePercent.$valid">
                Percent must be between 0 and 100
            </label>
        </div>

        <div class="form-group">
            <label class="checkbox-inline">
                <input id="keepPopSizeConstant"
                       type="checkbox"
                       checked
                       data-toggle="toggle"
                       data-width="100"
                       data-on="Constant"
                       data-off="Variable"
                       data-size="small"
                       ng-disabled="!load.graphViewerLoaded">
            </label>
            <label class="control-label" for="keepPopSizeConstant"
                   data-toggle="tooltip" 
                   data-original-title="If selected, population will always have the same number of individuals">
                Population Size
            </label>
        </div>

        <div class="form-group">       
            <label class="checkbox-inline">
                <input id="alwaysCalculateFitness"
                       type="checkbox"
                       checked
                       data-toggle="toggle"
                       data-width="100"
                       data-on="Always"
                       data-off="As Needed"
                       data-size="small"
                       ng-disabled="!load.graphViewerLoaded">
            </label>
            <label class="control-label" for="alwaysCalculateFitness"
                   data-toggle="tooltip" 
                   data-original-title="If selected, fitness will always be calculated for chromosomes, even if they are not modified">
                Calculate Fitness
            </label>     
        </div>

        <div class="form-group">
            <label class="checkbox-inline">
                <input id="preserveFittestIndividual"
                       type="checkbox"
                       checked
                       data-toggle="toggle"
                       data-width="100"
                       data-on="Yes"
                       data-off="No"
                       data-size="small"
                       ng-disabled="!load.graphViewerLoaded">
            </label>
            <label class="control-label" for="preserveFittestIndividual"
                   data-toggle="tooltip" 
                   data-original-title="If selected, the fittest individual of a generation will always be preserved">
                Preserve Fittest
            </label>    
        </div>
    </div>
</div>