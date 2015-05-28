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
        <link href="styles/lib/vis/vis.css" rel="stylesheet" />
        <!-- GOOGLE FONTS-->
        <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
        <!-- CUSTOM STYLES-->
        <link href="styles/custom.css" rel="stylesheet" />

		<script type="text/javascript">
			var appInsights=window.appInsights||function(config){
				function s(config){t[config]=function(){var i=arguments;t.queue.push(function(){t[config].apply(t,i)})}}var t={config:config},r=document,f=window,e="script",o=r.createElement(e),i,u;for(o.src=config.url||"//az416426.vo.msecnd.net/scripts/a/ai.0.js",r.getElementsByTagName(e)[0].parentNode.appendChild(o),t.cookie=r.cookie,t.queue=[],i=["Event","Exception","Metric","PageView","Trace"];i.length;)s("track"+i.pop());return config.disableExceptionTracking||(i="onerror",s("_"+i),u=f[i],f[i]=function(config,r,f,e,o){var s=u&&u(config,r,f,e,o);return s!==!0&&t["_"+i](config,r,f,e,o),s}),t
			}({
				instrumentationKey:"f01663e8-df01-4529-86e1-ca15c7037946"
			});
			
			window.appInsights=appInsights;
			appInsights.trackPageView();
		</script>

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
        <!-- VIS SCRIPTS -->
        <script src="scripts/lib/vis/vis.js"></script>
        <!-- GAPS SCRIPTS -->
        <script src="scripts/gaps/app.js"></script>
        <script src="scripts/gaps/app.gaps.js"></script>

        <script src="scripts/gaps/directives/app.gaps.directives.formvalidation.nodeidvalid.js"></script>
        <script src="scripts/gaps/directives/app.gaps.directives.formvalidation.nodeiddifferent.js"></script>
        <script src="scripts/gaps/directives/app.gaps.directives.infocard.js"></script>
        
        <script src="scripts/gaps/app.gaps.factories.websocket.js"></script>
        <script src="scripts/gaps/app.gaps.factories.graph.js"></script>

        <script src="scripts/gaps/statistics/app.gaps.factories.statistics.graph.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.factories.statistics.paths.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.factories.statistics.genetic.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.factories.statistics.compare.js"></script>
        
        <script src="scripts/gaps/statistics/app.gaps.controllers.statistics.graph.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.controllers.statistics.paths.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.controllers.statistics.genetic.js"></script>
        <script src="scripts/gaps/statistics/app.gaps.controllers.statistics.compare.js"></script>
        <script src="scripts/gaps/app.gaps.controllers.gapscontroller.js"></script>

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

                    <%@include file="/pages/fragments/statistics-genetic.jsp" %>

                    <%@include file="/pages/fragments/statistics-compare.jsp" %>

                    <%@include file="/pages/fragments/statistics-paths.jsp" %>

                    <%@include file="/pages/fragments/statistics-graph.jsp" %>
                    
                    <%@include file="/pages/fragments/modal-loading-error.jsp" %>

                </div>
                <!-- /. PAGE INNER  -->
            </div>
            <!-- /. PAGE WRAPPER  -->
        </div>
        <!-- /. WRAPPER  -->
    </body>
</html>
