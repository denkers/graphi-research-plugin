//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Node;
import java.util.HashSet;
import java.util.Set;

public class PolicyController 
{
    private Set<Node> pendingInfluenceAgents;
    
    public PolicyController()
    {
        pendingInfluenceAgents  =   new HashSet<>();
    }
    
    public void pollPendingAgents()
    {
        for(Node pendingAgent: pendingInfluenceAgents)
        {
            
        }
        
        pendingInfluenceAgents.clear();
    }
    
    public void addPendingAgent(Node node)
    {
        pendingInfluenceAgents.add(node);
    }
    
    public Set<Node> getPendingInfluenceAgents()
    {
        return pendingInfluenceAgents;
    }
}
