//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import java.util.Comparator;

public class RankingAgentFactory extends InfluenceAgentFactory
{
    private Comparator<Node> policyComparator;
    
    public RankingAgentFactory(Comparator<Node> policyComparator)
    {
        super();
        
        this.policyComparator   =   policyComparator;
    }
    
    public RankingAgentFactory(int lastID)
    {
        super(lastID);
    }
    
    public RankingAgentFactory(int lastID, int incAmount)
    {
        super(lastID, incAmount);
    }
    
    @Override
    public Node create()
    {
        lastID                      +=  incAmount;
        RankingAgent rankingAgent   =    new RankingAgent(lastID, Integer.toHexString(lastID), policyComparator); 
        rankingAgent.setInfluenceDecisionComparator(influenceComparator);
        return rankingAgent;
    }

    public Comparator<Node> getPolicyComparator()
    {
        return policyComparator;
    }

    public void setPolicyComparator(Comparator<Node> policyComparator)
    {
        this.policyComparator = policyComparator;
    }
}
