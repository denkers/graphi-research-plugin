//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import com.graphi.util.factory.GraphObjFactory;

public class InfluenceAgentFactory extends GraphObjFactory<Node>
{
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

    @Override
    public Node create()
    {
        lastID  +=  incAmount;
        return new InfluenceAgent(lastID, Integer.toHexString(lastID)); 
    }
}
