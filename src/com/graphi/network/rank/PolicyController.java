//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Node;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PolicyController 
{
    private Set<Node> pendingInfluenceAgents;
    private Map<Node, Integer> treeScores;
    
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
    
    public Map<Node, Integer> getTreeScores()
    {
        return treeScores;
    }
    
    public int getTreeScore(Node treeRootNode)
    {
        if(treeScores.containsKey(treeRootNode))
            return treeScores.get(treeRootNode);
        else
            return 0;
    }
}
