<nav class="navbar navbar-default navbar-cls-top " role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="./">Genetic Path Search</a>
    </div>

    <div id="spinner-wrapper" style="position: fixed; right: 10px; top: 10px; z-index: 100000;" ng-show="load.wip">
        <div class="spinner"></div>
        <span id="wipSpan" style="vertical-align: super; color: #428bca">{{load.wipType}}</span>
    </div>

    <nav class="navbar-default navbar-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav" id="main-menu">
                <li>
                    <a ng-click="setActive('nav-menu-left-top');" 
                       id="nav-menu-left-top" 
                       class="nav-menu-left active-menu" 
                       href="#top"
                       data-toggle="tooltip" 
                       data-placement="right"
                       data-original-title="Top"><i class="fa fa-arrow-up fa-3x"></i>
                    </a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-graph-settings');" 
                       id="nav-menu-left-graph-settings" 
                       class="nav-menu-left" href="#graph-settings"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Settings"><i class="fa fa-cog fa-3x"></i>
                    </a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-graph-viewer');" 
                       id="nav-menu-left-graph-viewer" 
                       class="nav-menu-left" 
                       href="#graph-viewer"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Graph"><i class="fa fa-connectdevelop fa-3x"></i></a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-genetic-statistics');" 
                       id="nav-menu-left-genetic-statistics" 
                       class="nav-menu-left" 
                       href="#genetic-statistics"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Genetic Statistics"><i class="fa fa-venus-mars fa-3x"></i></a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-compare-statistics');" 
                       id="nav-menu-left-compare-statistics" 
                       class="nav-menu-left" 
                       href="#compare-statistics"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Results Compare Statistics"><i class="fa fa-columns fa-3x"></i></a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-path-statistics');" 
                       id="nav-menu-left-path-statistics" 
                       class="nav-menu-left" 
                       href="#path-statistics"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Path Search Statistics"><i class="fa fa-line-chart fa-3x"></i></a>
                </li>
                <li>
                    <a ng-click="setActive('nav-menu-left-graph-statistics');" 
                       id="nav-menu-left-graph-statistics" 
                       class="nav-menu-left" 
                       href="#graph-statistics"
                       data-toggle="tooltip" 
                       data-placement="right" 
                       data-original-title="Graph Statistics"><i class="fa fa-bar-chart-o fa-3x"></i></a>
                </li>
                <li>
                    <a id="nav-menu-left-faq"
                       class="nav-menu-left" 
                       href="https://github.com/Andrei-Straut/gaps/wiki"
                       target="_blank"
                       data-toggle="tooltip" 
                       data-placement="right"
                       data-original-title="Help"><i class="fa fa-info-circle fa-3x"></i>
                    </a>
                </li>
            </ul>
        </div>
    </nav>  
</nav>