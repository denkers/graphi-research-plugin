//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Edge;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Comparator;

public class DegreeCentralityComparator implements Comparator<Node>
{
    @Override
    public int compare(Node nodeA, Node nodeB)
    {
        Graph<Node, Edge> graph =   GraphDataManager.getGraphDataInstance().getGraph();
        int degreeA             =   graph.inDegree(nodeA);
        int degreeB             =   graph.inDegree(nodeB);
        
        if(degreeA > degreeB) return -1;
        else if(degreeA < degreeB) return 1;
        else return 0;
    }
}
