//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Node;
import java.util.HashSet;
import java.util.Set;

public class NetworkSeeder 
{
    public static final int RANDOM_NODE_SEED    =   0;
    public static final int RANDOM_FRIEND_SEED  =   1;
    
    private Set<Node> seeds;
    private int seedMethod;
    private double seedPercent;
    private boolean authenticityMode;
    private double authenticPercent;
    
    public NetworkSeeder()
    {
        this(RANDOM_NODE_SEED, 0.1);
    }
    
    public NetworkSeeder(int seedMethod)
    {
        this(seedMethod, 0.1);
    }
    
    public NetworkSeeder(int seedMethod, double populationPercent)
    {
        this.seedMethod         =   seedMethod;
        this.seedPercent        =   populationPercent;
        seeds                   =   new HashSet<>();
        authenticityMode        =   false;
        authenticPercent        =   0.0;
    }
    
    
    public void enableAuthenticityMode(double authenticPercent)
    {
        authenticityMode            =   true;
        this.authenticPercent       =   authenticPercent;
    }
    
    public void disableAuthenticityMode()
    {
        authenticityMode    =   false;
    }

    public Set<Node> getSeeds()
    {
        return seeds;
    }

    public int getSeedMethod()
    {
        return seedMethod;
    }

    public double getSeedPercent() 
    {
        return seedPercent;
    }
    
    public int getSeedCount()
    {
        return seeds.size();
    }

    public boolean isAuthenticityMode()
    {
        return authenticityMode;
    }

    public double getAuthenticPercent()
    {
        return authenticPercent;
    }
}
