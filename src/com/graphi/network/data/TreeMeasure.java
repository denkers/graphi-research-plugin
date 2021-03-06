//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.InfluenceAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class TreeMeasure extends AbstractMeasure
{
    public static final int RECORD_TREE_HEIGHT  =   0;
    public static final int RECORD_TREE_SIZE    =   1;
    public static final int RECORD_BOTH_MODE    =   2;
    
    private Map<Node, Integer> maxTreeHeights;
    private Map<Node, Integer> treeSizes;
    private Node maxTreeSizeNode, maxTreeHeightNode;
    
    public TreeMeasure()
    {
        this(0, RECORD_TREE_SIZE);
    }
    
    public TreeMeasure(int recordMode)
    {
        this(0, recordMode);
    }
    
    public TreeMeasure(int populationSize, int recordMode)
    {
        this.populationSize     =   populationSize;
        this.recordMode         =   recordMode;
        treeSizes               =   new HashMap<>();
        maxTreeHeights          =   new HashMap<>();
        maxTreeSizeNode         =   null;
        maxTreeHeightNode       =   null;
    }
    
    @Override
    public boolean addAgent(Node node)
    {
        if(node != null)
        {
            InfluenceAgent agent  =   (InfluenceAgent) node;
            
            if(isTreeHeightMode())
            {
                InfluenceAgent treeSource =   agent.getTreeRootAgent();
                
                if(!maxTreeHeights.containsKey(treeSource))
                    maxTreeHeights.put(treeSource, 0);
                
                else
                {
                    int depth   =   agent.getInfluencedTreeDepth();
                    
                    if(maxTreeHeights.get(treeSource) < depth)
                        maxTreeHeights.put(treeSource, depth);

                    if(maxTreeHeightNode == null || (depth > maxTreeHeights.get(maxTreeHeightNode)))
                        maxTreeHeightNode   =   treeSource;
                }
            }
            
            if(isTreeSizeMode())
            {
                InfluenceAgent treeSource   =   agent.getTreeRootAgent();
                int treeSize                =   treeSizes.containsKey(treeSource)? (treeSizes.get(treeSource) + 1) : 1;
                treeSizes.put(treeSource, treeSize);
                
                if(maxTreeSizeNode == null || (treeSize > treeSizes.get(maxTreeSizeNode)))
                    maxTreeSizeNode   =   treeSource;
            }
            
            return true;
        }
        
        else return false;
    }
    
    public double getAverage(boolean treeHeight)
    {
        Map<Node, Integer> treeRecordings   =   treeHeight? maxTreeHeights : treeSizes;
        if(treeRecordings == null || treeRecordings.isEmpty()) return 0.0;
        
        Set<Node> trees                     =   treeRecordings.keySet();
        int numTrees                        =   trees.size();
        int total                           =   0;
        
        for(Node tree : trees)
            total += treeRecordings.get(tree);

        return (double) total / (double) numTrees;
    }
    
    @Override
    public DefaultTableModel getMeasureModel()
    {
        DefaultTableModel model =   new DefaultTableModel();
        model.addColumn("Tree Source ID");
        model.addColumn("Authentic Tree");
        
        if(isTreeSizeMode())
        {
            model.addColumn("Tree Size");
            model.addColumn("Max Tree Size");
            model.addColumn("Average Tree Size");
        }
        
        if(isTreeHeightMode())
        {
            model.addColumn("Tree Height");
            model.addColumn("Max Tree Height");
            model.addColumn("Average Tree Height");
        }
        
        Set<Node> treeNodes =   isTreeSizeMode()? treeSizes.keySet() : maxTreeHeights.keySet();
        Iterator<Node> it   =   treeNodes.iterator();
        
        for(int i = 0; it.hasNext(); i++)
        {
            List rowList                =   new ArrayList();
            InfluenceAgent nextAgent    =   (InfluenceAgent) it.next();
            int ID                      =   nextAgent.getID();
            boolean authentic           =   nextAgent.isAuthentic();
            
            rowList.add(ID);
            rowList.add(authentic);
            
            if(isTreeSizeMode())
            {
                int treeSize            =   treeSizes.get(nextAgent);
                rowList.add(treeSize);
                
                int maxTreeSize         =   i == 0? getMaxTreeSize() : 0;
                double averageTreeSize  =   i == 0? getAverage(false) : 0.0;
                
                rowList.add(maxTreeSize);
                rowList.add(averageTreeSize);
            }
            
            if(isTreeHeightMode())
            {
                int treeHeight      =   maxTreeHeights.get(nextAgent);
                rowList.add(treeHeight);
                
                int maxTreeHeight           =   i == 0? getMaxTreeHeight() : 0;
                double averageTreeHeight    =   i == 0? getAverage(true) : 0;
                
                rowList.add(maxTreeHeight);
                rowList.add(averageTreeHeight);
            }
            
            model.addRow(rowList.toArray());
        }
        
        return model;
    }
    
    public int getMaxTreeHeight()
    {
        if(maxTreeHeightNode != null)
            return maxTreeHeights.get(maxTreeHeightNode);
        
        else return 0;
    }
    
    public int getMaxTreeSize()
    {
        if(maxTreeSizeNode != null)
            return treeSizes.get(maxTreeSizeNode);
        
        else return 0;
    }
    
    public Map<Node, Integer> getMaxTreeHeights()
    {
        return maxTreeHeights;
    }

    public Map<Node, Integer> getTreeSizes() 
    {
        return treeSizes;
    }

    public Node getMaxTreeSizeNode() 
    {
        return maxTreeSizeNode;
    }

    public Node getMaxTreeHeightNode() 
    {
        return maxTreeHeightNode;
    }
    
    public boolean isTreeHeightMode()
    {
        return recordMode == RECORD_TREE_HEIGHT || recordMode == RECORD_BOTH_MODE;
    }
    
    public boolean isTreeSizeMode()
    {
        return recordMode == RECORD_TREE_SIZE || recordMode == RECORD_BOTH_MODE;
    }
}
