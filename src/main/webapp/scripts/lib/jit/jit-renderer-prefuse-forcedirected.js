/** These objects have been ported and adapted to Javascript from the Prefuse Java
 * distribution.
 *
 * Many original comments have been retained in the Javascript as well as original author
 * credit.
 *
 * All Javascript written by Scott Yeadon at the Australian National University.
 */
 
/**
 * Represents a node in the quadtree.
 *
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/NBodyForce.java>
 *
 * jeffrey heer <http://jheer.org">
 */
 var QuadTreeNode = function(){
	this.com = [0.0, 0.0];
	this.children = [null, null, null, null];
	this.hasChildren = false;
	this.mass = null;
	this.fItem = null;
};

/**
 * Helper object to minimize number of object creations across multiple
 * uses of the quadtree.
 *
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/NBodyForce.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var QuadTreeNodeFactory = function(){
	this.maxNodes = 50000;
	this.nodes = new Array();
	
	this.getQuadTreeNode = function() {
		if (this.nodes.length > 0)
		{
			return this.nodes.pop();
		}
		else
		{
			return new QuadTreeNode();
		}
	};
	
	// n is a QuadTreeNode
	this.reclaim = function(n){
		n.mass = 0;
		n.com[0] = 0.0;
		n.com[1] = 0.0;
		n.fItem = null;
		n.hasChildren = false;
		n.children = [null, null, null, null];
		if (this.nodes.length < this.maxNodes)
		{
			this.nodes.push(n);
		}
	};
};

/**
 * Force function which computes an n-body force such as gravity,
 * anti-gravity, or the results of electric charges. This function implements
 * the the Barnes-Hut algorithm for efficient n-body force simulations,
 * using a quad-tree with aggregated mass values to compute the n-body
 * force in O(N log N) time, where N is the number of ForceItems.
 * 
 * The algorithm used is that of J. Barnes and P. Hut, in their research
 * paper A Hierarchical O(n log n) force calculation algorithm, Nature, 
 * v.324, December 1986. For more details on the algorithm, see one of
 *  the following links --
 * 
 *   James Demmel's UC Berkeley lecture notes:
 * <http://www.cs.berkeley.edu/~demmel/cs267/lecture26/lecture26.html>
 *   Description of the Barnes-Hut algorithm:
 * <http://www.physics.gmu.edu/~large/lr_forces/desc/bh/bhdesc.html>
 *   Joshua Barnes' recent implementation
 * <href="http://www.ifa.hawaii.edu/~barnes/treecode/treeguide.html">
 * 
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/NBodyForce.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var NBodyForce = function(){
	this.pnames = ["GravitationalConstant", "Distance", "BarnesHutTheta" ];

	this.DEFAULT_GRAV_CONSTANT = -1.0;
	this.DEFAULT_MIN_GRAV_CONSTANT = -10.0;
	this.DEFAULT_MAX_GRAV_CONSTANT = 10.0;

	this.DEFAULT_DISTANCE = -1.0;
	this.DEFAULT_MIN_DISTANCE = -1.0;
	this.DEFAULT_MAX_DISTANCE = 500.0;

	this.DEFAULT_THETA = 0.9;
	this.DEFAULT_MIN_THETA = 0.0;
	this.DEFAULT_MAX_THETA = 1.0;

	this.GRAVITATIONAL_CONST = 0;
	this.MIN_DISTANCE = 1;
	this.BARNES_HUT_THETA = 2;

	this.xMin, this.xMax, this.yMin, this.yMax = null;
	
	this.factory = new QuadTreeNodeFactory();

	this.params = [this.DEFAULT_GRAV_CONSTANT, this.DEFAULT_DISTANCE, this.DEFAULT_THETA]
	this.minValues = [this.DEFAULT_MIN_GRAV_CONSTANT, this.DEFAULT_MIN_DISTANCE, this.DEFAULT_MIN_THETA];
	this.maxValues = [this.DEFAULT_MAX_GRAV_CONSTANT, this.DEFAULT_MAX_DISTANCE, this.DEFAULT_MAX_THETA];
	this.root = this.factory.getQuadTreeNode();

	/**
	* Set the bounds of the region for which to compute the n-body simulation
	* @param xMin the minimum x-coordinate
	* @param yMin the minimum y-coordinate
	* @param xMax the maximum x-coordinate
	* @param yMax the maximum y-coordinate
	*/
	this.setBounds = function(xMin, yMin, xMax, yMax){
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	};
	
	this.insertHelper = function(p, n, x1, y1, x2, y2){
		var x = p.location[0];
		var y = p.location[1];
		var splitx = (x1+x2)/2;
		var splity = (y1+y2)/2;
		var	i = (x>=splitx ? 1 : 0) + (y>=splity ? 2 : 0);
		// create new child node, if necessary
		if (n.children[i] == null)
		{
			n.children[i] = this.factory.getQuadTreeNode();
			n.hasChildren = true;
		}
		// update bounds
		if (i == 1 || i == 3)
		{
			x1 = splitx;
		}
		else
		{
			x2 = splitx;
		}
		
		if (i > 1)
		{
			y1 = splity;
		}
		else
		{
			y2 = splity;
		}
		
		// recurse
		this.insert(p, n.children[i], x1, y1, x2, y2);
	};
	
	this.isSameLocation = function(fItem1, fItem2){
		var dx = Math.abs(fItem1.location[0]-fItem2.location[0]);
		var dy = Math.abs(fItem1.location[1]-fItem2.location[1]);
		return (dx < 0.01 && dy < 0.01);
	};

	this.insert = function(p, n, x1, y1, x2, y2){
		// try to insert particle p at node n in the quadtree
		// by construction, each leaf will contain either 1 or 0 particles
		if (n.hasChildren)
		{
			// n contains more than 1 particle
			this.insertHelper(p, n, x1, y1, x2, y2);
		}
		else if (n.fItem != null)
		{
			// n contains 1 particle
			if (this.isSameLocation(n.fItem, p))
			{
				this.insertHelper(p, n, x1, y1, x2, y2);
			}
			else
			{
				var v = n.fItem;
				n.fItem = null;
				this.insertHelper(v, n, x1, y1, x2, y2);
				this.insertHelper(p, n, x1, y1, x2, y2);			
			}
		}
		else // n is empty, so is a leaf
		{
			n.fItem = p;
		}
	};

	this.calcMass = function(n){
		var xcom = 0;
		var ycom = 0;
		n.mass = 0;
		if (n.hasChildren)
		{
			for (var i=0; i < n.children.length; i++)
			{
				if (n.children[i] != null)
				{
					this.calcMass(n.children[i]);
					n.mass += n.children[i].mass;
					xcom += n.children[i].mass * n.children[i].com[0];
					ycom += n.children[i].mass * n.children[i].com[1];
				}
			}
		}
		
		if (n.fItem != null)
		{
			n.mass += n.fItem.mass;
			xcom += n.fItem.mass * n.fItem.location[0];
			ycom += n.fItem.mass * n.fItem.location[1];
		}
		
		n.com[0] = xcom / n.mass;
		n.com[1] = ycom / n.mass;
	};


	this.insertRoot = function(fItem){
		this.insert(fItem, this.root, this.xMin, this.yMin, this.xMax, this.yMax);
	};

	this.clearHelper = function(n){
		for (var i = 0; i < n.children.length; i++)
		{
			if (n.children[i] != null)
			{
				this.clearHelper(n.children[i]);
			}
		}
		this.factory.reclaim(n);
	};
		
	this.clear = function(){
		this.clearHelper(this.root);
		this.root = this.factory.getQuadTreeNode();
	};

	this.init = function(fSim){
		this.clear();
		
		var x1 = Number.MAX_VALUE;
		var y1 = Number.MAX_VALUE;
		var x2 = Number.MIN_VALUE;
		var y2 = Number.MIN_VALUE;
		
		for (var i = 0; i < fSim.items.length; i++)
		{
			var x = fSim.items[i].location[0];
			var y = fSim.items[i].location[1];

			if (x < x1) x1 = x;
			if (y < y1) y1 = y;
			if (x > x2) x2 = x;
			if (y > y2) y2 = y;
		}
		var dx = x2-x1;
		var dy = y2-y1;
		if (dx > dy)
		{
			y2 = y1 + dx;
		}
		else
		{
			x2 = x1 + dy;
		}
		this.setBounds(x1, y1, x2, y2);

		for (var i = 0; i < fSim.items.length; i++)
		{
			this.insertRoot(fSim.items[i]);
		}

		// calculate magnitudes and centers of mass
		this.calcMass(this.root);
	};

 	/**
	* Updates the force calculation on the given ForceItem.
 	*/	
	this.getForce = function(fItem){
		this.forceHelper(fItem, this.root, this.xMin, this.yMin, this.xMax, this.yMax);
	};
	
	this.forceHelper = function(item, n, x1, y1, x2, y2){
		var dx = n.com[0] - item.location[0];
		var dy = n.com[1] - item.location[1];
		var r = Math.sqrt(dx*dx+dy*dy);
		var same = false;
		
		if (r == 0.0)
		{
			dx = (Math.random()-0.5) / 50.0;
			dy = (Math.random()-0.5) / 50.0;
			r = Math.sqrt(dx*dx+dy*dy);
			same = true;
		}
		
		var minDist = this.params[this.MIN_DISTANCE] > 0 && r > this.params[this.MIN_DISTANCE];
		
		// the Barnes-Hut approximation criteria is if the ratio of the
		// size of the quadtree box to the distance between the point and
		// the box's center of mass is beneath some threshold theta.
		if ((!n.hasChildren && n.fItem != item) || (!same && (x2 - x1)/r < this.params[this.BARNES_HUT_THETA]))
		{
			if (minDist)
			{
				return;
			}
			var v = this.params[this.GRAVITATIONAL_CONST]*item.mass*n.mass / (r*r*r);
			item.force[0] += v*dx;
			item.force[1] += v*dy;
		}
 		else if (n.hasChildren)
 		{
 			// recurse for more accurate calculation
 			var splitx = (x1+x2)/2;
 			var splity = (y1+y2)/2;
 			for (var i = 0; i < n.children.length; i++)
 			{
 				if (n.children[i] != null)
 				{
 					this.forceHelper(item, n.children[i], (i==1||i==3?splitx:x1), (i>1?splity:y1), (i==1||i==3?x2:splitx), (i>1?y2:splity));
 				}
 			}
 			
 			if (minDist)
 			{
 				return;
 			}
 			
 			if (n.fItem != null && n.fItem != item)
 			{
 				var v = this.params[this.GRAVITATIONAL_CONST]*item.mass*n.fItem.mass / (r*r*r);
 				item.force[0] += v*dx;
 				item.force[1] += v*dy;
 			}
 		}
 	};
};

