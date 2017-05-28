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
import java.util.List;

public class InfluenceAgent extends Node
{
    private boolean influenced;
    private boolean authentic;
    private List<InfluenceAgent> influenceOffers;
    private InfluenceAgent influencer;
    private int depth;
    
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
    }

    public int getInfluencedDepth()
    {
        if(influencer == null) return 0;
        else return influencer.getInfluencedDepth() + 1;
    }
    
    public int getInfluencedHeight(Graph<Node, Edge> graph)
    {
        Collection<Node> neighbours =   graph.getNeighbors(this);
        int maxHeight               =   0;
        
        if(neighbours.isEmpty()) return 0;
        
        for(Node neighbour : neighbours)
        {
            InfluenceAgent agentNeighbour   =   (InfluenceAgent) neighbour;
            if(agentNeighbour.isInfluenced() && agentNeighbour.getInfluencer().equals(this)) 
            {
                int neighbourHeight     =   agentNeighbour.getInfluencedHeight(graph);
                if(neighbourHeight > maxHeight) maxHeight   =   neighbourHeight;
            }
        }
        
        return maxHeight + 1;
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

    public List<InfluenceAgent> getInfluenceOffers() 
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
