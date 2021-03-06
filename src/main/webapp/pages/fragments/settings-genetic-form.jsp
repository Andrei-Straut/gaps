
<div class="form-group"
     ng-class="{'has-error' : 
                 geneticsettings.sourceNode.$error.required
                 || geneticsettings.sourceNode.$error.nodeidvalid
                 || geneticsettings.sourceNode.$error.nodeiddifferent}">
    <label class="control-label" for="sourceNode">Source Node Id</label>
    <input required
           type="text"
           id="sourceNode"
           name="sourceNode"
           class="form-control"
           data-toggle="tooltip" 
           data-placement="bottom" 
           data-original-title="Start point of path search"
           ng-model="geneticSettings.sourceNode"
           ng-disabled="!load.graphViewerLoaded"
           typeahead="id for id in nodeIds | filter: $viewValue | limitTo:8"
           node-id-valid
           node-id-different
           disabled>
    <label class="control-label" 
           for="sourceNode" 
           ng-show="geneticsettings.sourceNode.$error.required">
        Source Node ID must always be specified
    </label>
    <label class="control-label" 
           for="sourceNode" 
           ng-show="geneticsettings.sourceNode.$error.nodeidvalid">
        Source Node ID not found in graph
    </label>
    <label class="control-label" 
           for="sourceNode" 
           ng-show="geneticsettings.sourceNode.$error.nodeiddifferent">
        Source and destination nodes must be different
    </label>
</div>

<div class="form-group"
     ng-class="{'has-error' : 
             (geneticsettings.destinationNode.$error.required
                 || geneticsettings.destinationNode.$error.nodeidvalid
                 || geneticsettings.destinationNode.$error.nodeiddifferent)}">
    <label class="control-label" for="destinationNode">Destination Node Id</label>
    <input required 
           id="destinationNode" 
           name="destinationNode"
           class="form-control"
           data-toggle="tooltip" 
           data-original-title="End point of path search"
           ng-model="geneticSettings.destinationNode"
           ng-disabled="!load.graphViewerLoaded"
           typeahead="id for id in nodeIds | filter: $viewValue | limitTo:8"
           node-id-valid
           node-id-different
           disabled>
    <label class="control-label" 
           for="destinationNode" 
           ng-show="geneticsettings.destinationNode.$error.required">
        Destination Node ID must always be specified
    </label>
    <label class="control-label" 
           for="destinationNode" 
           ng-show="geneticsettings.destinationNode.$error.nodeidvalid">
        Destination Node ID not found in graph
    </label>
    <label class="control-label" 
           for="sourceNode" 
           ng-show="geneticsettings.destinationNode.$error.nodeiddifferent">
        Source and destination nodes must be different
    </label>
</div>

<div class="form-group"
     ng-class="{'has-error' : 
         geneticsettings.numberOfPaths.$error.required
                 || !geneticsettings.numberOfPaths.$valid}">
    <label class="control-label" for="numberOfPaths">Initial Population Size</label>
    <input required
           id="numberOfPaths" 
           name="numberOfPaths"
           class="form-control"
           data-toggle="tooltip" 
           data-original-title="Size of initial population"
           type="number"
           ng-model="geneticSettings.numberOfPaths"
           ng-disabled="!load.graphViewerLoaded"
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
           data-toggle="tooltip" 
           data-original-title="Maximum number of genetic algorithm iterations"
           type="number"
           ng-model="geneticSettings.numberOfEvolutions"
           ng-disabled="!load.graphViewerLoaded"
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
    <label class="control-label" for="stopConditionPercent">Stop Condition (%)</label>
    <input required
           id="stopConditionPercent" 
           name="stopConditionPercent"
           class="form-control" 
           data-toggle="tooltip" 
           data-original-title="If no evolution happens during this percent of maximum number of evolutions, calculation stops"
           type="number"
           ng-model="geneticSettings.stopConditionPercent" 
           ng-disabled="!load.graphViewerLoaded"
           min="0"
           max="100"
           disabled>
    <label class="control-label" 
           for="stopConditionPercent" 
           ng-show="!geneticsettings.stopConditionPercent.$valid">
        Percent must be between 0 and 100
    </label>
</div>