//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.display.layout.DataPanel;
import com.graphi.display.layout.GraphPanel;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import com.graphi.network.MutualNeighbourModel;
import com.graphi.network.data.OverallMeasureComputation;
import com.graphi.tasks.AbstractTask;
import com.graphi.util.factory.EdgeFactory;
import edu.uci.ics.jung.graph.Graph;

public class MNModelTransformTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("MN Model Transform");
    }

    @Override
    public void initDefaultProperties()
    {
    }

    @Override
    public void performTask()
    {
        GraphData graphData             =   GraphDataManager.getGraphDataInstance();
        EdgeFactory edgeFactory         =   graphData.getEdgeFactory();
        Graph<Node, Edge> currentGraph  =   graphData.getGraph();
        int numNodes                    =   currentGraph.getVertexCount();
        int numEdges                    =   currentGraph.getEdgeCount();
        int total                       =   0;
        
        for(Node node : currentGraph.getVertices())
            total += currentGraph.degree(node);
        
        
        double density                  =   (double) (2.0 * numEdges) / (double) (numNodes * (numNodes - 1));
        double averageDegree            =   (double) (2.0 * numEdges) / (double) numNodes;
        
        System.out.println("Density: " + density);
        System.out.println("Average Degree: " + averageDegree);
        
      /*  Graph<Node, Edge> MNNetwork     =   MutualNeighbourModel.transformInfluenceNetwork(currentGraph, edgeFactory);
        graphData.setGraph(MNNetwork);
        GraphPanel.getInstance().reloadGraph();
        double avg  =   OverallMeasureComputation.getColumnAverage(DataPanel.getInstance().getEdgeDataModel(), 3, graphData.getGraph().getEdges().size());
        System.out.println("Average MN Weight: " + avg); */
    }
}
