//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network;

import com.graphi.graph.Edge;
import com.graphi.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkSeeder 
{
    public static final int RANDOM_NODE_SEED    =   0;
    public static final int RANDOM_FRIEND_SEED  =   1;
    public static final Color AUTH_COLOUR       =   Color.GREEN;
    public static final Color UNAUTH_COLOUR     =   Color.RED;
    
    private Set<Node> seeds;
    private int seedMethod;
    private double seedPercent;
    private boolean authenticityMode;
    private double authenticPercent;
    private boolean colourAuth;
    private boolean colourInfluence;
    
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
        colourAuth              =   false;
        colourInfluence         =   false;
    }
    
    public void generateSeeds(Map<Integer, Node> nodes, Graph<Node, Edge> graph)
    {
        int seedSize    =   (int) (nodes.size() * seedPercent);
        
        if(seedMethod == RANDOM_FRIEND_SEED) 
            seedSize     =   seedSize / 2;
        
        if(seedMethod == RANDOM_NODE_SEED)
            randomNodeMethod(nodes, seedSize);

        else if(seedMethod == RANDOM_FRIEND_SEED)
            addRandomNeighbour(nodes, graph, seedSize);
    }
    
    public void randomNodeMethod(Map<Integer, Node> nodes, int seedSize)
    {
        List<Integer> nodesCopy =   new LinkedList<>(nodes.keySet());
        Collections.shuffle(nodesCopy);
        
        for(int i = 0; i < seedSize; i++)
            seeds.add(nodes.get(nodesCopy.get(i)));
    }
    
    public void addRandomNeighbour(Map<Integer, Node> nodes, Graph<Node, Edge> graph, int seedSize)
    {
        List<Integer> nodesCopy =   new LinkedList<>(nodes.keySet());
        int expectedSeedSize    =   seedSize * 2;
        Collections.shuffle(nodesCopy);
        
        for(int i = 0; i < nodes.size() && seeds.size() < expectedSeedSize; i++)
        {
            Node randomNode          =   nodes.get(nodesCopy.get(i));
            List<Node> neighbours    =   new ArrayList<>(graph.getNeighbors(randomNode));
            Collections.shuffle(neighbours);
            
            if(!seeds.contains(randomNode))
            {
                seeds.add(randomNode);
            
                for(int j = 0; j < neighbours.size(); j++)
                {
                    Node randomNeighbour    =   neighbours.get(j);
                    if(!seeds.contains(randomNeighbour))
                    {
                        seeds.add(randomNeighbour);
                        break;
                    }
                }
            }
        }
    }
    
    public void runSeedInfluenceTransform()
    {
        for(Node seed : seeds)
        {
            InfluenceAgent agentSeed    =   (InfluenceAgent) seed;
            agentSeed.setInfluenced(true);
            
            if(colourInfluence)
                agentSeed.setFill(generateRandomSeedColour());
        }
    }
    
    public void runSeedAuthenticityTransform()
    {
        if(authenticityMode)
        {
            double falsePercent     =   1.0 - authenticPercent;
            int falseSize           =   (int) (seeds.size() * falsePercent);
            List<Node> seedsCopy    =   new ArrayList<>(seeds);
            int i                   =   0;
            Collections.shuffle(seedsCopy);
            
            for(i = 0; i < falseSize; i++)
            {
                InfluenceAgent agentSeed    =   (InfluenceAgent) seedsCopy.get(i);
                agentSeed.setAuthentic(false);
                
                if(colourAuth)
                    agentSeed.setFill(UNAUTH_COLOUR);
            }
            
            if(colourAuth)
            {
                for(; i < seedsCopy.size(); i++)
                    seedsCopy.get(i).setFill(AUTH_COLOUR);
            }
        }
    }
    
    public Color generateRandomSeedColour()
    {
        double hue      =   Math.random();
        int rgb         =   java.awt.Color.HSBtoRGB((float) hue, 0.5f, 0.5f);
        Color colour    =   new Color(rgb);
        
        return colour;
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
    
    public void resetSeeds()
    {
        seeds.clear();
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
    
    public boolean isColourAuth() 
    {
        return colourAuth;
    }

    public void setColourAuth(boolean colourAuth) 
    {
        this.colourAuth = colourAuth;
    }

    public boolean isColourInfluence()
    {
        return colourInfluence;
    }

    public void setColourInfluence(boolean colourInfluence) 
    {
        this.colourInfluence = colourInfluence;
    }
}
