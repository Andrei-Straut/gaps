/**
 * Extension methods to JIT library: custom nodes, custom edges, custom behavior, etc
 */

$jit.Graph.prototype.isNode = function (object) {
    return (object.data
            && object.data.objectType.toLowerCase().trim() === 'node');
};

$jit.Graph.prototype.isEdge = function (object) {
    return (object.nodeFrom
            && object.nodeTo);
};

$jit.Graph.Node.prototype.renderCustom = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.NodeOptions) {
        this.setData('type', viewerOptions.NodeOptions.getType());
        this.setData('color', viewerOptions.NodeOptions.getColor());
    }
};
$jit.Graph.Node.prototype.renderNodePath = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.NodeOptions) {
        this.setData('type', viewerOptions.NodeOptions.getPathType());
        this.setData('color', viewerOptions.NodeOptions.getPathColor());
    }
};

$jit.Graph.Node.prototype.getHtmlTooltip = function () {
    //count connections
    list = [];
    this.eachAdjacency(function (adj) {
        if (adj.getData('alpha')) {
            list.push(adj.nodeTo.name);
        }
    });

    //display node info in tooltip 
    var html = "<h4>Node Id: " + this.id + "</h4>";
    html = html + "<p><b>Node Name:</b><br/>" + this.name + "</br>";
    html = html + "<b>Connections: </b><br/>";
    html = html + list.join(", ") + "<p/>";

    return html;
};
/**
 * Set a node as selected, changing style to selected
 * 
 * @param {type} viewerOptions Object holding the graph style definitions
 */
$jit.Graph.Node.prototype.select = function (viewerOptions) {
    this.renderCustom(viewerOptions);

    if (viewerOptions
            && viewerOptions.NodeOptions
            && viewerOptions.NodeOptions.nodeDimSelected) {
        this.setData('dim', viewerOptions.NodeOptions.nodeDimSelected, 'end');
    }

    this.eachAdjacency(function (adj) {
        adj.select(viewerOptions);
    });

    this.selected = true;
};
$jit.Graph.Node.prototype.selectpath = function (viewerOptions) {
    this.renderCustom(viewerOptions);

    if (viewerOptions
            && viewerOptions.NodeOptions
            && viewerOptions.NodeOptions.nodeDimSelected) {
        this.setData('dim', viewerOptions.NodeOptions.nodeDimSelected, 'end');
    }
    
    this.renderNodePath(viewerOptions);
    this.selectedPath = true;
};
/**
 * Set a node as unselected, while reverting to unselected style.
 * 
 * @param {type} viewerOptions Object holding the graph style definitions
 */
$jit.Graph.Node.prototype.unselect = function (viewerOptions) {
    this.renderCustom(viewerOptions);

    if (viewerOptions
            && viewerOptions.NodeOptions
            && viewerOptions.NodeOptions.nodeDimDefault) {
        this.setData('dim', viewerOptions.NodeOptions.nodeDimDefault, 'end');
    }

    this.eachAdjacency(function (adj) {
        adj.unselect(viewerOptions);
    });

    delete this.selected;
};
$jit.Graph.Node.prototype.unselectpath = function (viewerOptions) {
    this.renderCustom(viewerOptions);

    if (viewerOptions
            && viewerOptions.NodeOptions
            && viewerOptions.NodeOptions.nodeDimDefault) {
        this.setData('dim', viewerOptions.NodeOptions.nodeDimDefault, 'end');
    }

    this.eachAdjacency(function (adj) {
        adj.unselect(viewerOptions);
    });

    delete this.selectedPath;
};

/**
 * Set an edge as selected, changing style to selected
 * 
 * @param {type} viewerOptions Object holding the graph style definitions
 */
$jit.Graph.Adjacence.prototype.select = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.EdgeOptions
            && viewerOptions.EdgeOptions.edgeWidthSelected) {
        this.setDataset('end', {
            lineWidth: viewerOptions.EdgeOptions.edgeWidthSelected
        });
    }

    this.selected = true;
};
$jit.Graph.Adjacence.prototype.selectpath = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.EdgeOptions
            && viewerOptions.EdgeOptions.edgeWidthSelected) {
        this.setDataset('end', {
            lineWidth: viewerOptions.EdgeOptions.edgeWidthSelected,
            color: viewerOptions.EdgeOptions.edgeColorPath
        });
    }

    this.selectedPath = true;
};
/**
 * Set an edge as unselected, while reverting to unselected style.
 * 
 * @param {type} viewerOptions Object holding the graph style definitions
 */
