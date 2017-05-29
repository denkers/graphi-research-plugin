//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import com.graphi.sim.generator.NetworkGenerator;
import java.util.HashSet;
import java.util.Set;

public class DiffusionController 
{
    private Set<Node> finishedNodes;
    private NetworkSeeder seeder;
    private NetworkGenerator networkGenerator;
    private int timeUnit;
    
    public DiffusionController(NetworkGenerator networkGenerator)
    {
        this(networkGenerator, new NetworkSeeder());
    }

    public DiffusionController(NetworkGenerator networkGenerator, NetworkSeeder seeder)
    {
        this.networkGenerator   =   networkGenerator;
        this.seeder             =   seeder;
        timeUnit                =   0;
        finishedNodes           =   new HashSet<>();
    }
    
    public NetworkSeeder getSeeder() 
    {
        return seeder;
    }

    public void setSeeder(NetworkSeeder seeder) 
    {
        this.seeder = seeder;
    }

    public NetworkGenerator getNetworkGenerator()
    {
        return networkGenerator;
    }

    public void setNetworkGenerator(NetworkGenerator networkGenerator) 
    {
        this.networkGenerator = networkGenerator;
    }

    public Set<Node> getFinishedNodes()
    {
        return finishedNodes;
    }

    public int getTimeUnit() 
    {
        return timeUnit;
    }
}
