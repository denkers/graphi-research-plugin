//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.generator;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import com.graphi.io.EdgeListParser;
import com.graphi.sim.generator.AbstractGenerator;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import org.apache.commons.collections15.Factory;

public class RealDataGenerator extends AbstractGenerator
{
    private File dataFile;
    
    public RealDataGenerator()
    {
        this(null);
    }
    
    public RealDataGenerator(File dataFile)
    {
        this.dataFile   =   dataFile;
    }
    
    @Override
    protected void initGeneratorDetails() 
    {
        generatorName   =   "Real Network";
    }

    @Override
    public Graph<Node, Edge> generateNetwork(Factory<Node> nodeFactory, Factory<Edge> edgeFactory) 
    {
        return EdgeListParser.importGraph(dataFile, false, nodeFactory, edgeFactory);
    }

    public File getDataFile() 
    {
        return dataFile;
    }

    public void setDataFile(File dataFile) 
    {
        this.dataFile = dataFile;
    }
}
