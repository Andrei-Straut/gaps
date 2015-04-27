
<div class="row">

    <div class="col-md-6 col-sm-12 col-xs-12">
        <%@include file="settings-genetic-form.jsp" %>
    </div>

    <div class="col-md-6 col-sm-12 col-xs-12">
        <div class="form-group"
             ng-class="{'has-error' :
        geneticsettings.minPopSizePercent.$error.required
                         || !geneticsettings.minPopSizePercent.$valid}">
            <label class="control-label" for="minPopSizePercent">Minimum Population Size Percent (%)</label>
            <input required
                   id="minPopSizePercent" 
                   name="minPopSizePercent"
                   class="form-control" 
                   type="number"
                   ng-model="geneticSettings.minPopSizePercent" 
                   ng-disabled="!(load.graphStatisticsLoaded)"
                   min="0"
                   max="100"
                   disabled>
            <label class="control-label" 
                   for="minPopSizePercent" 
                   ng-show="!geneticsettings.minPopSizePercent.$valid">
                Percent must be between 0 and 100
            </label>
        </div>

        <div class="form-group">
            <div class="btn-group">
                <button id="keepPopSizeConstant"
                        name="keepPopSizeConstant"
                        class="btn btn-default"
                        aria-expanded="true">Keep Population Size Constant</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">Yes</a></li>
                    <li><a href="#">No</a></li>
                </ul>
            </div>
        </div>

        <div class="form-group">
            <div class="btn-group">
                <button id="preserveFittestIndividual"
                        name="preserveFittestIndividual"
                        class="btn btn-default" 
                        aria-expanded="true">Preserve Fittest Individual</button>
                <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">Yes</a></li>
                    <li><a href="#">No</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="form-group">
        <div class="panel panel-default">
            <div class="panel-heading">
                Genetic Operators (Drag and Drop to select / reorder)
            </div>
            <div class="simpleDemo row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="row">
                        <div ng-repeat="(listName, list) in models.lists" class="col-md-6">
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title">List {{listName}}</h3>
                                </div>
                                <ul dnd-list="list">
                                    <li ng-repeat="item in list"
                                        dnd-draggable="item"
                                        dnd-moved="list.splice($index, 1)"
                                        dnd-effect-allowed="move"
                                        dnd-selected="models.selected = item"
                                        ng-class="{'selected': models.selected === item}"
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
</div>