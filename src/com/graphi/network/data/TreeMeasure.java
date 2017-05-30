//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.RankingAgent;
import java.util.Map;
import java.util.Set;

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
            
            if(recordMode == RECORD_TREE_HEIGHT || recordMode == RECORD_BOTH_MODE)
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
            
            else if(recordMode == RECORD_TREE_SIZE || recordMode == RECORD_BOTH_MODE)
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
}