$jit.Graph.Adjacence.prototype.unselect = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.EdgeOptions
            && viewerOptions.EdgeOptions.edgeWidthDefault) {
        this.setDataset('end', {
            lineWidth: viewerOptions.EdgeOptions.edgeWidthDefault,
            color: viewerOptions.EdgeOptions.edgeColorDefault
        });
    }

    delete this.selected;
};
$jit.Graph.Adjacence.prototype.unselectpath = function (viewerOptions) {
    if (viewerOptions
            && viewerOptions.EdgeOptions
            && viewerOptions.EdgeOptions.edgeWidthDefault) {
        this.setDataset('end', {
            lineWidth: viewerOptions.EdgeOptions.edgeWidthDefault,
            color: viewerOptions.EdgeOptions.edgeColorDefault
        });
    }

    delete this.selectedPath;
};

$jit.RGraph.Plot.EdgeTypes.implement({
    'labeledarrow': {
        'render': function (adj, canvas) {
            //get nodes cartesian coordinates 
            var pos = adj.nodeFrom.pos.getc(true);
            var posChild = adj.nodeTo.pos.getc(true);

            //check for edge label in data
            var data = adj.data;

            if (data.id) {
                //if the label doesn't exist create it and append it to the label container 
                var domlabel = document.getElementById('edge-id-arrow-' + data.id);
                if (!domlabel) {
                    domlabel = document.createElement('span');
                    domlabel.id = 'edge-id-arrow-' + data.id;
                    domlabel.innerHTML = data.cost;
                }

                //now adjust the label placement 
                var ox = canvas.translateOffsetX,
                        oy = canvas.translateOffsetY,
                        sx = canvas.scaleOffsetX,
                        sy = canvas.scaleOffsetY,
                        posx = (pos.x + posChild.x) / 2 * sx + ox,
                        posy = (pos.y + posChild.y) / 2 * sy + oy,
                        s = canvas.getSize();

                var labelPosX = parseInt((pos.x + posChild.x) / 2);
                var labelPosY = parseInt((pos.y + posChild.y) / 2);
                this.viz.canvas.getCtx().fillText(domlabel.innerHTML, labelPosX, labelPosY);

                var labelPos = {
                    x: Math.round(labelPosX - domlabel.offsetWidth / 2 + s.width / 2),
                    y: Math.round(labelPosY - domlabel.offsetHeight / 2 + s.height / 2)
                };

                domlabel.style.left = labelPos.x + 'px;';
                domlabel.style.top = labelPos.y + 'px;';

                //plot arrow edge
                this.edgeTypes.arrow.render.call(this, adj, canvas);
            }
        },
        'contains': function (adj, pos) {
            var from = adj.nodeFrom.pos.getc(true),
                    to = adj.nodeTo.pos.getc(true);

            if (this.edgeHelper.arrow.contains(from, to, pos, this.edge.epsilon)) {
                this.edgeTypes.labeledarrow.showTooltip(adj, pos,
                        this.viz.canvas, this.labels.getLabelContainer());
            } else {
                this.edgeTypes.labeledarrow.hideTooltip(adj);
            }

            return this.edgeHelper.arrow.contains(from, to, pos, this.edge.epsilon);
        },
        'showTooltip': function (adj, pos, canvas, labelContainer) {
            var data = adj.data;
            var domlabelId = 'edge-id-tooltip-' + data.id;
            var domlabel = document.getElementById(domlabelId);

            //check for edge label in data
            if (data.id) {
                //if the label doesn't exist create it and append it to the label container 
                if (!domlabel) {
                    domlabel = document.createElement('div');
                    domlabel.id = domlabelId;
                    domlabel.className = 'tip';

                    var html = "<h4>Edge Id: " + data.id + "</h4>";
                    html = html + "<p><b>Cost: </b>" + data.cost + "<br/>";
                    html = html + "<b>From: </b>" + adj.nodeTo.name + "<br/>";
                    html = html + "<b>To: </b>" + adj.nodeFrom.name + "</p>";

                    domlabel.innerHTML = html;
                }

                var labelPos = {
                    x: Math.round((pos.x * canvas.scaleOffsetX)
                            + (canvas.getSize().width / 2)
                            + (canvas.translateOffsetX)),
                    y: Math.round((pos.y * canvas.scaleOffsetY)
                            + (canvas.getSize().height / 2)
                            + (canvas.translateOffsetY))
                };

                domlabel.style.position = 'absolute';
                $(domlabel).css('left', (labelPos.x) + 'px');
                $(domlabel).css('top', (labelPos.y) + 'px');
                $(domlabel).show();
                labelContainer.appendChild(domlabel);
            }
        },
        'hideTooltip': function (adj) {
            var data = adj.data;
            var domlabelId = 'edge-id-tooltip-' + data.id;
            var domlabel = document.getElementById(domlabelId);

            if (domlabel) {
                $(domlabel).hide();
            }
        }
    }
});

