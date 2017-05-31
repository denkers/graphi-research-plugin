//======================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-research-plugin
//======================================================

package com.graphi.research.plugin;

import com.graphi.network.DiffusionController;
import com.graphi.network.NetworkSeeder;
import com.graphi.network.data.AbstractMeasure;
import com.graphi.network.data.PopulationMeasure;
import com.graphi.network.data.TreeMeasure;
import com.graphi.network.rank.PolicyController;
import com.graphi.plugins.PluginManager;
import com.graphi.sim.generator.NetworkGenerator;
import com.graphi.tasks.MappedProperty;
import com.graphi.tasks.SimulateNetworkTask;

public class InitDiffusionControllerTask extends SimulateNetworkTask
{

    @Override
    public void initTaskDetails() 
    {
        setTaskName("Init Diffusion Controller");
    }

    @Override
    public void initDefaultProperties() 
    {
        //Generator
        MappedProperty genProp =   new MappedProperty();
        genProp.setName("kleinberg");
        genProp.setParamValue("latSize", "15");
        genProp.setParamValue("exp", "2");
        setProperty("Generator", genProp.toString());
        
        //Seeding
        MappedProperty seedProp =   new MappedProperty();
        seedProp.setName("networkSeeder");
        seedProp.setParamValue("method", "0");
        seedProp.setParamValue("seedPerc", "0.1");
        seedProp.setParamValue("authMode", "false");
        seedProp.setParamValue("authPerc", "1.0");
        seedProp.setParamValue("colourAuth", "false");
        seedProp.setParamValue("colourInfl", "false");
        setProperty("Seeding", seedProp.toString());
        
        //Measure
        MappedProperty measureProp  =   new MappedProperty();
        seedProp.setName("treeMeasure");
        seedProp.setParamValue("recordMode", "0");
        seedProp.setParamValue("popSize", "-1");
        setProperty("Measure", measureProp.toString());
        
        //Policy
        MappedProperty policyProp   =   new MappedProperty();
        seedProp.setName("PolicyController");
        seedProp.setParamValue("enable", "false");
        seedProp.setParamValue("policyMode", "0");
        setProperty("Policy", policyProp.toString());
        
        //Diffusion
        MappedProperty diffusionProp    =   new MappedProperty();
        diffusionProp.setName("DiffusionController");
        diffusionProp.setParamValue("diffusionMode", "0");
        diffusionProp.setParamValue("maxUnits", "10");
        diffusionProp.setParamValue("agentType", "0");
        setProperty("Diffusion", diffusionProp.toString());
    }

    @Override
    public void performTask() 
    {
        ResearchPlugin plugin                       =   (ResearchPlugin) PluginManager.getInstance().getActivePlugin();
        DiffusionController diffusionController     =   getDiffusionController();
        
        plugin.setDiffusionController(diffusionController);
        diffusionController.initDiffusion();
    }
    
    public DiffusionController getDiffusionController()
    {
        String diffusionStr             =   (String) getProperty("Diffusion");
        MappedProperty diffusionProp    =   new MappedProperty(diffusionStr);
        
        int diffusionMode           =   diffusionProp.getIntParamValue("diffusionMode");
        int maxUnits                =   diffusionProp.getIntParamValue("maxUnits");
        int agentType               =   diffusionProp.getIntParamValue("agentType");
        
        NetworkGenerator generator  =   getGenerator();
        PolicyController policy     =   getPolicy();
        AbstractMeasure measure     =   getMeasure();
        NetworkSeeder seeder        =   getSeeding();
        
        DiffusionController diffController  =   new DiffusionController(generator, seeder, diffusionMode);
        diffController.setPolicyController(policy);
        diffController.setMeasure(measure);
        diffController.setMaxUnits(maxUnits);
        
        if(agentType == 0)
            diffController.initInfluenceAgentManipulators();
        
        else if(agentType == 1)
            policy.initRankingAgentManipulators();
        
        return diffController;
    }
    
    
    public NetworkGenerator getGenerator()
    {
        String genAlgorithmStr  =   (String) getProperty("Generator");
        MappedProperty genProp  =   new MappedProperty(genAlgorithmStr);
        String genName          =   genProp.getName();
        NetworkGenerator gen;
        
        switch(genName)
        {
            case "berbasi": gen     =   getBASim(genProp); break;
            case "kleinberg": gen   =   getKleinbergSim(genProp); break;
            case "random": gen      =   getRASim(genProp); break;
            default: gen = null;
        }
        
        return gen;
    }
    
    public AbstractMeasure getMeasure()
    {
        String measureStr           =   (String) getProperty("Measure");
        MappedProperty measureProp  =   new MappedProperty(measureStr);
        String measureName          =   measureProp.getName();
        int populationSize          =   measureProp.getIntParamValue("popSize");
        int recordMode              =   measureProp.getIntParamValue("recordMode");
        
        if(measureName.equalsIgnoreCase("treeMeasure"))
            return new TreeMeasure(populationSize, recordMode);
        
        else if(measureName.equalsIgnoreCase("populationMeasure"))
            return new PopulationMeasure(populationSize, recordMode);
        
        else return null;
    }
    
    public NetworkSeeder getSeeding()
    {
        String seedingStr           =   (String) getProperty("Seeding");
        MappedProperty seedingProp  =   new MappedProperty(seedingStr);
        
        int seedMethod              =   seedingProp.getIntParamValue("method");
        double seedPerc             =   seedingProp.getDoubleParamValue("seedPerc");
        boolean authMode            =   seedingProp.getBoolParamValue("authMode");
        double authPerc             =   seedingProp.getDoubleParamValue("authPerc");
        boolean colourAuth          =   seedingProp.getBoolParamValue("colourAuth");
        boolean colourInfluence     =   seedingProp.getBoolParamValue("colourInfl");
        
        NetworkSeeder seeder        =   new NetworkSeeder(seedMethod, seedPerc);
        seeder.setColourAuth(colourAuth);
        seeder.setColourInfluence(colourInfluence);
        
        if(authMode)
            seeder.enableAuthenticityMode(authPerc);
        
        return seeder;
    }
    
    public PolicyController getPolicy()
    {
        String policyStr            =   (String) getProperty("Policy");
        MappedProperty policyProp   =   new MappedProperty(policyStr);
        
        boolean enablePolicy        =   policyProp.getBoolParamValue("enable");
        
        if(!enablePolicy) return null;
        else
        {
            int policyMode  =   policyProp.getIntParamValue("policyMode");
            return new PolicyController(policyMode);
        }
    }
}
