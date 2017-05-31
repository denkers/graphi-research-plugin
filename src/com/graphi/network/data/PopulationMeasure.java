//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import com.graphi.graph.Node;
import com.graphi.network.InfluenceAgent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class PopulationMeasure extends AbstractMeasure
{
    public static final int RECORD_INFLUENCE_MODE   =   0;
    public static final int RECORD_AUTH_MODE        =   1;
    public static final int RECORD_BOTH_MODE        =   2;
    
    private Set<Node> influencedAgents;
    private Set<Node> unauthenticAgents;
    
    public PopulationMeasure()
    {
        this(0, 0);
    }
    
    public PopulationMeasure(int populationSize, int recordMode)
    {
        this(populationSize, new HashSet<>(), recordMode);
    }
    
    public PopulationMeasure(int populationSize, Set<Node> nodes, int recordMode)
    {
        this.populationSize             =   populationSize;
        this.recordMode                 =   recordMode;
        influencedAgents                =   nodes.isEmpty()? nodes : new HashSet<>(nodes);
        unauthenticAgents               =   new HashSet<>();
    }
    
    @Override
    public boolean addAgent(Node node)
    {
        if(node != null)
        {
            InfluenceAgent agent    =   (InfluenceAgent) node;
            if(!agent.isInfluenced()) return false;
            
            if(isInfluenceMode())
                influencedAgents.add(agent);
            
            if(isAuthMode() && !agent.isAuthentic())
                unauthenticAgents.add(agent);
                
            return true;
        }
        
        else return false;
    }
    
    @Override
    public DefaultTableModel getMeasureModel()
    {
        DefaultTableModel model     =   new DefaultTableModel();
        List<Double> dataList       =   new ArrayList<>();
        
        if(isInfluenceMode())
        {
            model.addColumn("Influenced Population Portion");
            dataList.add(computeInflencePopulationRatio());
        }
        
        if(isAuthMode())
        {
            model.addColumn("Unauthentic Population Portion");
            dataList.add(computeUnauthenticPopulationRatio());
        }
        
        model.addRow(dataList.toArray());
        return model;
    }
    
    public double computeUnauthenticPopulationRatio()
    {
        if(unauthenticAgents.isEmpty()) return 0.0;
        return (double) unauthenticAgents.size() / (double) populationSize;
    }
    
    public double computeInflencePopulationRatio()
    {
        if(influencedAgents.isEmpty()) return 0.0;
        return (double) influencedAgents.size() / (double) populationSize;
    }
    
    public int getInfluencedCount()
    {
        return influencedAgents.size();
    }
    
    public int getUnauthenticCount()
    {
        return unauthenticAgents.size();
    }

    public Set<Node> getInfluencedAgents() 
    {
        return influencedAgents;
    }

    public void setInfluencedNodes(Set<Node> influencedAgents) 
    {
        this.influencedAgents     =   influencedAgents;
    }

    public Set<Node> getUnauthenticAgents() 
    {
        return unauthenticAgents;
    }
    
    public boolean isInfluenceMode()
    {
        return recordMode == RECORD_INFLUENCE_MODE || recordMode == RECORD_BOTH_MODE;
    }
    
    public boolean isAuthMode()
    {
        return recordMode == RECORD_AUTH_MODE || recordMode == RECORD_BOTH_MODE;
    }
}
