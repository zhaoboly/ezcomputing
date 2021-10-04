# EZComputing.org is a javascript platform. 

We want to provide a platform to write server-side javascript codes, which can be distributed to the connected PCs for execution. The project consists of two parts: the server and workers. The server will provide Javascript compose and debug capabilities, also prepare javascript codes ready to be distributed to the connected workers. The worker will accept javascript codes from the server and execute it locally. 

We encourage everyone go to [ezcomputing.org](https://ezcomputing.org) to try this new platform in live.

Also our platform is in beta, welcome anyone like to contribute to this project. If you are interested, please email to zhaoboly@me.com.

## some requirements for the project

### 1. server-side javascript only
The javacript codes are running under Jaalvm engine, which is java virtual machine. It's not in any browser environment, so a lot cliet-side javascript functions are not supported, also there is no DOM object. 

### 2. Open source in mind
We want to encourage more people to donate their PC's computing power. So all javascript codes created in our plateform will be in open source domain.

some good resources used in the project:

[JsonStores.com](https://jsonstores.com)
