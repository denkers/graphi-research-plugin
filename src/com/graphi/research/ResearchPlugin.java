//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research;

import com.graphi.plugins.AbstractPlugin;

public class ResearchPlugin extends AbstractPlugin
{
    public static final String PLUGIN_NAME        =   "AUT Research Plugin";
    public static final String PLUGIN_DESCRIPTION =   "Agent-based network diffusion research plugin";   
    
    public ResearchPlugin(String name, String description) 
    {
        super(PLUGIN_NAME, PLUGIN_DESCRIPTION);
    }

    @Override
    public void onEvent(int i)
    {
    }
}
