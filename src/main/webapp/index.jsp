<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="gaps">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />

        <title>Genetic Algorithm Path Search</title>

        <!-- ANGULAR STYLES-->
        <link href="styles/lib/angular/angular.ui-notification.css" rel="stylesheet" />
        <link href="styles/lib/angular/angular.draganddrop.css" rel="stylesheet" />
        <!-- BOOTSTRAP STYLES-->
        <link href="styles/lib/bootstrap/bootstrap.css" rel="stylesheet" />
        <link href="styles/lib/bootstrap/bootstrap.toggle.css" rel="stylesheet" />
        <link href="styles/lib/bootstrap/bootstrap.slider.css" rel="stylesheet" />
        <link href="styles/lib/bootstrap/bootstrap.dataTables.css" rel="stylesheet" />
        <!-- FONTAWESOME STYLES-->
        <link href="styles/lib/font-awesome/font-awesome.css" rel="stylesheet" />
        <!-- JIT STYLES-->
        <link href="styles/lib/jit/jit.css" rel="stylesheet" />
        <!-- MORRIS CHART STYLES-->
        <link href="styles/lib/morris/morris-0.4.3.min.css" rel="stylesheet" />
        <!-- GOOGLE FONTS-->
        <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
        <!-- CUSTOM STYLES-->
        <link href="styles/custom.css" rel="stylesheet" />

        <!-- JQUERY SCRIPTS -->
        <script src="scripts/lib/jquery/jquery-1.11.2.js"></script>
        <script src="scripts/lib/jquery/jquery.dataTables.js"></script>
        <script src="scripts/lib/jquery/jquery.metisMenu.js"></script>
        <!-- BOOTSTRAP SCRIPTS -->
        <script src="scripts/lib/bootstrap/bootstrap.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.toggle.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.dataTables.js"></script>
        <!-- ANGULAR SCRIPTS -->
        <script src="scripts/lib/angular/angular.min.js"></script>
        <script src="scripts/lib/angular/angular.ui.bootstrap.tpls.js"></script>
        <script src="scripts/lib/angular/angular.ui.notification.min.js"></script>
        <script src="scripts/lib/angular/angular.ui.bootstrap.slider.js"></script>
        <script src="scripts/lib/angular/angular.draganddrop.js"></script>
        <!-- JIT SCRIPTS -->
        <script src="scripts/lib/jit/jit.js"></script>
        <script src="scripts/lib/jit/jit-custom-forcedirected.js"></script>
        <script src="scripts/lib/jit/jit-renderer-prefuse-forcedirected.js"></script>
        <script src="scripts/lib/jit/jit-renderer-forcedirected.js"></script>
        <!-- MORRIS CHART SCRIPTS -->
        <script src="scripts/lib/morris/raphael-2.1.0.min.js"></script>
        <script src="scripts/lib/morris/morris.js"></script>
        <!-- CUSTOM SCRIPTS -->
        <script src="scripts/app.gaps.js"></script>
        <script src="scripts/app.gaps.websocketfactory.js"></script>
        <script src="scripts/app.gaps.statisticsfactory.js"></script>
        <script src="scripts/app.gaps.gapscontroller.js"></script>
        <script src="scripts/app.gaps.statisticscontroller.js"></script>
        <script src="scripts/app.gaps.formvalidation.js"></script>
        <script src="scripts/main.js"></script>

    </head>
    <body>
        <div id="wrapper" ng-controller="gapscontroller" ng-init="init();">
            <a name="top"></a>

            <!-- /. NAV TOP  -->
            <%@include file="/pages/fragments/nav-top.jsp" %>

            <!-- /. NAV SIDE  -->
            <div id="page-wrapper" >
                <div id="page-inner">

                    <%@include file="/pages/fragments/settings.jsp" %>

                    <%@include file="/pages/fragments/graph-viewer.jsp" %>

                    <div ng-controller="statisticscontroller">
                        <%@include file="/pages/fragments/genetic-statistics.jsp" %>

                        <%@include file="/pages/fragments/compare-statistics.jsp" %>

                        <%@include file="/pages/fragments/path-statistics.jsp" %>

                        <%@include file="/pages/fragments/graph-statistics.jsp" %>
                    </div>

                    <div class="modal fade" id="modalLoadingError" tabindex="-1" role="dialog" aria-labelledby="loadingError" aria-hidden="true" style="display: none;">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                    <h4 class="modal-title" id="loadingError">Loading error</h4>
                                </div>
                                <div class="modal-body">
                                    An error has occurred loading the data :(<br/>
                                    {{errorText}}
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /. PAGE INNER  -->
            </div>
            <!-- /. PAGE WRAPPER  -->
        </div>
        <!-- /. WRAPPER  -->
    </body>
</html>
