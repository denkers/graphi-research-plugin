//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.InfluenceAgent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class MixedMeasure extends AbstractMeasure
{
    private PopulationMeasure populationMeasure;
    private TreeMeasure treeMeasure;
    
    public MixedMeasure()
    {
        populationMeasure   =   new PopulationMeasure(PopulationMeasure.RECORD_BOTH_MODE);
        treeMeasure         =   new TreeMeasure(TreeMeasure.RECORD_BOTH_MODE);
    }
    
    @Override
    public boolean addAgent(Node node)
    {
        populationMeasure.addAgent(node);
        treeMeasure.addAgent(node);
        
        return true;
    }

    @Override
    public DefaultTableModel getMeasureModel() 
    {
        DefaultTableModel model     =   new DefaultTableModel();
        
        //PopulationMeasure data
        model.addColumn("Influenced %");
        model.addColumn("Unauthentic %");
        
        //TreeMeasure data
        
        model.addColumn("Tree ID");
        model.addColumn("AuthenticTree");
        
        model.addColumn("TreeSize");
        model.addColumn("Max Tree Size");
        model.addColumn("Average Tree Size");

        model.addColumn("TreeHeight");
        model.addColumn("MaxTree Height");
        model.addColumn("AverageTree Height");
        
        Set<Node> treeNodes =   treeMeasure.getTreeSizes().keySet();
        Iterator<Node> it   =   treeNodes.iterator();
        
        for(int i = 0; it.hasNext(); i++)
        {
            List rowList                =   new ArrayList();
            
            if(i == 0)
            {
                rowList.add(populationMeasure.computeInflencePopulationRatio());
                rowList.add(populationMeasure.computeUnauthenticPopulationRatio());
            }
            
            else
            {
                rowList.add(0.0);
                rowList.add(0.0);
            }
            
            InfluenceAgent nextAgent    =   (InfluenceAgent) it.next();
            int ID                      =   nextAgent.getID();
            boolean authentic           =   nextAgent.isAuthentic();
            
            rowList.add(ID);
            rowList.add(authentic);
            
            int treeSize            =   treeMeasure.getTreeSizes().get(nextAgent);
            rowList.add(treeSize);

            int maxTreeSize         =   i == 0? treeMeasure.getMaxTreeSize() : 0;
            double averageTreeSize  =   i == 0? treeMeasure.getAverage(false) : 0.0;

            rowList.add(maxTreeSize);
            rowList.add(averageTreeSize);

            int treeHeight      =   treeMeasure.getMaxTreeHeights().get(nextAgent);
            rowList.add(treeHeight);

            int maxTreeHeight           =   i == 0? treeMeasure.getMaxTreeHeight() : 0;
            double averageTreeHeight    =   i == 0? treeMeasure.getAverage(true) : 0;

            rowList.add(maxTreeHeight);
            rowList.add(averageTreeHeight);
            
            model.addRow(rowList.toArray());
        }
        
        return model;
    }

    public PopulationMeasure getPopulationMeasure() 
    {
        return populationMeasure;
    }

    public void setPopulationMeasure(PopulationMeasure populationMeasure)
    {
        this.populationMeasure = populationMeasure;
    }

    public TreeMeasure getTreeMeasure()
    {
        return treeMeasure;
    }

    public void setTreeMeasure(TreeMeasure treeMeasure)
    {
        this.treeMeasure = treeMeasure;
    }
}
