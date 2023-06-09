package org.example;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MasterAgent extends Agent {
    private double[] gBestPosition;
    private double gBestFitness;

    protected void setup() {
        // Initialization of the global best position (gBest) for the master agent
        gBestPosition = null;
        gBestFitness = Double.MAX_VALUE;

        // Start the PSO algorithm on the master agent
        addBehaviour(new PSOBehaviour());
    }

    protected void takeDown() {
        // Cleanup and closing actions for the master agent
        System.out.println("Master agent is terminating.");
    }

    private class PSOBehaviour extends Behaviour {
        private boolean isCoordinationDone;

        public void onStart() {
            isCoordinationDone = false;
        }

        public void action() {
            // Coordination of island agents and updating the global best position (gBest)
            if (!isCoordinationDone) {
                // Retrieve the pBest positions of the island agents
                DFAgentDescription[] islandAgentDescriptions = getIslandAgentDescriptions();
                double[][] pBestPositions = new double[islandAgentDescriptions.length][];
                for (int i = 0; i < islandAgentDescriptions.length; i++) {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.addReceiver(islandAgentDescriptions[i].getName());
                    msg.setConversationId("pBestPosition");
                    send(msg);

                    ACLMessage reply = blockingReceive();
                    if (reply != null && reply.getPerformative() == ACLMessage.INFORM) {
                        String[] positionStr = reply.getContent().split(",");
                        pBestPositions[i] = new double[]{Double.parseDouble(positionStr[0]), Double.parseDouble(positionStr[1])};
                    }
                }

                // ...
            }
        }

        public boolean done() {
            // Termination condition for the PSO algorithm for the master agent
            // In this example, the algorithm runs for a single iteration
            return true;
        }

        private DFAgentDescription[] getIslandAgentDescriptions() {
            // Create an agent description template to search for agents of type IslandAgent
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("IslandAgent");
            template.addServices(sd);

            try {
                // Search for agents matching the agent description template
                DFAgentDescription[] result = DFService.search(myAgent, template);
                return result;
            } catch (FIPAException e) {
                e.printStackTrace();
                return new DFAgentDescription[0];
            }
        }
    }
}