var GraphViewerOptions = {
    canvasId: 'infovis',
    canvasHeight: 500,
    canvasWidth: 500,
    selectEpsilon: 15,
    selectAnimationDuration: 50,
    computeAnimationDuration: 200,
    iterations: 5,
    levelDistance: 100,
    NodeOptions: {
        overridable: true,
        nodeDimDefault: 8,
        nodeHeightDefault: 8,
        nodeWidthDefault: 8,
        nodeTypeDefault: 'circle',
        nodeColorDefault: '#337ab7',
        nodeHeightSelected: 15,
        nodeWidthSelected: 15,
        nodeDimSelected: 15,
        get: function () {
            return {
                // These properties are also set per node
                // with dollar prefixed data-properties in the
                // JSON structure.
                overridable: this.overridable,
                dim: this.nodeDimDefault,
                type: this.nodeTypeDefault,
                color: this.nodeColorDefault,
                //used by ellipse and rectangle types:
                height: this.nodeHeightDefault,
                width: this.nodeWidthDefault,
                epsilon: GraphViewerOptions.selectEpsilon
            };
        },
        getType: function () {
            return 'circle';
        },
        getPathType: function () {
            return 'circle';
        },
        getColor: function () {
            return '#337ab7';
        },
        getPathColor: function () {
            return '#00CE6F';
        }
    },
    EdgeOptions: {
        overridable: true,
        edgeDimDefault: 12,
        edgeWidthDefault: 0.5,
        edgeWidthSelected: 2.5,
        edgeTypeDefault: 'labeledarrow',
        edgeColorDefault: '#23A4FF',
        edgeColorPath: '#00CE6F',
        get: function () {
            return {
                // These properties are also set per node
                // with dollar prefixed data-properties in the
                // JSON structure.
                overridable: this.overridable,
                type: this.edgeTypeDefault,
                dim: this.edgeDimDefault,
                lineWidth: this.edgeWidthDefault,
                color: this.edgeColorDefault,
                epsilon: GraphViewerOptions.selectEpsilon
            };
        }
    },
    LabelOptions: {
        fontSize: "1em",
        color: "#000000"
    },
    NavigationOptions: {
        enable: true,
        type: 'Native',
        panning: 'avoid nodes',
        zooming: 20,
        get: function () {
            return {
                //Enable zooming and panning
                //with scrolling and DnD
                enable: this.enable,
                type: this.type,
                //Enable panning events only if we're dragging the empty
                //canvas (and not a node).
                panning: this.panning,
                zooming: this.zooming //zoom speed. higher is more sensible
            };
        }
    },
    TipOptions: {
        enable: true,
        enableForEdges: true,
        type: 'Native',
        offsetX: 20,
        offsetY: 20
    },
    EventOptions: {
        enable: true,
        type: 'Native',
        enableForEdges: true
    }
};

var Log = {
    elem: false,
    write: function (text) {
        if (!this.elem)
            this.elem = document.getElementById('log');
        this.elem.innerHTML = text;
        //this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
    }
};