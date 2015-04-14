var labelType, useGradients, nativeTextSupport, animate;

(function () {
    var ua = navigator.userAgent,
            iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
            typeOfCanvas = typeof HTMLCanvasElement,
            nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
            textSupport = nativeCanvasSupport
            && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    //I'm setting this based on the fact that ExCanvas provides text support for IE
    //and that as of today iPhone/iPad current text support is lame
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

var rtime = new Date(1, 1, 2000, 12, 00, 00);
var timeout = false;
var delta = 800;
$(window).resize(function () {
    rtime = new Date();
    if (timeout === false) {
        timeout = true;
        setTimeout(resizeend, delta);
    }

});
function resizeend() {
    if (window.fd) {
        if (new Date() - rtime < delta) {
            setTimeout(resizeend, delta);
        } else {
            timeout = false;
            var distanceToRight = $(window).width() - $('#infovis').offset().left;
            fd.canvas.resize(distanceToRight - 20, $('#infovis').height(), true);
        }
    }
}

function jitInit(json) {

    var fd = new $jit.ForceDirected({
        injectInto: GraphViewerOptions.canvasId,
        width: GraphViewerOptions.canvasHeight,
        height: GraphViewerOptions.canvasWidth,
        Node: GraphViewerOptions.NodeOptions.get(),
        Edge: GraphViewerOptions.EdgeOptions.get(),
        Navigation: GraphViewerOptions.NavigationOptions.get(),
        iterations: GraphViewerOptions.iterations,
        levelDistance: GraphViewerOptions.levelDistance,
        // This method is only triggered
        // on label creation and only for DOM labels (not native canvas ones).
        Tips: {
            enable: GraphViewerOptions.TipOptions.enable,
            type: GraphViewerOptions.TipOptions.type,
            offsetX: GraphViewerOptions.TipOptions.offsetX,
            offsetY: GraphViewerOptions.TipOptions.offsetY,
            onShow: function (tip, node) {
                tip.innerHTML = node.getHtmlTooltip();
            }
        },
        // Add node events
        Events: {
            enable: GraphViewerOptions.EventOptions.enable,
            type: GraphViewerOptions.EventOptions.type,
            enableForEdges: GraphViewerOptions.EventOptions.enableForEdges,
            //Change cursor style when hovering a node
            onMouseEnter: function (graphComponent, eventInfo, e) {
                fd.canvas.getElement().style.cursor = 'move';

                if (graphComponent.selectedPath && graphComponent.selectedPath === true) {
                    return;
                }

                fd.graph.eachNode(function (n) {
                    if (!n.selectedPath || n.selectedPath !== true) {
                        n.unselect(GraphViewerOptions);
                    }
                });

                if (!graphComponent.selected && !graphComponent.selectedPath === true) {
                    graphComponent.select(GraphViewerOptions);
                }

                //trigger animation to final styles
                fd.fx.animate({
                    modes: ['node-property:dim',
                        'edge-property:lineWidth:color'],
                    duration: GraphViewerOptions.selectAnimationDuration
                });
            },
            onMouseLeave: function (graphComponent, eventInfo, e) {
                fd.canvas.getElement().style.cursor = '';

                if (graphComponent.selectedPath && graphComponent.selectedPath === true) {
                    return;
                }

                fd.graph.eachNode(function (n) {
                    if (!n.selectedPath || n.selectedPath !== true) {
                        n.unselect(GraphViewerOptions);
                    }
                }
                );
                if (fd.graph.isEdge(graphComponent)) {
                    var selectedEdge = graphComponent;
                    if (!selectedEdge.selectedPath || selectedEdge.selectedPath !== true) {
                        selectedEdge.unselect(GraphViewerOptions);
                    }
                }

                //trigger animation to final styles
                fd.fx.animate({
                    modes: ['node-property:dim',
                        'edge-property:lineWidth:color'],
                    duration: GraphViewerOptions.selectAnimationDuration
                });
            },
            onClick: function (graphComponent, eventInfo, e) {
                //clicked object is in fact an edge
                /*if (fd.graph.isEdge(graphComponent)) {
                 var selectedEdge = graphComponent;
                 } else if (fd.graph.isNode(graphComponent)) {
                 var selectedVertex = graphComponent;
                 }*/
                return true;
            },
            //Update node positions when dragged
            onDragMove: function (graphComponent, eventInfo, e) {

                if (graphComponent.nodeFrom) {
                    return;
                }

                var pos = eventInfo.getPos();
                graphComponent.pos.setc(pos.x, pos.y);
                fd.plot();
            },
            //Implement the same handler for touchscreens
            onTouchMove: function (node, eventInfo, e) {
                /*$jit.util.event.stop(e); //stop default touchmove event
                 this.onDragMove(node, eventInfo, e);*/
            }
        },
        onCreateLabel: function (domElement, node) {
            // Create a 'name' and 'close' buttons and add them
            // to the main node label
            var nameContainer = document.createElement('span'),
                    style = nameContainer.style;
            nameContainer.className = 'name';
            nameContainer.innerHTML = node.name;
            domElement.appendChild(nameContainer);
            style.fontSize = GraphViewerOptions.LabelOptions.fontSize;
            style.color = GraphViewerOptions.LabelOptions.color;

            node.renderCustom(GraphViewerOptions);

            //Toggle a node selection when clicking
            //its name. This is done by animating some
            //node styles like its dimension and the color
            //and lineWidth of its adjacencies.
        },
        // Change node styles when DOM labels are placed
        // or moved.
        onPlaceLabel: function (domElement, node) {
            var style = domElement.style;
            var left = parseInt(style.left);
            var top = parseInt(style.top);
            var w = domElement.offsetWidth;
            style.left = (left - w / 2) + 'px';
            style.top = (top + 10) + 'px';
            style.display = '';
        }
    });
    fd.canvas.clear();
    // load JSON data.
    fd.loadJSON(json);
    // compute positions incrementally and animate.
    fd.computeIncremental({
        iter: GraphViewerOptions.iterations,
        property: 'end',
        onStep: function (perc) {
            Log.write(perc + '% loaded...');
        },
        onComplete: function () {
            var distanceToRight = $(window).width() - $('#infovis').offset().left - 60;
            fd.canvas.resize(distanceToRight, $('#infovis').height());

            Log.write('Graph Loaded');
            fd.animate({
                modes: ['linear', 'node-property:dim', 'edge-property:lineWidth:color'],
                transition: $jit.Trans.Circ.easeIn,
                duration: GraphViewerOptions.computeAnimationDuration
            });
        }
    });
    // end
    window.fd = fd;
    return fd;
}