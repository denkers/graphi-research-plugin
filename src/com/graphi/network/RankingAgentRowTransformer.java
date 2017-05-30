//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import java.util.List;

public class RankingAgentRowTransformer extends AgentRowTransformer
{
    @Override
    public List transform(Node node)
    {
        List rowList        =   super.transform(node);
        RankingAgent agent  =   (RankingAgent) node;
        
        rowList.add(agent.getTreeRootAgent().getID());
        rowList.add(agent.getPropagationCount());
        
        return rowList;
    }
}
