//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.graph.Node;
import com.graphi.network.DiffusionController;
import com.graphi.network.InfluenceAgent;
import com.graphi.network.RankingAgent;
import com.graphi.network.rank.TrendingSourceComparator;
import com.graphi.plugins.AbstractPlugin;
import com.graphi.tasks.TaskManager;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ResearchPlugin extends AbstractPlugin
{
    public static final String PLUGIN_NAME        =   "AUT Research Plugin";
    public static final String PLUGIN_DESCRIPTION =   "Agent-based network diffusion research plugin";   
    private DiffusionController diffusionController;

    public ResearchPlugin()
    {
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
        TaskManager taskManager =   TaskManager.getInstance();
        taskManager.registerTask(new InitDiffusionControllerTask());
        taskManager.registerTask(new DiffusionTask());
        taskManager.registerTask(new OverallMeasureTask());
        taskManager.registerTask(new MNModelTransformTask());
        taskManager.registerTask(new LazyRecorderTask());
    }
    
    @Override
    public void onPluginDeactivate() {}

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
