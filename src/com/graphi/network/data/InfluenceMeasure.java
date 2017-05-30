//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.InfluenceAgent;
import java.util.HashSet;
import java.util.Set;

public class InfluenceMeasure 
{
    private int populationSize;
    private Set<Node> influencedAgents;
    private Set<Node> unauthenticAgents;
    private boolean recordUnauthenticAgents;
    
    public InfluenceMeasure()
    {
        this(0, false);
    }
    
    public InfluenceMeasure(int populationSize, boolean recordUnauthenticAgents)
    {
        this(populationSize, new HashSet<>(), recordUnauthenticAgents);
    }
    
    public InfluenceMeasure(int populationSize, Set<Node> nodes, boolean recordUnauthenticAgents)
    {
        this.populationSize             =   populationSize;
        this.recordUnauthenticAgents    =   recordUnauthenticAgents;
        influencedAgents                =   nodes.isEmpty()? nodes : new HashSet<>(nodes);
        unauthenticAgents               =   new HashSet<>();
    }
    
    public boolean addAgent(Node node)
    {
        if(node != null && node instanceof InfluenceAgent)
        {
            InfluenceAgent agent    =   (InfluenceAgent) node;
            if(!agent.isInfluenced()) return false;
            
            influencedAgents.add(agent);
            
            if(recordUnauthenticAgents && !agent.isAuthentic())
                unauthenticAgents.add(agent);
                
            return true;
        }
        
        else return false;
    }
    
    public double computeUnauthenticPopulationRatio()
    {
        if(unauthenticAgents.isEmpty()) return 0.0;
        return (double) unauthenticAgents.size() / (double) populationSize;
    }
    
    public double computeInflencePopulationRatio()
    {
        if(influencedAgents.isEmpty()) return 0.0;
        return (double) influencedAgents.size() / (double) populationSize;
    }
    
    public int getInfluencedCount()
    {
        return influencedAgents.size();
    }
    
    public int getUnauthenticCount()
    {
        return unauthenticAgents.size();
    }
    
    public int getPopulationSize() 
    {
        return populationSize;
    }

    public void setPopulationSize(int populationSize)
    {
        this.populationSize = populationSize;
    }

    public Set<Node> getInfluencedAgents() 
    {
        return influencedAgents;
    }

    public void setInfluencedNodes(Set<Node> influencedAgents) 
    {
        this.influencedAgents     =   influencedAgents;
    }

    public Set<Node> getUnauthenticAgents() 
    {
        return unauthenticAgents;
    }

    public boolean isRecordUnauthenticAgents() 
    {
        return recordUnauthenticAgents;
    }

    public void setRecordUnauthenticAgents(boolean recordUnauthenticAgents)
    {
        this.recordUnauthenticAgents = recordUnauthenticAgents;
    }
    
    
}
