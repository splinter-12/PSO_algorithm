package org.example;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class IslandAgent extends Agent {
    private double[] pBestPosition;
    private double pBestFitness;

    protected void setup() {
        // Initialization of the personal best position (pBest) for the island agent
        pBestPosition = new double[]{Math.random(), Math.random()};
        pBestFitness = evaluateFitness(pBestPosition);

        // Add a behavior to handle incoming messages
        addBehaviour(new HandleMessagesBehaviour());
    }

    protected void takeDown() {
        // Cleanup and closing actions for the island agent
        System.out.println("Island agent " + getAID().getName() + " is terminating.");
    }

    private double evaluateFitness(double[] position) {
        // Logic to evaluate the quality of a position (fitness)
        double x = position[0];
        double y = position[1];
        return Math.sin(x) + Math.cos(y);
    }

    private class HandleMessagesBehaviour extends CyclicBehaviour {
        public void action() {
            // Handle incoming messages with the REQUEST performative
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                // Send a reply with the pBest position
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(pBestPosition[0] + "," + pBestPosition[1]);
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }
}
