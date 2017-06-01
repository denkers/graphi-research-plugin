//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.rank;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.util.Comparator;

public class DegreeCentralityComparator implements Comparator<Node>
{
    private Graph<Node, Edge> graph;
    
    public DegreeCentralityComparator(Graph<Node, Edge> graph)
    {
        this.graph  =   graph;
    }

    @Override
    public int compare(Node nodeA, Node nodeB)
    {
        int degreeA     =   graph.degree(nodeA);
        int degreeB     =   graph.degree(nodeB);
        
        if(degreeA > degreeB) return -1;
        else if(degreeA < degreeB) return 1;
        else return 0;
    }
}
