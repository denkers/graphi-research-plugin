//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class InfluenceAgent extends Node
{
    private boolean influenced;
    private boolean authentic;
    private Set<InfluenceAgent> influenceOffers;
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
        
        influenced      =   false;
        authentic       =   true;
        influencer      =   null;
        influenceOffers =   new TreeSet<>();   
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
    
    public void addInfluenceOffer(InfluenceAgent influenceAgent)
    {
        influenceAgent.addInfluenceOffer(this);
    }
    
    public void influenceNode(InfluenceAgent influencerAgent)
    {
        influenced  =   true;
        authentic   =   influencerAgent.isAuthentic();
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

    public Set<InfluenceAgent> getInfluenceOffers() 
    {
        return influenceOffers;
    }
    
    public void clearInfluenceOffers()
    {
        influenceOffers.clear();
    }
    
    public int getInfluenceOfferCount()
    {
        return influenceOffers.size();
    }
    
    public boolean hasInfluenceOffers()
    {
        return !influenceOffers.isEmpty();
    }
    
    public Node getInfluencer()
    {
        return influencer;
    }
    
}
