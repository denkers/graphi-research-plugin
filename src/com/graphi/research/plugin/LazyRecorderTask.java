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
        
        for(int i = 100; i < 1000; i++)
        {
            String iteration    =   "" + i;
            setFiles(iteration, tasks);
            setDiffusionController(iteration, tasks);
            
            for(int j = 0; j < 5; j++)
            {
                for(int k = 0; k < tasks.size(); k++)
                    tasks.get(k).performTask();
            }
        }
    }
    
    public void setFiles(String dirIteration, List<Task> tasks)
    {
        final String expName    =   "Line";
        String rootDir          =   "/home/denker/Dropbox/FinalProjectGraphiTasks/experiments/";
        String currentDir       =   "ClassicGeneratorExperiments/" + expName + "/";   
        String dir              =   rootDir + currentDir;
        String fileName         =   expName + dirIteration + "Node";
        
        tasks.get(5).setProperty("File name", fileName);
        tasks.get(5).setProperty("Directory", dir);
        tasks.get(6).setProperty("File name", fileName);
        tasks.get(6).setProperty("Directory", dir);
    }
    
    public void setDiffusionController(String iteration, List<Task> tasks)
    {
        MappedProperty generatorProperty    =   new MappedProperty();
        generatorProperty.setName("Line");
        generatorProperty.setParamValue("numNodes", iteration);
        
        tasks.get(2).setProperty("Generator", generatorProperty.toString());
    }
}
