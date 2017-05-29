//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.display.layout.GraphPanel;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.util.factory.EdgeFactory;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashSet;
import java.util.Set;

public class DiffusionController 
{
    public static final int STANDARD_MODE       =   0;
    public static final int POLICY_MODE         =   1;
    public static final int STANDARD_AUTH_MODE  =   2;
    public static final int POLICY_AUTH_MODE    =   3;
    
    private Set<Node> activeAgents;
    private Set<Node> pendingInfluenceAgents;
    private NetworkSeeder seeder;
    private NetworkGenerator networkGenerator;
    private int timeUnit;
    private int diffusionMode;
    
    public DiffusionController(NetworkGenerator networkGenerator)
    {
        this(networkGenerator, new NetworkSeeder(), STANDARD_MODE);
    }
    
    public DiffusionController(NetworkGenerator networkGenerator, int diffusionMode)
    {
        this(networkGenerator, new NetworkSeeder(), diffusionMode);
    }
    
    public DiffusionController(NetworkGenerator networkGenerator, NetworkSeeder seeder, int diffusionMode)
    {
        this.networkGenerator   =   networkGenerator;
        this.seeder             =   seeder;
        this.diffusionMode      =   diffusionMode;
        timeUnit                =   0;
        pendingInfluenceAgents  =   new HashSet<>();
        activeAgents            =   null;
    }
    
    public void pollAgents()
    {
        for(Node activeAgent : activeAgents)
        {
            InfluenceAgent agent    =   (InfluenceAgent) activeAgent;
            Node optimalNeighbour   =   agent.chooseOptimalNeighbour();
            
            if(optimalNeighbour == null)
                activeAgents.remove(activeAgent);
            
            else
            {
                InfluenceAgent neighbourAgent   =   (InfluenceAgent) optimalNeighbour;   
                boolean influenceSuccess        =   agent.tryInfluenceAgent(neighbourAgent);
                
                if(influenceSuccess) agent.influenceAgent(neighbourAgent);
            }
        }
    }
    
    public void generateSeeds()
    {
        GraphData graphData     =   GraphDataManager.getGraphDataInstance();
        seeder.generateSeeds(graphData.getNodes(), graphData.getGraph());
        activeAgents            =   new HashSet<>(seeder.getSeeds());
    }
    
    public void generateNetwork()
    {
        Graph<Node, Edge> network   =   networkGenerator.generateNetwork(new InfluenceAgentFactory(), new EdgeFactory());
        MutualNeighbourModel.transformInfluenceNetwork(network);

        GraphData graphData         =   GraphDataManager.getGraphDataInstance();
        graphData.resetFactoryIDs();
        graphData.setGraph(network);
        GraphPanel.getInstance().reloadGraph();
    }
    
    public NetworkSeeder getSeeder() 
    {
        return seeder;
    }

    public void setSeeder(NetworkSeeder seeder) 
    {
        this.seeder = seeder;
    }

    public NetworkGenerator getNetworkGenerator()
    {
        return networkGenerator;
    }

    public void setNetworkGenerator(NetworkGenerator networkGenerator) 
    {
        this.networkGenerator = networkGenerator;
    }

    public Set<Node> getActiveAgents()
    {
        return activeAgents;
    }

    public int getTimeUnit() 
    {
        return timeUnit;
    }

    public Set<Node> getPendingInfluenceAgents()
    {
        return pendingInfluenceAgents;
    }

    public int getDiffusionMode() 
    {
        return diffusionMode;
    }
}
