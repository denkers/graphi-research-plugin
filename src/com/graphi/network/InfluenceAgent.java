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
import java.util.PriorityQueue;
import java.util.Random;

public class InfluenceAgent extends Node
{
    private boolean influenced;
    private boolean authentic;
    private InfluenceAgent influencer;
    
    public InfluenceAgent()
    {
        super();
    }
    
    public InfluenceAgent(int id)
    {
        super(id);
    }
    
    public InfluenceAgent(int id, String name)
    {
        super(id, name);
    }
    
    public InfluenceAgent(int id, String name, Color fill)
    {
        super(id, name, fill);
        
        influenced              =   false;
        authentic               =   true;
        influencer              =   null;
    }

    public int getInfluencedTreeDepth()
    {
        if(influencer == null) return 0;
        else return influencer.getInfluencedTreeDepth() + 1;
    }
    
    public int getInfluencedTreeHeight(Graph<Node, Edge> graph)
    {
        Collection<Node> neighbours =   graph.getNeighbors(this);
        int maxHeight               =   0;
        
        if(neighbours.isEmpty()) return 0;
        
        for(Node neighbour : neighbours)
        {
            InfluenceAgent agentNeighbour   =   (InfluenceAgent) neighbour;
            if(agentNeighbour.isInfluenced() && agentNeighbour.getInfluencer().equals(this)) 
            {
                int neighbourHeight     =   agentNeighbour.getInfluencedTreeHeight(graph);
                if(neighbourHeight > maxHeight) maxHeight   =   neighbourHeight;
            }
        }
        
        return maxHeight + 1;
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
    
    private class InfluencedNeighbourPriorityQueue extends PriorityQueue<Node>
    {
        public InfluencedNeighbourPriorityQueue(int initialCapacity)
        {
            super(initialCapacity, new InfluenceRankComparator(InfluenceAgent.this));
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
