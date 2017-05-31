//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.graph.GraphDataManager;
import com.graphi.network.InfluenceAgentFactory;
import com.graphi.plugins.PluginDisplayHandler;
import com.graphi.util.factory.NodeFactory;

public class ResearchDisplayHandler implements PluginDisplayHandler
{
    @Override
    public void initPluginDisplay() 
    {
        GraphDataManager.getGraphDataInstance().setNodeFactory(new InfluenceAgentFactory());
    }

    @Override
    public void destroyDisplay() 
    {
        GraphDataManager.getGraphDataInstance().setNodeFactory(new NodeFactory());
    }
    
    @Override
    public void attachDisplay() {}
}
