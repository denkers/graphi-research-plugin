//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import com.graphi.util.factory.NodeFactory;
import java.util.Comparator;

public class InfluenceAgentFactory extends NodeFactory
{
    protected Comparator<Node> influenceComparator;
    
    public InfluenceAgentFactory()
    {
        super();
    }
    
    public InfluenceAgentFactory(int lastID)
    {
        super(lastID);
    }
    
    public InfluenceAgentFactory(int lastID, int incAmount)
    {
        super(lastID, incAmount);
    }

    public Comparator<Node> getInfluenceComparator()
    {
        return influenceComparator;
    }

    public void setInfluenceComparator(Comparator<Node> influenceComparator) 
    {
        this.influenceComparator = influenceComparator;
    }
    
    @Override
    public Node create()
    {
        lastID  +=  incAmount;
        InfluenceAgent agent    =    new InfluenceAgent(lastID, Integer.toHexString(lastID)); 
        agent.setInfluenceDecisionComparator(influenceComparator);
        
        return agent;
    }
}
