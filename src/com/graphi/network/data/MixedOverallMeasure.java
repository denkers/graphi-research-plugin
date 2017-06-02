//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.sim.PlaybackEntry;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class MixedOverallMeasure 
{
    public static DefaultTableModel getOverallModel()
    {
        DefaultTableModel model     =   new DefaultTableModel();
        List<PlaybackEntry> entries =   OverallMeasureComputation.getEntries();
        model.addColumn("Time Unit");
        
        //PopulationMeasure columns
        model.addColumn("Infl. %");
        model.addColumn("Infl. % Change");
        model.addColumn("Avg Infl. % Change");
        
        model.addColumn("Unauth %");
        model.addColumn("Unauth % Change");
        model.addColumn("Avg Unauth % Change");
        
        //TreeMeasure columns
        model.addColumn("Max Tree Size");
        model.addColumn("Avg Tree Size");
        
        model.addColumn("Max Tree Height");
        model.addColumn("Avg Tree Height");
        
        for(int i = 0 ; i < entries.size(); i++)
        {
            DefaultTableModel currentModel      =   entries.get(i).getComputationModel().getModel();
            DefaultTableModel prevModel         =   i == 0? null : entries.get(i - 1).getComputationModel().getModel();
            List rowList                        =   new ArrayList<>();
            rowList.add(i);
            
            //PopulationMeasure data
            double inflPortion          =   (double) currentModel.getValueAt(0, 0);
            double inflChange           =   OverallMeasureComputation.getChangeRate(currentModel, prevModel, 0, 0, inflPortion);
            rowList.add(inflPortion);
            rowList.add(inflChange);
            rowList.add(0.0);
            
            double unauthPortion        =   (double) currentModel.getValueAt(0, 1);
            double unauthChange         =   OverallMeasureComputation.getChangeRate(currentModel, prevModel, 0, 1, unauthPortion);
            rowList.add(unauthPortion);
            rowList.add(unauthChange);
            rowList.add(0.0);
            
            //TreeMeasure data
            int maxSize       =   (int) currentModel.getValueAt(0, 5);
            double avgSize    =   (double) currentModel.getValueAt(0, 6);
            rowList.add(maxSize);
            rowList.add(avgSize);
            
            int maxTreeHeight   =   (int) currentModel.getValueAt(0, 8);
            double avgHeight    =   (double) currentModel.getValueAt(0, 9);
            rowList.add(maxTreeHeight);
            rowList.add(avgHeight);
            
            model.addRow(rowList.toArray());
        }
        
        //Add PopulationMeasure averages
        double inflChangeAverage    =   OverallMeasureComputation.getColumnAverage(model, 2, entries.size() - 1);
        model.setValueAt(inflChangeAverage, 0, 3);
        
        double authChangeAverage    =   OverallMeasureComputation.getColumnAverage(model, 5, entries.size() - 1);
        model.setValueAt(authChangeAverage, 0, 6);
        
        return model;
    }
}
