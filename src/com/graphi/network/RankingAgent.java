//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import java.awt.Color;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class RankingAgent extends InfluenceAgent
{
    private RankingAgent treeRootAgent;
    private int propagationCount;
    private Comparator<Node> policyComparator;
    private Set<InfluenceAgent> influenceOffers;
            
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
        influenceOffers     =   new TreeSet<>();   
    }
    
    @Override
    public void influenceAgent(InfluenceAgent target)
    {
        super.influenceAgent(target);
        
        propagationCount++;
        RankingAgent rankingTarget  =   (RankingAgent) target;
        rankingTarget.setTreeRootAgent(getTreeRootAgent());
    }
    
    public void addInfluenceOffer(RankingAgent influenceAgent)
    {
        influenceAgent.addInfluenceOffer(this);
    }
    
    public RankingAgent getTreeRootAgent() 
    {
        return treeRootAgent == null? this : treeRootAgent;
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

    public Comparator<Node> getPolicyComparator()
    {
        return policyComparator;
    }

    public void setPolicyComparator(Comparator<Node> policyComparator) 
    {
        this.policyComparator = policyComparator;
    }
    
    public void clearInfluenceOffers()
    {
        influenceOffers.clear();
    }
    
    public Set<InfluenceAgent> getInfluenceOffers() 
    {
        return influenceOffers;
    }
    
    public int getInfluenceOfferCount()
    {
        return influenceOffers.size();
    }
    
    public boolean hasInfluenceOffers()
    {
        return !influenceOffers.isEmpty();
    }
}
