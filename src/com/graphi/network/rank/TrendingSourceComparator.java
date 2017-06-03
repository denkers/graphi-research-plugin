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

public class TrendingSourceComparator implements Comparator<Node>, Serializable
{
    @Override
    public int compare(Node nodeA, Node nodeB) 
    {
        RankingAgent agentA     =   (RankingAgent) nodeA;
        RankingAgent agentB     =   (RankingAgent) nodeB;
        int propagationCountA   =   agentA.getPropagationCount();
        int propagationCountB   =   agentB.getPropagationCount();
        
        if(propagationCountA > propagationCountB)
            return -1;
        
        else if (propagationCountA < propagationCountB)
            return 1;
        
        else return 0;
    }
}
