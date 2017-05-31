//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.graph.GraphDataManager;
import com.graphi.network.DiffusionController;
import com.graphi.network.InfluenceAgentFactory;
import com.graphi.plugins.AbstractPlugin;
import com.graphi.util.factory.NodeFactory;

public class ResearchPlugin extends AbstractPlugin
{
    public static final String PLUGIN_NAME        =   "AUT Research Plugin";
    public static final String PLUGIN_DESCRIPTION =   "Agent-based network diffusion research plugin";   
    private DiffusionController diffusionController;

    public ResearchPlugin()
    {
        displayHandler      =   new ResearchDisplayHandler();
        diffusionController =   new DiffusionController();   
    }
    
    @Override
    public void onEvent(int i)
    {
        super.onEvent(i);
    }

    @Override
    public void initPluginDetails() 
    {
        name        =   PLUGIN_NAME;
        description =   PLUGIN_DESCRIPTION;
    }
    
    @Override
    public void onPluginActivate() 
    {
        GraphDataManager.getGraphDataInstance().setNodeFactory(new InfluenceAgentFactory());
    }
    
    @Override
    public void onPluginDeactivate() 
    {
        GraphDataManager.getGraphDataInstance().setNodeFactory(new NodeFactory());
    }

    @Override
    public void onPluginLoad() {}

    public DiffusionController getDiffusionController() 
    {
        return diffusionController;
    }

    public void setDiffusionController(DiffusionController diffusionController)
    {
        this.diffusionController = diffusionController;
    }
}