/**
 * Force function that computes the force acting on ForceItems due to a
 * given Spring.
 * 
 * Java source: 
<https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/SpringForce.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var SpringForce = function(){
	this.pnames = ["SpringCoefficient", "DefaultSpringLength"];
	
	this.DEFAULT_SPRING_COEFF = 1e-4;
	this.DEFAULT_MAX_SPRING_COEFF = 1e-3;
	this.DEFAULT_MIN_SPRING_COEFF = 1e-5;
	this.DEFAULT_SPRING_LENGTH = 50;
	this.DEFAULT_MIN_SPRING_LENGTH = 0;
	this.DEFAULT_MAX_SPRING_LENGTH = 200;
	this.SPRING_COEFF = 0;
	this.SPRING_LENGTH = 1;
	
	this.params = [this.DEFAULT_SPRING_COEFF, this.DEFAULT_SPRING_LENGTH];
	this.minValues = [this.DEFAULT_MIN_SPRING_COEFF, this.DEFAULT_MIN_SPRING_LENGTH];
	this.maxValues = [this.DEFAULT_MAX_SPRING_COEFF, this.DEFAULT_MAX_SPRING_LENGTH];
	
	
	/**
	* Calculates the force vector acting on the items due to the given spring.
	* Updates the force calculation on the given Spring. The ForceItems attached to
	* Spring will have their force values updated appropriately.
	* s - the Spring on which to compute updated forces
	*/
	this.getForce = function(s){
		fItem1 = s.item1;
		fItem2 = s.item2;
		var length = (s.length < 0 ? this.params[this.SPRING_LENGTH] : s.length);
		var x1 = fItem1.location[0];
		var y1 = fItem1.location[1];
		var x2 = fItem2.location[0];
		var y2 = fItem2.location[1];
		var dx = x2-x1;
		var dy = y2-y1;
		var r = Math.sqrt(dx*dx+dy*dy);
		if (r == 0.0)
		{
			dx = (Math.random()-0.5) / 50.0;
			dy = (Math.random()-0.5) / 50.0;
			r = Math.sqrt(dx*dx+dy*dy);
		}
		var d = r - length;
		var coeff = (s.coeff < 0 ? this.params[this.SPRING_COEFF] : s.coeff)*d/r;
		fItem1.force[0] += coeff*dx;
		fItem1.force[1] += coeff*dy;
		fItem2.force[0] += -coeff*dx;
		fItem2.force[1] += -coeff*dy;
	};
	
	this.init = function(){
	};
};

