package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import java.util.ArrayList;
import javax.websocket.Session;

public class MessageDispatcherFactory {

    private DirectedWeightedGraph graph;
    private ArrayList<DirectedWeightedGraphPath> paths;
    private final Controller controller;
    private final Session session;

    public MessageDispatcherFactory(Controller controller, Session session) {
        this.controller = controller;
        this.session = session;
    }

    public MessageDispatcher getDispatcher(MessageType messageType) throws Exception {
        if (messageType == null) {
            throw new NullPointerException("Message Type cannot be null");
        }

        MessageDispatcher dispatcher;

        switch (messageType) {
            case GETGRAPH: {
                dispatcher = new GetGraphMessageDispatcher(
                        controller, session, messageType);
                break;
            }
            case UPLOADGRAPH: {
                dispatcher = new UploadGraphMessageDispatcher(
                        controller, session, messageType);
                break;
            }
            case COMPUTEPATHS: {
                dispatcher = new ComputePathMessageDispatcher(
                        controller, session, messageType);
                break;
            }
            case EVOLVE: {
                dispatcher = new EvolveMessageDispatcher(
                        controller, session, messageType);
                break;
            }
            case COMPARE: {
                dispatcher = new CompareStatisticsMessageDispatcher(
                        controller, session, messageType);
                break;
            }
            case UNKNOWN:
            default: {
                throw new Exception("Message Type Unknown");
            }
        }

        return dispatcher;
    }

    public void initDispatcherRequest(MessageDispatcher dispatcher, MessageRequest request) throws Exception {
        dispatcher.setRequest(request);
    }

    public void initDispatcherParams(MessageDispatcher dispatcher) throws Exception {
        ArrayList<Object> params = new ArrayList<Object>();

        if (dispatcher instanceof ComputePathMessageDispatcher) {
            params.add(this.graph);
            dispatcher.setParameters(params);
        } else if (dispatcher instanceof EvolveMessageDispatcher) {
            params.add(this.graph);
            params.add(this.paths);
            dispatcher.setParameters(params);
        } else if (dispatcher instanceof CompareStatisticsMessageDispatcher) {
            params.add(this.graph);
        }

        dispatcher.setParameters(params);
    }

    public void process(MessageDispatcher dispatcher) throws Exception {
        dispatcher.process();

        if (dispatcher instanceof GetGraphMessageDispatcher) {
            this.graph = ((GetGraphMessageDispatcher) dispatcher).getGraph();
        } else if (dispatcher instanceof UploadGraphMessageDispatcher) {
            this.graph = ((UploadGraphMessageDispatcher) dispatcher).getGraph();
        } else if (dispatcher instanceof ComputePathMessageDispatcher) {
            this.paths = ((ComputePathMessageDispatcher) dispatcher).getPaths();
        }
    }

    public void release() {
        this.graph = null;
        this.paths = null;
    }
}
