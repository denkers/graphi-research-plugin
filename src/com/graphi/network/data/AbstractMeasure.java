//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.display.layout.DataPanel;
import com.graphi.graph.Node;
import javax.swing.table.DefaultTableModel;

public abstract class AbstractMeasure 
{
    protected int populationSize;
    protected int recordMode;
    
    public AbstractMeasure()
    {
        populationSize  =   0;
        recordMode      =   0;
    }
    
    public abstract boolean addAgent(Node node);
    
    public abstract DefaultTableModel getMeasureModel();
    
    public static void setComputationModel(DefaultTableModel model, String context)
    {
        DataPanel dataPanel     =   DataPanel.getInstance();
        dataPanel.setComputationModel(model);
        dataPanel.setComputationContext(context);
    }
    
    public int getPopulationSize() 
    {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) 
    {
        this.populationSize = populationSize;
    }

    public int getRecordMode() 
    {
        return recordMode;
    }

    public void setRecordMode(int recordMode) 
    {
        this.recordMode = recordMode;
    }
}