/**
 * Implements a viscosity/drag force to help stabilize items.
 *
 * Java source: 
<https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/DragForce.java>
 * 
 * jeffrey heer <http://jheer.org">
 */
var DragForce = function(){
	this.pnames = ["DragCoefficient"];
	
	this.DEFAULT_DRAG_COEFF = 0.01;
	this.DEFAULT_MIN_DRAG_COEFF = 0.0;
	this.DEFAULT_MAX_DRAG_COEFF = 0.1;
	this.DRAG_COEFF = 0;
	
	this.params = [this.DEFAULT_DRAG_COEFF];
	this.minValues = [this.DEFAULT_MIN_DRAG_COEFF];
	this.maxValues = [this.DEFAULT_MAX_DRAG_COEFF];
	
	this.getForce = function(fItem){
		fItem.force[0] -= this.params[this.DRAG_COEFF]*fItem.velocity[0];
		fItem.force[1] -= this.params[this.DRAG_COEFF]*fItem.velocity[1];
	};
	
	this.init = function(){
	};
};

/**
 * Updates velocity and position data using the 4th-Order Runge-Kutta method.
 * It is slower but more accurate than other techniques such as Euler's Method.
 * The technique requires re-evaluating forces 4 times for a given timestep.
 *
 * Java source: 
<https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/RungeKuttaIntegrator.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var RungeKuttaIntegrator = function(){
	this.integrate = function(sim, timestep){
		var speedLimit = sim.speedLimit;
		var vx, vy, v, coeff;
		var k, l

		for (var i = 0; i < sim.items.length; i++)
		{
			var item = sim.items[i];
			coeff = timestep / item.mass;
			k = item.k;
			l = item.l;
			item.plocation[0] = item.location[0];
			item.plocation[1] = item.location[1];
			k[0][0] = timestep*item.velocity[0];
			k[0][1] = timestep*item.velocity[1];
			l[0][0] = coeff*item.force[0];
			l[0][1] = coeff*item.force[1];
		
			// Set the position to the new predicted position
			item.location[0] += 0.5*k[0][0];
			item.location[1] += 0.5*k[0][1];
		}
		
		// recalculate forces
		sim.accumulate();

		for (var i = 0; i < sim.items.length; i++)
		{
			var item = sim.items[i];
			coeff = timestep / item.mass;
			k = item.k;
			l = item.l;
			vx = item.velocity[0] + 0.5*l[0][0];
			vy = item.velocity[1] + 0.5*l[0][1];
			v = Math.sqrt(vx*vx+vy*vy);
			if (v > speedLimit)
			{
				vx = speedLimit * vx / v;
				vy = speedLimit * vy / v;
			}

			k[1][0] = timestep*vx;
			k[1][1] = timestep*vy;
			l[1][0] = coeff*item.force[0];
			l[1][1] = coeff*item.force[1];
		
			// Set the position to the new predicted position
			item.location[0] = item.plocation[0] + 0.5*k[1][0];
			item.location[1] = item.plocation[1] + 0.5*k[1][1];
		}

		// recalculate forces
		sim.accumulate();

		for (var i = 0; i < sim.items.length; i++)
		{
			var item = sim.items[i];
			coeff = timestep / item.mass;
			k = item.k;
			l = item.l;
			vx = item.velocity[0] + 0.5*l[1][0];
			vy = item.velocity[1] + 0.5*l[1][1];
			v = Math.sqrt(vx*vx+vy*vy);
			if (v > speedLimit)
			{
				vx = speedLimit * vx / v;
				vy = speedLimit * vy / v;
			}
			k[2][0] = timestep*vx;
			k[2][1] = timestep*vy;
			l[2][0] = coeff*item.force[0];
			l[2][1] = coeff*item.force[1];
	
			// Set the position to the new predicted position
			item.location[0] = item.plocation[0] + 0.5*k[2][0];
			item.location[1] = item.plocation[1] + 0.5*k[2][1];
		}
		
		// recalculate forces
		sim.accumulate();

		for (var i = 0; i < sim.items.length; i++)
		{
			var item = sim.items[i];
			coeff = timestep / item.mass;
			k = item.k;
			l = item.l;
			var p = item.plocation;
			vx = item.velocity[0] + l[2][0];
			vy = item.velocity[1] + l[2][1];
			v = Math.sqrt(vx*vx+vy*vy);
			if (v > speedLimit)
			{
				vx = speedLimit * vx / v;
				vy = speedLimit * vy / v;
			}
			k[3][0] = timestep*vx;
			k[3][1] = timestep*vy;
			l[3][0] = coeff*item.force[0];
			l[3][1] = coeff*item.force[1];
			item.location[0] = p[0] + (k[0][0]+k[3][0])/6.0 + (k[1][0]+k[2][0])/3.0;
			item.location[1] = p[1] + (k[0][1]+k[3][1])/6.0 + (k[1][1]+k[2][1])/3.0;
			
			vx = (l[0][0]+l[3][0])/6.0 + (l[1][0]+l[2][0])/3.0;
			vy = (l[0][1]+l[3][1])/6.0 + (l[1][1]+l[2][1])/3.0;
			v = Math.sqrt(vx*vx+vy*vy);
			if (v > speedLimit)
			{
				vx = speedLimit * vx / v;
				vy = speedLimit * vy / v;
			}
			item.velocity[0] += vx;
			item.velocity[1] += vy;
		}
	};
};

/**
 * Manages a simulation of physical forces acting on bodies using N-body, Drag and 
 * Spring forces with a Runge-Kutta integrator.
 *
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/ForceSimulator.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var ForceSimulator = function(){
	this.speedLimit = 1.0;
	this.items = new Array();
	this.springs = new Array();
	this.iforces = new Array();
	this.sforces = new Array();
	this.iflen = 0;
	this.sflen = 0;
	this.integrator = new RungeKuttaIntegrator();
	this.accumulate = function(){
		for (var i = 0; i < this.iforces.length; i++)
		{
			this.iforces[i].init(this);
		}
		
		for (var i = 0; i < this.sforces.length; i++)
		{
			this.sforces[i].init(this);
		}
		
		for (var i = 0; i < this.items.length; i++)
		{
			this.items[i].force[0] = 0.0;
			this.items[i].force[1] = 0.0;
			for (var j = 0; j < this.iforces.length; j++)
			{
				this.iforces[j].getForce(this.items[i]);
			}
		}
		
		for (var i = 0; i < this.springs.length; i++)
		{
			for (var j = 0; j < this.sforces.length; j++)
			{
				this.sforces[j].getForce(this.springs[i]);
			}
		}
	};
	
	this.runSimulator = function(timestep){
		this.accumulate();
		this.integrator.integrate(this, timestep);
	};
	
	this.addForce = function(f, itemForce){
		if (itemForce)
		{
			this.iforces.push(f);
			this.iflen++;
		}
		else
		{
			this.sforces.push(f);
			this.sflen++;
		}
	};
	
	this.addItem = function(fItem){
		this.items.push(fItem);
	};
	
	this.addSpring = function(spring){
		this.springs.push(spring);
	};
};

/**
 * Represents a spring in a force simulation.
 *
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/Spring.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var Spring = function(fItem1, fItem2, coeff, len){
	this.item1 = fItem1;
	this.item2 = fItem2;
	this.coeff = coeff;
	this.length = len;
};

/**
 * Represents a point particle in a force simulation, maintaining values for
 * mass, forces, velocity, and position.
 *
 * Java source: <https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/force/ForceItem.java>
 *
 * jeffrey heer <http://jheer.org">
 */
var ForceItem = function(){
	this.mass = 1.0;
	this.force = new Array();
	this.velocity = new Array();
	this.location = new Array();
	this.plocation = new Array();
	this.k = [[0,0], [0,0], [0,0], [0,0]];
	this.l = [[0,0], [0,0], [0,0], [0,0]];

	this.init = function(xLoc, yLoc){
		this.force[0] = 0;
		this.force[1] = 0;
		this.velocity[0] = 0;
		this.velocity[1] = 0;
		this.location[0] = xLoc;
		this.location[1] = yLoc;
		this.plocation[0] = 0;
		this.plocation[1] = 0;
	};
};

