//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.display.layout.controls.PlaybackControlPanel;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import com.graphi.network.data.AgentDataModel;
import com.graphi.network.data.AgentRowTransformer;
import com.graphi.network.rank.PolicyController;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.util.factory.EdgeFactory;
import edu.uci.ics.jung.graph.Graph;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DiffusionController 
{
    public static final int STANDARD_MODE       =   0;
    public static final int POLICY_MODE         =   1;
    public static final int STANDARD_AUTH_MODE  =   2;
    public static final int POLICY_AUTH_MODE    =   3;
    public static final int DEFAULT_MAX_UNITS   =   10;
    
    private Set<Node> activeAgents;
    private NetworkSeeder seeder;
    private NetworkGenerator networkGenerator;
    private PolicyController policyController;
    private int timeUnit;
    private int maxUnits;
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
        maxUnits                =   DEFAULT_MAX_UNITS;
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
                
                if(influenceSuccess) 
                {
                    if(isInfluenceMode())
                    {
                        agent.influenceAgent(neighbourAgent);
                        activeAgents.add(neighbourAgent);
                    }
                    
                    else
                    {
                        ((RankingAgent) agent).addInfluenceOffer((RankingAgent) neighbourAgent);
                        if(policyController != null) 
                            policyController.addPendingAgent(neighbourAgent);
                    }
                }
            }
        }
        
        if(policyController != null && isPolicyMode())
        {
            Set<Node> adoptedAgents     =   policyController.pollPendingAgents();
            activeAgents.addAll(adoptedAgents);
        }
    }
    
    public boolean canDiffuse()
    {
        return !activeAgents.isEmpty() && timeUnit < maxUnits;
    }
    
    public void initDiffusion()
    {
        generateNetwork();
        generateSeeds();
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
    
    public void recordState()
    {
        PlaybackControlPanel pbPanel    =   GraphPanel.getInstance().getPlaybackPanel();
        String name                     =   "Time Unit (" + timeUnit + ")";
        pbPanel.addRecordedGraph(name, new Date(), true, true, true);
        timeUnit++;
    }
    
    public void initInfluenceAgentManipulators()
    {
        AgentDataModel model            =   new AgentDataModel();
        AgentRowTransformer transformer =   new AgentRowTransformer();   
        DataPanel dataPanel             =   DataPanel.getInstance();
        
        GraphDataManager.getGraphDataInstance().setNodeFactory(new InfluenceAgentFactory());
        dataPanel.setVertexDataModel(model);
        dataPanel.setNodeRowListTransformer(transformer);
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

    public int getDiffusionMode() 
    {
        return diffusionMode;
    }

    public PolicyController getPolicyController()
    {
        return policyController;
    }

    public void setPolicyController(PolicyController policyController) 
    {
        this.policyController = policyController;
    }
    
    public boolean isInfluenceMode()
    {
        return diffusionMode == STANDARD_MODE || diffusionMode == STANDARD_AUTH_MODE;
    }
    
    public boolean isPolicyMode()
    {
        return diffusionMode == POLICY_MODE || diffusionMode == POLICY_AUTH_MODE;
    }

    public int getMaxUnits()
    {
        return maxUnits;
    }

    public void setMaxUnits(int maxUnits)
    {
        this.maxUnits = maxUnits;
    }
}
    
