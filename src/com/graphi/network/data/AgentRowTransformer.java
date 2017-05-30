//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.InfluenceAgent;
import com.graphi.util.transformer.NodeRowListTransformer;
import java.util.List;

public class AgentRowTransformer extends NodeRowListTransformer
{
    @Override
    public List transform(Node node)
    {
        List rowList                =   super.transform(node);
        InfluenceAgent agent        =   (InfluenceAgent) node;
        Node influencer             =   agent.getInfluencer();   
        
        rowList.add(agent.isInfluenced());
        rowList.add(agent.isAuthentic());
        rowList.add(influencer == null? "SEED" : influencer.getID());
        
        return rowList;
    }
}
