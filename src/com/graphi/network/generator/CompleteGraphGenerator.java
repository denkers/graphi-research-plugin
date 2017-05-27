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
import java.util.Collection;
import org.apache.commons.collections15.Factory;

public class CompleteGraphGenerator extends AbstractGenerator
{
    private int numNodes;
    
    public CompleteGraphGenerator()
    {
        this(2);
    }
    
    public CompleteGraphGenerator(int n)
    {
        numNodes    =   n;
    }

    @Override
    protected void initGeneratorDetails() 
    {
        generatorName   =   "Complete Graph";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory)
    {
        if(nodeFactory == null) nodeFactory         =   () -> new Node();
        if(edgeFactory == null) edgeFactory         =   () -> new Edge();
        Graph<Node, Edge> graph                     =   new SparseMultigraph<>();
        
        for(int i = 0; i < numNodes; i++)
        {
            Node nextNode   =   nodeFactory.create();
            
            if(i > 0)
            {
                Collection<Node> vertices   =   graph.getVertices();
                for(Node vertex : vertices)
                    graph.addEdge(edgeFactory.create(), nextNode, vertex);
            }
            
            graph.addVertex(nextNode);
        }
        
        return graph;
    }

    public int getNumNodes() 
    {
        return numNodes;
    }

    public void setNumNodes(int numNodes) 
    {
        this.numNodes = numNodes;
    }
}
