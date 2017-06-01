//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Node;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import java.util.Comparator;

public class EigenvectorCentralityComparator implements Comparator<Node>
{
    private EigenvectorCentrality centrality;
    
    public EigenvectorCentralityComparator(EigenvectorCentrality centrality)
    {
        this.centrality     =   centrality;
    }

    @Override
    public int compare(Node nodeA, Node nodeB) 
    {
        double scoreA      =   (double) centrality.getVertexScore(nodeA);
        double scoreB      =   (double) centrality.getVertexScore(nodeB);
        
        if(scoreA > scoreB) return -1;
        else if(scoreA < scoreB) return 1;
        else return 0;
    }
}
