//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Node;
import com.graphi.network.RankingAgent;
import java.io.Serializable;
import java.util.Comparator;

public class TrendingTreeComparator implements Comparator<Node>, Serializable
{
    private final PolicyController policyController;
    
    public TrendingTreeComparator(PolicyController policyController)
    {
        this.policyController   =   policyController;
    }
    
    @Override
    public int compare(Node nodeA, Node nodeB) 
    {
        RankingAgent agentA     =   (RankingAgent) nodeA;
        RankingAgent agentB     =   (RankingAgent) nodeB;
        int scoreA              =   policyController.getTreeScore(agentA.getTreeRootAgent());
        int scoreB              =   policyController.getTreeScore(agentB.getTreeRootAgent());
        
        if(scoreA > scoreB) return -1;
        else if(scoreA < scoreB) return 1;
        else return 0;
    }
}
