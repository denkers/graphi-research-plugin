//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.tasks.AbstractTask;
import com.graphi.tasks.MappedProperty;
import com.graphi.tasks.Task;
import com.graphi.tasks.TaskManager;
import java.util.List;

public class LazyRecorderTask extends AbstractTask
{
    @Override
    public void initTaskDetails() 
    {
        setTaskName("Lazy Recorder");
    }

    @Override
    public void initDefaultProperties() 
    {
    }

    /*
    Task Indexes:
    2: init diffusion controller
        0: polcicy
        1: Measure
        2: Diffusion
        3: Seeding
        4: Generator 
    3: run diffusion
    4: multi-state measuring
    5: export table
        0: table index
        1: file name
        2: directory
    6: export playback
        0: file name
        1: directory
    */
    
    @Override
    public void performTask() 
    {
        List<Task> tasks    =   TaskManager.getInstance().getTasks().getRepeatableTasks();
        
        for(int i = 100; i <= 1000; i+= 100)
        {
            setFiles("" + i, tasks);
            setDiffusionController(i, tasks);
            
            for(int j = 0; j < 3; j++)
            {
                for(int k = 0; k < tasks.size(); k++)
                    tasks.get(k).performTask();
            }
        }
    }
    
    public void setFiles(String dirIteration, List<Task> tasks)
    {
        final String expName    =   "Kleinberg";
        String rootDir          =   "/home/denker/Dropbox/FinalProjectGraphiTasks/experiments/";
        String currentDir       =   "FixedNodeInfluenceAllExperiments/" + expName + "/";   
        String dir              =   rootDir + currentDir + dirIteration + "/";
        String fileName         =   expName + dirIteration + "Node";
        
        tasks.get(5).setProperty("File name", fileName);
        tasks.get(5).setProperty("Directory", dir);
        tasks.get(6).setProperty("File name", fileName);
        tasks.get(6).setProperty("Directory", dir);
    }
    
    public void setDiffusionController(int iteration, List<Task> tasks)
    {
        MappedProperty genProperty  =   getKleinbergGen(iteration);
        tasks.get(2).setProperty("Generator", genProperty.toString());
    }
    
    public MappedProperty getRealGen()
    {
        MappedProperty genProp  =   new MappedProperty();
        genProp.setName("real");
        genProp.setParamValue("path", "/home/denker/Dropbox/FinalProjectGraphiTasks/graphs/facebook.edgelist");
        
        return genProp;
    }
    
    public MappedProperty getKleinbergGen(double iteration)
    {
        MappedProperty genProp  =   new MappedProperty();
        double sq               =   Math.sqrt(iteration);
        int latSize             =   sq % 1 == 0? (int) sq : (int) (sq + 1);
        
        genProp.setName("kleinberg");
        genProp.setParamValue("latSize", "" + latSize);
        genProp.setParamValue("exp", "" + 2);
        
        return genProp;
    }
    
    public MappedProperty getRandomGen(int iteration)
    {
        MappedProperty generatorProperty    =   new MappedProperty();
        generatorProperty.setName("random");
        generatorProperty.setParamValue("n", "" + iteration);
        generatorProperty.setParamValue("p", "0.1");
        generatorProperty.setParamValue("dir", "false");
        
        return generatorProperty;
    }
    
    public MappedProperty getBarbasiGen(int iteration)
    {
        MappedProperty generatorProperty    =   new MappedProperty();
        int numInitial  =   (int) (iteration * 0.05);
        int numNodes    =   iteration - numInitial;
        generatorProperty.setName("berbasi");
        generatorProperty.setParamValue("n", "" + numNodes);
        generatorProperty.setParamValue("i", "" + numInitial);
        generatorProperty.setParamValue("dir", "false");
        
        return generatorProperty;
    }
}
