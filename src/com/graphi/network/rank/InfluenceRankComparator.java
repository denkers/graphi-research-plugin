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

public class InfluenceRankComparator implements Comparator<Node>
{
    private final Node perspectiveAgent;
    
    public InfluenceRankComparator(Node perspectiveAgent)
    {
        this.perspectiveAgent   =   perspectiveAgent;
    }
    
    public double getConnectionScore(double influenceProbability, int degree)
    {
        return degree * influenceProbability;
    }
    
    @Override
    public int compare(Node nodeA, Node nodeB) 
    {
        Graph<Node, Edge> graph =   GraphDataManager.getGraphDataInstance().getGraph();
        Edge edgeA              =   graph.findEdge(perspectiveAgent, nodeA);
        Edge edgeB              =   graph.findEdge(perspectiveAgent, nodeB);
        
        if(edgeA != null && edgeB != null)
        {
            double scoreA  =   getConnectionScore(edgeA.getWeight(), graph.degree(nodeA));
            double scoreB  =   getConnectionScore(edgeB.getWeight(), graph.degree(nodeB));
            
            if(scoreA > scoreB) return -1;
            else if(scoreA < scoreB) return 1;
            else return 0;
        }
        
        else return 0;
    }
}
