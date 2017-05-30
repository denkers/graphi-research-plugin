//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.RankingAgent;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class TreeMeasure 
{
    public static final int RECORD_TREE_HEIGHT  =   0;
    public static final int RECORD_TREE_SIZE    =   1;
    public static final int RECORD_BOTH_MODE    =   2;
    
    private Map<Node, Integer> maxTreeHeights;
    private Map<Node, Integer> treeSizes;
    private Node maxTreeSizeNode, maxTreeHeightNode;
    private int populationSize;
    private int recordMode;
    
    public TreeMeasure()
    {
        this(0, RECORD_TREE_SIZE);
    }
    
    public TreeMeasure(int populationSize, int recordMode)
    {
        this.populationSize     =   populationSize;
        this.recordMode         =   recordMode;
        maxTreeSizeNode         =   null;
        maxTreeHeightNode       =   null;
    }
    
    public boolean addAgent(Node node)
    {
        if(node != null)
        {
            RankingAgent agent  =   (RankingAgent) node;
            
            if(isTreeHeightMode())
            {
                RankingAgent treeSource =   agent.getTreeRootAgent();
                int depth               =   agent.getInfluencedTreeDepth();
                
                if(!maxTreeHeights.containsKey(treeSource))
                    maxTreeHeights.put(treeSource, 0);
                
                else if(maxTreeHeights.get(treeSource) < depth)
                    maxTreeHeights.put(treeSource, depth);
                
                if(maxTreeHeightNode == null || (depth > maxTreeHeights.get(maxTreeHeightNode)))
                    maxTreeHeightNode   =   treeSource;
            }
            
            else if(isTreeSizeMode())
            {
                RankingAgent treeSource =   agent.getTreeRootAgent();
                int treeSize            =   treeSizes.containsKey(treeSource)? (treeSizes.get(treeSource) + 1) : 1;
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
    
    public DefaultTableModel getTreeSizeModel()
    {
        DefaultTableModel model =   new DefaultTableModel();
        model.addColumn("Tree Source ID");
        model.addColumn("Authentic Tree");
        model.addColumn("Tree Size");
        model.addColumn("Max Tree Size");
        model.addColumn("Average Tree Size");
        
        int maxTreeSize     =   getMaxTreeSize();
        double average      =   getAverage(false);
        Set<Node> treeNodes =   treeSizes.keySet();
        Iterator<Node> it   =   treeNodes.iterator();
        
        for(int i = 0; it.hasNext(); i++)
        {
            RankingAgent nextAgent  =   (RankingAgent) it.next();
            int ID                  =   nextAgent.getID();
            boolean authentic       =   nextAgent.isAuthentic();
            int treeSize            =   treeSizes.get(nextAgent);
            
            if(i > 0)
            {
                maxTreeSize     =   0;
                average         =   0;
            }
            
            model.addRow(new Object[] {ID, authentic, treeSize, maxTreeSize, average});
        }
        
        return model;
    }
    
    public DefaultTableModel getTreeHeightModel()
    {
        DefaultTableModel model =   new DefaultTableModel();
        model.addColumn("Tree Source ID");
        model.addColumn("Authentic Tree");
        model.addColumn("Tree Height");
        model.addColumn("Max Tree Height");
        model.addColumn("Average Tree Height");
        
        int maxTreeHeight   =   getMaxTreeHeight();
        double average      =   getAverage(true);
        Set<Node> treeNodes =   maxTreeHeights.keySet();
        Iterator<Node> it   =   treeNodes.iterator();
        
        for(int i = 0; it.hasNext(); i++)
        {
            RankingAgent nextAgent  =   (RankingAgent) it.next();
            int ID                  =   nextAgent.getID();
            boolean authentic       =   nextAgent.isAuthentic();
            int treeHeight          =   maxTreeHeights.get(nextAgent);
            
            if(i > 0)
            {
                maxTreeHeight   =   0;
                average         =   0;
            }
            
            model.addRow(new Object[] {ID, authentic, treeHeight, maxTreeHeight, average});
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
