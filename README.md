=======================
## An Expert Recommender framework.
### UNDER DEVELOPMENT.

![alt text](https://github.com/rwth-acis/Recommendation-Service/blob/master/screenshot.png "Sample screenshot of a graph.")

Red node is the main hub in the network. 

Blue and Violet nodes shows the differnce in two algorithms namely Community aware HITS and regular HITS.
Motivation is to find not only hub nodes but to find useful nodes with awareness of community knowledge.

Test cases are failed currently as it is under modifications.

To build the project, clone the project and run "ant" command from the root directory of the project.

To start the LAS2Peer server, 
Run
sh bin/start_network.sh from the root of the project.

Currently, Framework is under development and cannot be used directly. 

Requirements:

MySql - For storing data.

Ant - For build system.
