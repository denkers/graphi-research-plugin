//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections15.Factory;

public class MutualNeighbourModel
{
    public static void transformInfluenceNetwork(Graph<Node, Edge> underlyingGraph)
    {
        Collection<Edge> edges              =   underlyingGraph.getEdges();
        Factory<Edge> edgeFactory           =   () -> new Edge();
        
        
        for(Edge edge : edges)
        {
            Collection<Node> incidentVertices     =   underlyingGraph.getIncidentVertices(edge);
            if(incidentVertices.size() == 2)
            {
                Iterator<Node> vertexIterator   =   incidentVertices.iterator();
                InfluenceAgent agentA           =   (InfluenceAgent) vertexIterator.next();
                InfluenceAgent agentB           =   (InfluenceAgent) vertexIterator.next();
                double influenceAonB            =   computeInfluence(agentA, agentB, underlyingGraph);
                double influenceBonA            =   computeInfluence(agentB, agentA, underlyingGraph);
                Edge edgeAtoB                   =   edgeFactory.create();
                Edge edgeBtoA                   =   edgeFactory.create();
                
                edgeAtoB.setWeight(influenceAonB);
                edgeBtoA.setWeight(influenceBonA);
                
                underlyingGraph.removeEdge(edge);
                underlyingGraph.addEdge(edgeAtoB, agentA, agentB, EdgeType.DIRECTED);
                underlyingGraph.addEdge(edgeBtoA, agentB, agentA, EdgeType.DIRECTED);
            }
        }
    }
    
    public static double computeInfluence(InfluenceAgent influencer, InfluenceAgent target, Graph<Node, Edge> graph)
    {
        double influence            =   0.0; //influence value that the @influencer node has on the @target node
        int targetDegree            =   graph.degree(target); //denominator in the influence ratio
        int intersectionSize        =   1; //size of the intersection of the mutual neighbour set of @influencer & @target nodes
        
        
        Collection<Node> influencerNeighbours   =   graph.getNeighbors(influencer);
        Collection<Node> targetNeighbours       =   graph.getNeighbors(target);
        Collection<Node> listNeighbours, checkNeighbours;
        
        //Choose smallest neighbour collection to cycle through
        if(influencerNeighbours.size() > targetNeighbours.size())
        {
            listNeighbours  =   targetNeighbours;
            checkNeighbours =   influencerNeighbours;
        }
        
        else
        {
            listNeighbours  =   influencerNeighbours;
            checkNeighbours =   targetNeighbours;
        }
        
        for(Node neighbour : listNeighbours)
        {
            if(checkNeighbours.contains(neighbour))
                intersectionSize++;
        }
        
        influence   =   (double) intersectionSize / (double) targetDegree;
        return influence;
    }
}
