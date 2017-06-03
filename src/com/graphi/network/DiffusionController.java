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
    public static final int DEFAULT_MAX_UNITS       =   100;
    
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
        
        recordMeasure();
        DataPanel.getInstance().reloadGraphObjects();
        GraphPanel.getInstance().repaintDisplay();
    }
    
    
    public void runDiffusion()
    {
        while(canDiffuse())
        {
            pollAgents();
            
            if(!activeAgents.isEmpty())
            {
                recordMeasure();
                recordState();
            }
        }
        
        System.out.println("DIFFUSION UNITS: " + timeUnit);
    }
    
    public boolean canDiffuse()
    {
        return !activeAgents.isEmpty() && timeUnit <= maxUnits;
    }
    
    public void initDiffusion()
    {
        activeAgents.clear();
        timeUnit    =   0;
        
        generateNetwork();
        generateSeeds();
        DataPanel.getInstance().reloadGraphObjects();
        recordState();
    }
    
    public void generateSeeds()
    {
        GraphData graphData     =   GraphDataManager.getGraphDataInstance();
        seeder.generateSeeds(graphData.getNodes(), graphData.getGraph());
        seeder.runSeedTransformation();
        
        Set<Node> seeds         =   seeder.getSeeds();
        
        for(Node seed : seeds)
            addActiveAgent(seed);
        
        recordMeasure();
    }
    
    public void generateNetwork()
    {
        GraphData graphData         =   GraphDataManager.getGraphDataInstance();
        NodeFactory nodeFactory     =   graphData.getNodeFactory();
        EdgeFactory edgeFactory     =   graphData.getEdgeFactory();
        graphData.resetFactoryIDs();
        Graph<Node, Edge> network   =   networkGenerator.generateNetwork(nodeFactory, edgeFactory);
        
        if(diffusionDecisionType == EIGEN_DECISION_TYPE)
        {
            EigenvectorCentrality centrality        =   new EigenvectorCentrality(network);
            EigenvectorCentralityComparator comp    =   new EigenvectorCentralityComparator(centrality); 
            Collection<Node> nodes                  =   network.getVertices();
            
            for(Node node : nodes)
                ((InfluenceAgent) node).setInfluenceDecisionComparator(comp);
        }
        
        network =   MutualNeighbourModel.transformInfluenceNetwork(network, edgeFactory);
        graphData.setGraph(network);
        GraphPanel.getInstance().reloadGraph();
    }
    
    public void recordMeasure()
    {
        if(measure != null)
        {
            DefaultTableModel model     =   measure.getMeasureModel();
            String name                 =   "Measure for time unit (" + timeUnit + ")";
            AbstractMeasure.setComputationModel(model, name);
        }
    }
    
    public void recordState()
    {
        PlaybackControlPanel pbPanel    =   GraphPanel.getInstance().getPlaybackPanel();
        String name                     =   "Time Unit (" + timeUnit + ")";
        
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
        return diffusionMode == STANDARD_MODE;
    }
    
    public boolean isPolicyMode()
    {
        return diffusionMode == POLICY_MODE;
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
    
