//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.network.DiffusionController;
import com.graphi.network.data.AbstractMeasure;
import com.graphi.network.data.OverallMeasureComputation;
import com.graphi.network.data.PopulationMeasure;
import com.graphi.network.data.TreeMeasure;
import com.graphi.plugins.PluginManager;
import com.graphi.tasks.AbstractTask;
import javax.swing.table.DefaultTableModel;

public class OverallMeasureTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Multi-State Measuring");
    }

    @Override
    public void initDefaultProperties() 
    {
        setProperty("populationMeasure", "true");
    }

    @Override
    public void performTask() 
    {
        ResearchPlugin plugin                       =   (ResearchPlugin) PluginManager.getInstance().getActivePlugin();
        DiffusionController diffusionController     =   plugin.getDiffusionController();
        boolean isPopulationMeasure                 =   getProperty("populationMeasure").equalsIgnoreCase("true");
        AbstractMeasure measure                     =   diffusionController.getMeasure();
        
        if(measure != null)
        {
            DefaultTableModel model;
            String context;
            
            if(isPopulationMeasure)
            {
                model   =   OverallMeasureComputation.getOverallPopulationModel((PopulationMeasure) measure);
                context =   "Overall Population Measure";
            }
                
            else
            {
                model   =   OverallMeasureComputation.getOverallTreeModel((TreeMeasure) measure);
                context =   "Overall Tree Measure";
            }
            
            AbstractMeasure.setComputationModel(model, context);
        }
    }
}
