//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

public class NetworkMeasureController
{
    public static final int INFLUENCE_PORTION_MODE  =   0;
    public static final int TREE_HEIGHT_MODE        =   1;
    public static final int TREE_SIZE_MODE          =   2;
    public static final int AUTH_MODE               =   3;
    
    private int measureMode;
    
    public NetworkMeasureController(int measureMode)
    {
        this.measureMode    =   measureMode;
    }

    public int getMeasureMode() 
    {
        return measureMode;
    }

    public void setMeasureMode(int measureMode) 
    {
        this.measureMode = measureMode;
    }
}
