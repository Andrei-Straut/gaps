
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
           max="{{graphSettings.numberOfNodes - 1}}"
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
           max="{{graphSettings.numberOfNodes - 1}}"
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
    <label class="control-label" for="stopConditionPercent">Stop Condition Percent (%)</label>
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