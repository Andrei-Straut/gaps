
<div class="row">
    <div class="col-md-6 col-sm-12 col-xs-12">
        <%@include file="settings-genetic-form.jsp" %>
    </div>

    <div class="col-md-6 col-sm-12 col-xs-12">
        <div class="form-group"
             ng-class="{'has-error':
                         geneticsettingsadvanced.minPopSizePercent.$error.required
                         || !geneticsettingsadvanced.minPopSizePercent.$valid}">
            <label class="control-label" for="minPopSizePercent">Minimum Population Size Percent (%)</label>
            <input required
                   id="minPopSizePercent" 
                   name="minPopSizePercent"
                   class="form-control" 
                   type="number"
                   min="0"
                   max="100"
                   ng-model="geneticSettings.minPopSizePercent" 
                   ng-disabled="!(load.graphStatisticsLoaded)"
                   disabled>
            <label class="control-label" 
                   for="minPopSizePercent" 
                   ng-show="!geneticsettingsadvanced.minPopSizePercent.$valid">
                Percent must be between 0 and 100
            </label>
        </div>

        <div class="form-group">
            <label class="control-label" for="preserveFittestIndividual">Keep Population Size Constant</label>
            <div class="btn-group">
                <button id="keepPopSizeConstant"
                        name="keepPopSizeConstant"
                        class="btn btn-default"
                        aria-expanded="false">Select...</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">Yes</a></li>
                    <li><a href="#">No</a></li>
                </ul>
            </div>
        </div>

        <div class="form-group">
            <label class="control-label" for="preserveFittestIndividual">Preserve fittest individual</label>
            <div class="btn-group">
                <button id="preserveFittestIndividual"
                        name="preserveFittestIndividual"
                        class="btn btn-default" 
                        aria-expanded="true">Select...</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">Yes</a></li>
                    <li><a href="#">No</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="form-group">
    <div class="panel panel-default">
        <div class="panel-heading">
            Genetic Operators (Drag and Drop to select / reorder)
        </div>
        <div class="simpleDemo row">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="row">
                    <div ng-repeat="(listName, list) in geneticSettings.mutators.lists" class="col-md-6">
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                <h3 class="panel-title">{{listName}} Mutators</h3>
                            </div>
                            <ul dnd-list="list">
                                <li ng-repeat="item in list"
                                    dnd-draggable="item"
                                    dnd-moved="list.splice($index, 1)"
                                    dnd-effect-allowed="move"
                                    dnd-selected="geneticSettings.mutators.selected = item"
                                    ng-class="{'selected': geneticSettings.mutators.selected === item}"
                                    >
                                    {{item.label}}
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>