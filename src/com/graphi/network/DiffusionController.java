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
    private Set<Node> activeAgents;
    private NetworkSeeder seeder;
    private NetworkGenerator networkGenerator;
    private int timeUnit;
    
    public DiffusionController(NetworkGenerator networkGenerator)
    {
        this(networkGenerator, new NetworkSeeder());
    }

    public DiffusionController(NetworkGenerator networkGenerator, NetworkSeeder seeder)
    {
        this.networkGenerator   =   networkGenerator;
        this.seeder             =   seeder;
        timeUnit                =   0;
        activeAgents            =   null;
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
}
