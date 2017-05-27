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

public class CycleGraphGenerator extends AbstractGenerator
{
    private int numNodes;
    
    public CycleGraphGenerator()
    {
        this(3);
    }
    
    public CycleGraphGenerator(int n)
    {
        numNodes    =   n;
    }

    @Override
    protected void initGeneratorDetails() 
    {
        generatorName   =   "Cycle Graph";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory) 
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        Graph<Node, Edge> graph                     =   new SparseMultigraph<>();
        
        Node tailNode       =   nodeFactory.create();
        Node prevNode       =   tailNode;
        Node currentNode    =   null;
        
        graph.addVertex(tailNode);
        
        for(int i = 1; i < numNodes; i++)
        {
            currentNode    =   nodeFactory.create();
            graph.addVertex(currentNode);
            graph.addEdge(edgeFactory.create(), currentNode, prevNode);
            prevNode       =   currentNode;
        }
        
        if(currentNode != null) graph.addEdge(edgeFactory.create(), currentNode, tailNode);
        return graph;
    }
    
    public int getNumNodes()
    {
        return numNodes;
    }
    
    public void setNumNodes(int n)
    {
        numNodes    =   n;
    }
}
