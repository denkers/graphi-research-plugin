//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.network.DiffusionController;
import com.graphi.plugins.PluginManager;
import com.graphi.tasks.AbstractTask;

public class DiffusionTask extends AbstractTask
{

    @Override
    public void initTaskDetails() 
    {
        setTaskName("Run Diffusion");
    }

    @Override
    public void initDefaultProperties()
    {
        setProperty("runAutomatic", "false");
    }

    @Override
    public void performTask() 
    {
        ResearchPlugin plugin                       =   (ResearchPlugin) PluginManager.getInstance().getActivePlugin();
        DiffusionController diffusionController     =   plugin.getDiffusionController();
        boolean enableAutomaticRun                  =   getProperty("runAutomatic").equalsIgnoreCase("true");
        
        if(enableAutomaticRun)
            diffusionController.runDiffusion();
        
        else
            diffusionController.pollAgents();
    }
}
