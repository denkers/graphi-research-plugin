//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.sim.PlaybackEntry;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class OverallMeasureComputation 
{
    public static DefaultTableModel getOverallTreeModel(TreeMeasure treeMeasure)
    {
        DefaultTableModel model     =   new DefaultTableModel();
        List<PlaybackEntry> entries =   getEntries();
        model.addColumn("Time Unit");
        
        if(treeMeasure.isTreeSizeMode())
        {
            model.addColumn("Max Tree Size");
            model.addColumn("Average Tree Size");
        }
        
        if(treeMeasure.isTreeHeightMode())
        {
            model.addColumn("Max Tree Height");
            model.addColumn("Average Tree Height");
        }
        
        for(int i = 0 ; i < entries.size(); i++)
        {
            DefaultTableModel currentModel      =   entries.get(i).getComputationModel().getModel();
            List rowList                        =   new ArrayList();
            rowList.add(i);
            
            if(treeMeasure.isTreeSizeMode())
            {
                int maxSize       =   (int) currentModel.getValueAt(0, 3);
                double avgSize    =   (double) currentModel.getValueAt(0, 4);
                rowList.add(maxSize);
                rowList.add(avgSize);
            }
            
            if(treeMeasure.isTreeHeightMode())
            {
                int col             =   model.getColumnCount() > 3? 6 : 3;
                int maxTreeHeight   =   (int) currentModel.getValueAt(0, col);
                double avgHeight    =   (double) currentModel.getValueAt(0, col + 1);
                rowList.add(maxTreeHeight);
                rowList.add(avgHeight);
            }
            
            model.addRow(rowList.toArray());
        }
        
        return model;
    }

    public static DefaultTableModel getOverallPopulationModel(PopulationMeasure populationMeasure)
    {
        DefaultTableModel model     =   new DefaultTableModel();
        List<PlaybackEntry> entries =   getEntries();
        model.addColumn("Time Unit");
        
        if(populationMeasure.isInfluenceMode())
        {
            model.addColumn("Infl. %");
            model.addColumn("Infl. % Change");
            model.addColumn("Average Infl. % Change");
        }
        
        if(populationMeasure.isAuthMode())
        {
            model.addColumn("Unauth %");
            model.addColumn("Unauth % Change");
            model.addColumn("Average Unauth % Change");
        }
        
        for(int i = 0 ; i < entries.size(); i++)
        {
            DefaultTableModel currentModel      =   entries.get(i).getComputationModel().getModel();
            DefaultTableModel prevModel         =   i == 0? null : entries.get(i - 1).getComputationModel().getModel();
            List rowList                        =   new ArrayList<>();
            rowList.add(i);

            if(populationMeasure.isInfluenceMode())
            {
                double inflPortion          =   (double) currentModel.getValueAt(0, 0);
                double inflChange           =   getChangeRate(currentModel, prevModel, 0, 0, inflPortion);
                rowList.add(inflPortion);
                rowList.add(inflChange);
                rowList.add(0.0);
            }
            
            if(populationMeasure.isAuthMode())
            {
                double unauthPortion        =   (double) currentModel.getValueAt(0, 1);
                double unauthChange         =   getChangeRate(currentModel, prevModel, 0, 1, unauthPortion);
                rowList.add(unauthPortion);
                rowList.add(unauthChange);
                rowList.add(0.0);
            }
            
            model.addRow(rowList.toArray());
        }
        
        //Add averages
        if(populationMeasure.isInfluenceMode())
        {
            double inflChangeAverage    =   getColumnAverage(model, 2, entries.size() - 1);
            model.setValueAt(inflChangeAverage, 0, 3);
        }
        
        if(populationMeasure.isAuthMode())
        {
            boolean isBoth              =   model.getColumnCount() > 4;
            int col                     =   isBoth? 6 : 3;
            double authChangeAverage    =   getColumnAverage(model, col - 1, entries.size() - 1);
            model.setValueAt(authChangeAverage, 0, col);
        }
        
        return model;
    }
    
    private static double getChangeRate(DefaultTableModel currentModel, DefaultTableModel prevModel, int row, int col, double currentValue)
    {
        if(prevModel == null || currentModel == null) return 0.0;
        
        double prevValue    =   (double) prevModel.getValueAt(row, col);
        double change       =   currentValue - prevValue;
        
        return change;
    }
    
    private static double getColumnAverage(DefaultTableModel model, int col, int numValues)
    {
        int numRows     =   model.getRowCount();
        double total    =   0;
        
        for(int row = 0; row < numRows; row++)
        {
            double val  =   (double) model.getValueAt(row, col);
            total       +=  val;
        }
        
        return (double) total / (double) numValues;
    }
    
    private static List<PlaybackEntry> getEntries()
    {
        return GraphPanel.getInstance().getPlaybackPanel().getGraphPlayback().getEntries();
    }
}
