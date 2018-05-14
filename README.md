<img src="preview/AppIcon.png" align="left" />

# graphi-diffusion Plugin

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

graphi-diffusion is a research driven plugin for [Graphi](https://github.com/kyleruss/graphi)  
The plugin simulates social network users that observe information sharing behaviour using an agent-based model  
Agents in the network are given decision making mechanisms where they can optimally choose which friend to pass information to as well as which friend to accept information. Agents can carry misinformation or factual information where they propogate this 
information and those that recieve it also endorse it and this information continues to diisseminate through the network. 
The plugin generates large random networks with information sharing agents where a set of the seeds carru misinformation, the agents 
then spread their information autonomously through the network and once complete, the plugin measures the proportion of misinformation 
and other diffusion behaviour measures such as propagation trees to observe the spread of a source


### [View Research Paper](ResearchPaper.pdf)
Russell, K. (2017). Reducing the Propagation of Misinformation in Social Networks with Agent-Based Modelling Methods

## Getting started

### Prerequisites
- JDK 1.8+ 
- [Graphi](https://github.com/kyleruss/graphi.git) v1.8.0+
- NetBeans 8.1+

### Installation
- Clone the graphi-diffusion-plugin repository
```
git clone https://github.com/kyleruss/graphi-diffusion-plugin.git
```

- Import the project into NetBeans
- Build the project in netbeans 
- Import the `graphi-diffusion-plugin.jar` plugin into Graphi

## License
graphi-diffusion-plugin is available under the MIT License  
See [LICENSE](LICENSE) for more details
