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
import com.graphi.network.data.AbstractMeasure;
import com.graphi.network.data.AgentDataModel;
import com.graphi.network.data.AgentRowTransformer;
import com.graphi.network.rank.DegreeCentralityComparator;
import com.graphi.network.rank.EigenvectorCentralityComparator;
import com.graphi.network.rank.PolicyController;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.util.factory.EdgeFactory;
import com.graphi.util.factory.NodeFactory;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class DiffusionController 
{
    public static final int STANDARD_MODE           =   0;
    public static final int POLICY_MODE             =   1;
    public static final int STANDARD_AUTH_MODE      =   2;
    public static final int POLICY_AUTH_MODE        =   3;
    public static final int DEFAULT_MAX_UNITS       =   10;
    
    public static final int STANDARD_DECISION_TYPE  =   0;
    public static final int DEGREE_DECISION_TYPE    =   1;
    public static final int EIGEN_DECISION_TYPE     =   2;
    
    private Set<Node> activeAgents;
    private NetworkSeeder seeder;
    private NetworkGenerator networkGenerator;
    private PolicyController policyController;
    private AbstractMeasure measure;
    private int timeUnit;
    private int maxUnits;
    private int diffusionMode;
    private boolean enableMN;
    private int diffusionDecisionType;
    
    public DiffusionController()
    {
        this(null);
    }
    
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
        activeAgents            =   new HashSet<>();
        enableMN                =   true;
        diffusionDecisionType   =   STANDARD_DECISION_TYPE;
    }
    
    public void pollAgents()
    {
        List<Node> currentActiveAgents =   new ArrayList<>(activeAgents);
        for(Node activeAgent : currentActiveAgents)
        {
            InfluenceAgent agent    =   (InfluenceAgent) activeAgent;
            Node optimalNeighbour   =   agent.chooseOptimalNeighbour();
            
            if(optimalNeighbour == null)
                activeAgents.remove(activeAgent);
            
            else
            {
                InfluenceAgent neighbourAgent   =   (InfluenceAgent) optimalNeighbour;   
                boolean influenceSuccess        =   !enableMN? true : agent.tryInfluenceAgent(neighbourAgent);
                
                if(influenceSuccess) 
                {
                    if(isInfluenceMode())
                    {
                        agent.influenceAgent(neighbourAgent);
                        addActiveAgent(neighbourAgent);
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
            
            for(Node agent : adoptedAgents)
                addActiveAgent(agent);
        }
        
        DataPanel.getInstance().reloadGraphObjects();
        GraphPanel.getInstance().repaintDisplay();
    }
    
    
    public void runDiffusion()
    {
        while(canDiffuse())
        {
            pollAgents();
        //    recordState();
        }
    }
    
    public boolean canDiffuse()
    {
        return !activeAgents.isEmpty() && timeUnit < maxUnits;
    }
    
    public void initDiffusion()
    {
        activeAgents.clear();
        timeUnit    =   0;
        
        generateNetwork();
        generateSeeds();
        DataPanel.getInstance().reloadGraphObjects();
    //    recordState();
    }
    
    public void generateSeeds()
    {
        GraphData graphData     =   GraphDataManager.getGraphDataInstance();
        seeder.generateSeeds(graphData.getNodes(), graphData.getGraph());
        seeder.runSeedTransformation();
        
        Set<Node> seeds         =   seeder.getSeeds();
        System.out.println("== GENERATE SEEDS ==");
        System.out.println("Num seeds: " + seeds.size() + " num nodes: " + GraphDataManager.getGraphDataInstance().getGraph().getVertexCount());
        
        for(Node seed : seeds)
            addActiveAgent(seed);
    }
    
    public void generateNetwork()
    {
        GraphData graphData         =   GraphDataManager.getGraphDataInstance();
        NodeFactory nodeFactory     =   graphData.getNodeFactory();
        EdgeFactory edgeFactory     =   graphData.getEdgeFactory();
        graphData.resetFactoryIDs();
        Graph<Node, Edge> network   =   networkGenerator.generateNetwork(nodeFactory, edgeFactory);
        
        System.out.println("== GENERATE NETWORK ==");
        System.out.println("Generator name: " + networkGenerator.getGeneratorName());
        
        if(diffusionDecisionType == EIGEN_DECISION_TYPE)
        {
            EigenvectorCentrality centrality        =   new EigenvectorCentrality(network);
            EigenvectorCentralityComparator comp    =   new EigenvectorCentralityComparator(centrality); 
            Collection<Node> nodes                  =   network.getVertices();
            
            for(Node node : nodes)
                ((InfluenceAgent) node).setInfluenceDecisionComparator(comp);
        }
        
        
        
        Node testNode   =   network.getVertices().iterator().next();
        System.out.println("Graph stats before Transform: (Nodes=" + network.getVertexCount() + ") (Edges=" + network.getEdgeCount() + ")");
        System.out.println("id= " + testNode.getID()  + " Degree before Transform: " + network.degree(testNode));
        network =   MutualNeighbourModel.transformInfluenceNetwork(network, edgeFactory);
        System.out.println("Graph stats after Transform: (Nodes=" + network.getVertexCount() + ") (Edges=" + network.getEdgeCount() + ")");
        System.out.println("id= " + testNode.getID() + " Degree after Transform: " + network.degree(testNode));
        graphData.setGraph(network);
        GraphPanel.getInstance().reloadGraph();
    }
    
    public void recordState()
    {
        PlaybackControlPanel pbPanel    =   GraphPanel.getInstance().getPlaybackPanel();
        String name                     =   "Time Unit (" + timeUnit + ")";
        
        if(measure != null)
        {
            DefaultTableModel model     =   measure.getMeasureModel();
            AbstractMeasure.setComputationModel(model, name);
        }
        
        pbPanel.addRecordedGraph(name, new Date(), true, true, true);
        timeUnit++;
    }
    
    public void addActiveAgent(Node node)
    {
        activeAgents.add(node);
        if(measure != null) measure.addAgent(node);
    }
    
    public void initInfluenceAgentManipulators()
    {
        AgentDataModel model            =   new AgentDataModel();
        AgentRowTransformer transformer =   new AgentRowTransformer();   
        DataPanel dataPanel             =   DataPanel.getInstance();
        InfluenceAgentFactory factory   =   new InfluenceAgentFactory();
        
        if(diffusionDecisionType == STANDARD_DECISION_TYPE)
            factory.setInfluenceComparator(null);
        
        else if(diffusionDecisionType == DEGREE_DECISION_TYPE)
            factory.setInfluenceComparator(new DegreeCentralityComparator());
        
            
        GraphDataManager.getGraphDataInstance().setNodeFactory(factory);
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

    public AbstractMeasure getMeasure() 
    {
        return measure;
    }

    public void setMeasure(AbstractMeasure measure) 
    {
        this.measure = measure;
    }
    
    public boolean hasMeasure()
    {
        return measure != null;
    }

    public boolean isEnableMN() {
        return enableMN;
    }

    public void setEnableMN(boolean enableMN)
    {
        this.enableMN = enableMN;
    }

    public int getDiffusionDecisionType() 
    {
        return diffusionDecisionType;
    }

    public void setDiffusionDecisionType(int diffusionDecisionType)
    {
        this.diffusionDecisionType = diffusionDecisionType;
    }
    
    
}
    
