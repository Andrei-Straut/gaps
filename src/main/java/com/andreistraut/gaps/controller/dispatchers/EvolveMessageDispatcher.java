package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.genetics.GenerationStatistic;
import com.andreistraut.gaps.datamodel.genetics.GeneticEvolver;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

public class EvolveMessageDispatcher extends MessageDispatcher {

    private final Controller controller;
    private final Session session;
    private final MessageType type;

    private int numberOfEvolutions;
    private int stopConditionPercent;

    private DirectedWeightedGraph graph;
    private ArrayList<DirectedWeightedGraphPath> paths;

    public EvolveMessageDispatcher(Controller controller, Session session, MessageType type) {
	super(controller, session, type);
	this.controller = controller;
	this.session = session;
	this.type = type;
    }

    @Override
    public boolean setRequest(MessageRequest request) throws Exception {
	if (!request.getData().has("numberOfEvolutions")
		|| !request.getData().has("stopConditionPercent")) {
	    throw new Exception("Genetic request malformed, missing parameters");
	}

	this.numberOfEvolutions = request.getData().get("numberOfEvolutions").getAsInt();
	this.stopConditionPercent = request.getData().get("stopConditionPercent").getAsInt();
	this.request = request;

	return true;
    }

    @Override
    public void setParameters(ArrayList<Object> parameters) throws Exception {
	if (!(parameters.get(0) instanceof DirectedWeightedGraph)) {
	    throw new Exception("Could not find computed graph. Cannot continue");
	}

	this.graph = (DirectedWeightedGraph) parameters.get(0);

	if (!(parameters.get(1) instanceof ArrayList<?>)) {
	    throw new Exception("Could not find computed paths. Cannot continue");
	}

	this.paths = (ArrayList<DirectedWeightedGraphPath>) parameters.get(1);

	this.parameters = parameters;
    }

    @Override
    protected void updateProgress(MessageResponse response) {
	this.controller.respond(this.session, response);
    }

    @Override
    public boolean process() throws Exception {
	MessageResponse response = new MessageResponse(request.getCallbackId());
	GeneticEvolver evolver = new GeneticEvolver(
		this.numberOfEvolutions, this.stopConditionPercent,
		this.graph, this.paths);

	evolver.init();

	while (!evolver.hasFinished()) {
	    GenerationStatistic statistic = evolver.evolveAndGetStatistics();

	    /*Do not ouput all generations, generates TMI. Only output every 10% of steps,
	     and when there's an evolution*/
	    if (evolver.hasEvolved()) {
		response
			.setStatus(HttpServletResponse.SC_OK)
			.setIsEnded(false)
			.setDescription("Ok").setData(statistic.toJson());
		updateProgress(response);

		Logger.getLogger(Controller.class.getName()).log(
			Level.INFO, "Evolution update for session {0}: {1} ",
			new Object[]{session.getId(), statistic.toJson().toString()});
	    } else {
		if (evolver.getCompletedSteps() % (numberOfEvolutions / 10) == 0) {
		    response
			    .setStatus(HttpServletResponse.SC_OK)
			    .setIsEnded(false)
			    .setDescription("Ok").setData(statistic.toJson());
		    updateProgress(response);

		    Logger.getLogger(Controller.class.getName()).log(
			    Level.INFO, "Evolution update for session {0}: {1} ",
			    new Object[]{session.getId(), statistic.toJson().toString()});
		}
	    }
	}

	GenerationStatistic lastStatistic = evolver.getLastStatistic();
	response
		.setStatus(HttpServletResponse.SC_OK)
		.setIsEnded(true)
		.setData(lastStatistic.toJson())
		.setDescription("Ok");
	updateProgress(response);

	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "Request from {0} with callback ID {1} processed",
		new Object[]{session.getId(), request.getCallbackId()});

	return true;
    }

}
