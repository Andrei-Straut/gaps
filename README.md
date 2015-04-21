GAPS (*Genetic Algorithm Path Search*)
==============
For a more detailed description, please see [Wiki page](https://github.com/Andrei-Straut/gaps/wiki)  
See [the project in action](http://gaps.azurewebsites.net)  
And because we shouldn't take anyone's word for it, [latest build information](http://gaps.azurewebsites.net/build)  

What is GAPS?
--------------
A webapp started as a small side project doing path searches in graphs using genetic algorithms

How does it work?
--------------
For a description on genetic algorithms, [Wikipedia has it (loosely) covered](http://en.wikipedia.org/wiki/Genetic_algorithm). For graph theory, [Wikipedia has it covered again](http://en.wikipedia.org/wiki/Graph_theory). Putting the two and two together, and combining some great frameworks and technologies (see Thanks section), GAPS was born.

Just like any standard genetic algorithm. Picks a random number of initial paths, then it combines and mutates them until a good solution / stop condition is reached.

How well does it perform?
--------------
For small graphs (30 - 100 nodes, 100 - 1000 edges), in ~10% of cases it outperforms [JGraphT's](https://github.com/jgrapht/jgrapht) KShortestPath algorithm when it concerns path costs. In terms of runtime performance, it is slower, however, there's a chance of finding better paths.

In most cases, it loses to JGraphT, but barely, usually either tied up, or coming close behind.

What are the limits?
--------------
Starting from around 100 nodes / 1000 edges, it becomes a bit sluggish client-side due to graph rendering issues. However, if installed and run client-side (there's a runnable Main class with examples), it performs well even for very large graphs (100k nodes, 1M edges).

What are the future plans?
--------------
Please check the [issues page](https://github.com/Andrei-Straut/gaps/issues) for a list of upcoming features.

Special thanks
--------------
[JGAP](http://jgap.sourceforge.net/) helped a lot to get stuff off the ground quickly, and the possibilities of customization are practically endless.

[JGraphT](https://github.com/jgrapht/jgrapht), awesome framework for dealing with graphs.

[Binary Cart](http://binarycart.com/), one of the best bootstrap templates out there, keep it up guys! ::thumbsup::

[Bootstrap](http://getbootstrap.com/) - no explanation needed, it just works!

[AngularJS](https://angularjs.org/) was love at first sight, and a lasting relationship since then, 'nuff said

[JIT](http://philogb.github.io/jit/), Because rendering graphs in the browser should be this easy

[Angular UI notification](https://github.com/alexcrack/angular-ui-notification) because users should always be in the loop

[Bootstrap slider](http://www.eyecon.ro/bootstrap-slider/) and [Bootstrap toggle](http://www.bootstraptoggle.com/) two of the few plugins out there that actually work without extensive hacking or adaptation. Nice work!

[Morris Charts](http://morrisjs.github.io/morris.js/) Put together with JIT, these two pack an unstoppable punch

Also, special special thanks to [Pedro Serra](https://github.com/pdiogomserra) for being that wall I could bounce ideas from, and generally for listening to me being geek for hours (ok, minutes at most) on end