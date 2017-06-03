//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.network.DiffusionController;
import com.graphi.network.data.AbstractMeasure;
import com.graphi.network.data.MixedOverallMeasure;
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
        setProperty("overallMeasureType", "0");
    }

    @Override
    public void performTask() 
    {
        ResearchPlugin plugin                       =   (ResearchPlugin) PluginManager.getInstance().getActivePlugin();
        DiffusionController diffusionController     =   plugin.getDiffusionController();
        int overallMeasureType                      =   Integer.parseInt(getProperty("overallMeasureType"));
        AbstractMeasure measure                     =   diffusionController.getMeasure();
        
        if(measure != null)
        {
            DefaultTableModel model;
            String context;
            
            switch (overallMeasureType)
            {
                case 0:
                    model   =   OverallMeasureComputation.getOverallPopulationModel((PopulationMeasure) measure);
                    context =   "Overall Population Measure";
                    break;
                    
                case 1:
                    model   =   OverallMeasureComputation.getOverallTreeModel((TreeMeasure) measure);
                    context =   "Overall Tree Measure";
                    break;
                    
                case 2:
                    model   =   MixedOverallMeasure.getOverallModel();
                    context =   "Overall Mixed Measure";
                    break;
                    
                default: return;
            }
            
            AbstractMeasure.setComputationModel(model, context);
        }
    }
}
