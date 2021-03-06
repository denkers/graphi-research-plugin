//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.network.rank.InfluenceRankComparator;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class InfluenceAgent extends Node
{
    protected boolean influenced;
    protected boolean authentic;
    protected InfluenceAgent influencer;
    protected InfluenceAgent treeRootAgent;
    protected Comparator<Node> influenceDecisionComparator;
    
    public InfluenceAgent()
    {
        this(0, "");
    }
    
    public InfluenceAgent(int id)
    {
        this(id, "");
    }
    
    public InfluenceAgent(int id, String name)
    {
        this(id, name, null);
    }
    
    public InfluenceAgent(int id, String name, Color fill)
    {
        super(id, name, fill);
        
        influenced                  =   false;
        authentic                   =   true;
        influencer                  =   null;
        treeRootAgent               =   null;
        influenceDecisionComparator =   new InfluenceRankComparator(this);
    }

    public int getInfluencedTreeDepth()
    {
        if(influencer == null) return 0;
        else return influencer.getInfluencedTreeDepth() + 1;
    }
    
    public boolean tryInfluenceAgent(InfluenceAgent target)
    {
        Graph<Node, Edge> network   =   GraphDataManager.getGraphDataInstance().getGraph();
        Edge edge                   =   network.findEdge(this, target);
        Random rGen                 =   new Random();
        
        if(edge != null)
        {
            double influenceProbability     =   edge.getWeight();
            double roll                     =   rGen.nextDouble();
            
            return roll <= influenceProbability;
        }
        
        return false;
    }
    
    public Node chooseOptimalNeighbour()
    {
        Graph<Node, Edge> network       =   GraphDataManager.getGraphDataInstance().getGraph();
        Collection<Node> neighbours     =   network.getNeighbors(this);
        
        if(neighbours.isEmpty()) return null;
        
        else
        {
            InfluencedNeighbourPriorityQueue rankedNeighbours   =   new InfluencedNeighbourPriorityQueue(neighbours.size());
            rankedNeighbours.addAll(neighbours);
            
            return rankedNeighbours.peek();
        }
    }
    
    public void influenceAgent(InfluenceAgent target)
    {
        target.setInfluenced(true);
        target.setInfluencer(this);
        target.setAuthentic(authentic);
        target.setFill(fill);
        target.setTreeRootAgent(getTreeRootAgent());
    }
    
    public boolean isInfluenced() 
    {
        return influenced;
    }

    public void setInfluenced(boolean influenced) 
    {
        this.influenced = influenced;
    }

    public boolean isAuthentic() 
    {
        return authentic;
    }

    public void setAuthentic(boolean authentic)
    {
        this.authentic = authentic;
    }

    public Node getInfluencer()
    {
        return influencer;
    }
    
    public void setInfluencer(InfluenceAgent influencer)
    {
        this.influencer =   influencer;
    }
    
    public InfluenceAgent getTreeRootAgent() 
    {
        return treeRootAgent == null? this : treeRootAgent;
    }

    public void setTreeRootAgent(InfluenceAgent treeRootAgent) 
    {
        this.treeRootAgent = treeRootAgent;
    }

    public Comparator<Node> getInfluenceDecisionComparator() 
    {
        return influenceDecisionComparator;
    }

    public void setInfluenceDecisionComparator(Comparator<Node> influenceDecisionComparator)
    {
        if(influenceDecisionComparator == null)
            this.influenceDecisionComparator =   new InfluenceRankComparator(this);
        else
            this.influenceDecisionComparator = influenceDecisionComparator;
    }
    
    @Override
    public Node copyGraphObject()
    {
        InfluenceAgent agent   =   new InfluenceAgent(id, name, fill);
        agent.setInfluenced(influenced);
        agent.setAuthentic(authentic);
        agent.setTreeRootAgent(treeRootAgent);
        agent.setInfluenceDecisionComparator(influenceDecisionComparator);
        
        return agent;
    }
    
    private class InfluencedNeighbourPriorityQueue extends PriorityQueue<Node>
    {
        public InfluencedNeighbourPriorityQueue(int initialCapacity)
        {
            super(initialCapacity, influenceDecisionComparator);
        }
        
        @Override
        public boolean add(Node node)
        {
            if(node instanceof InfluenceAgent)
            {
                InfluenceAgent agent    =   (InfluenceAgent) node;
                
                if(agent.isInfluenced())
                    return false;
                
                else return super.add(node);
            }
            
            else return false;
        }
    }
}
