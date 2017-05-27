//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.generator;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import com.graphi.sim.generator.AbstractGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import org.apache.commons.collections15.Factory;

public class WheelGraphGenerator extends AbstractGenerator
{
    private int numNodes;
    
    public WheelGraphGenerator()
    {
        this(4);
    }
    
    public WheelGraphGenerator(int numNodes)
    {
        this.numNodes   =   numNodes;
    }
    
    @Override
    protected void initGeneratorDetails() 
    {
        generatorName           =   "Wheel Graph";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        
        Graph<Node, Edge> graph =   new SparseMultigraph<>();   
        Node spokeNode          =   nodeFactory.create();
        Node prevNode           =   null;
        Node currentNode        =   null;
        Node tailNode           =   null;
        
        for(int i = 1; i < numNodes; i++)
        {
            currentNode =   nodeFactory.create();
            graph.addEdge(edgeFactory.create(), spokeNode, currentNode);
            
            if(prevNode != null)
                graph.addEdge(edgeFactory.create(), prevNode, currentNode);
            else
                tailNode    =   currentNode;
            
            prevNode    =   currentNode;
        }
        
        graph.addEdge(edgeFactory.create(), currentNode, tailNode);
        return graph;
    }

    public void setNumNodes(int numNodes) 
    {
        this.numNodes = numNodes;
    }
}
