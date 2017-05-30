//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.network.data;

import javax.swing.table.DefaultTableModel;

public class AgentDataModel extends DefaultTableModel
{
    public AgentDataModel()
    {
        addColumn("Influenced");
        addColumn("Authentic");
        addColumn("Influencer ID");
    }
}
