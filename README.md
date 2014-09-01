TCA
===

This is the repository for the [Task Component Architecture](https://github.com/yamunamaccarana/TCA/wiki/TCA).

The work presented is motivated by the aim of simplifying the integration of robotic and information systems technology.

For this purpose, we created the Task Component Architecture, a framework for the implementation of SCA components that execute autonomously their service and that can be seamlessly integrated with robotic software control systems. 

Morover, we present an integration of TCA with [ROS](http://wiki.ros.org/) to manage a [Service Oriented Robot Control System](http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=6830911&url=http%3A%2F%2Fieeexplore.ieee.org%2Fxpls%2Fabs_all.jsp%3Farnumber%3D6830911).

**Structure**

• [TCAimpl](https://github.com/yamunamaccarana/TCA/tree/master/TCAimpl) contains the implementation of the TCA architecture;

• [ROSimpl](https://github.com/yamunamaccarana/TCA/tree/master/ROSimpl) contains the implementation of the ROSProxy, needed to integrate TCA with ROS;

• [Navigator](https://github.com/yamunamaccarana/TCA/tree/master/Navigator) contains a working example of a Navigator using the TCA-ROS architecture.
