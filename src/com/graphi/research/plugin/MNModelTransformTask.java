//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.display.layout.GraphPanel;
import com.graphi.graph.Edge;
import com.graphi.graph.GraphData;
import com.graphi.graph.GraphDataManager;
import com.graphi.graph.Node;
import com.graphi.network.MutualNeighbourModel;
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
        
        Graph<Node, Edge> MNNetwork     =   MutualNeighbourModel.transformInfluenceNetwork(currentGraph, edgeFactory);
        graphData.setGraph(MNNetwork);
        GraphPanel.getInstance().reloadGraph();
    }
}
