//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import java.awt.Color;

public class RankingAgent extends InfluenceAgent
{
    private RankingAgent treeRootAgent;
    private int propagationCount;
            
    public RankingAgent()
    {
        super();
    }
    
    public RankingAgent(int id)
    {
        super(id);
    }
    
    public RankingAgent(int id, String name)
    {
        super(id, name);
    }
    
    public RankingAgent(int id, String name, Color fill)
    {
        super(id, name, fill);
        
        propagationCount    =   0;
        treeRootAgent       =   null;
    }
    
    @Override
    public void influenceAgent(InfluenceAgent target)
    {
        super.influenceAgent(target);
        
        propagationCount++;
        RankingAgent rankingTarget  =   (RankingAgent) target;
        RankingAgent rootAgent      =   treeRootAgent == null? this : treeRootAgent;
        rankingTarget.setTreeRootAgent(rootAgent);
    }

    public RankingAgent getTreeRootAgent() 
    {
        return treeRootAgent;
    }

    public void setTreeRootAgent(RankingAgent treeRootAgent) 
    {
        this.treeRootAgent = treeRootAgent;
    }

    public int getPropagationCount() 
    {
        return propagationCount;
    }

    public void setPropagationCount(int propagationCount)
    {
        this.propagationCount = propagationCount;
    }
}
