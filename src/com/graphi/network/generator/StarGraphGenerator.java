//=========================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi
//=========================================

package com.graphi.network.generator;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import com.graphi.sim.generator.AbstractGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import org.apache.commons.collections15.Factory;

public class StarGraphGenerator extends AbstractGenerator
{
    private Node centerNode;
    private int numNodes;
    
    public StarGraphGenerator()
    {
        this(1);
    }
    
    public StarGraphGenerator(int n)
    {
        numNodes    =   n;
    }
    
    @Override
    protected void initGeneratorDetails() 
    {
        generatorName   =   "Star Graph";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        Graph<Node, Edge> graph     =   new SparseMultigraph<>();
        centerNode                  =   nodeFactory.create();
        graph.addVertex(centerNode);
        
        for(int i = 1; i < numNodes; i++)
        {
            Node nextNode   =   nodeFactory.create();
            graph.addVertex(nextNode);
            graph.addEdge(edgeFactory.create(), centerNode, nextNode);
        }
        
        return graph;
    }
    
    public int getNumNodes()
    {
        return numNodes;
    }
    
    public Node getCenterNode()
    {
        return centerNode;
    }
}
