package org.example;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {



    public static void main(String[] args) {
        // Get a JADE runtime instance
        Runtime rt = Runtime.instance();

        // Create a default profile
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        // Create the main container
        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            // Create the master agent
            AgentController masterAgent = mainContainer.createNewAgent("master", "org.example.MasterAgent", null);
            masterAgent.start();

            // Create island agents
            for (int i = 0; i < 5; i++) {
                AgentController islandAgent = mainContainer.createNewAgent("island" + i, "org.example.IslandAgent", null);
                islandAgent.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
    }

