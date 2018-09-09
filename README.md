# Babble: PennAppsXVIII

PennApps Project Fall 2018

## Babble: World's First and Only 100% Offline Chat Platform

Babble is the world's first and only chat platform that is able to be installed, setup, and used 100% offline. This platform has a wide variety of use cases such as use in communities with limited internet access like North Korea, Cuba, and Somalia. Additionally, this platform would be able to maintain communications in disaster situations where internet infrastructure is damaged or sabotaged. ex. Warzones, Natural Disasters, etc. 

## Offline Install & Setup

Babble (a zipped APK) is able to be sent from one user to another via Android Beam. From there it is able to be installed. This allows any user to install the app just by tapping their phone to that of another user. This can be done 100% offline.

## Offline Send 

All Babble users connect to all nearby devices via the creation of a localized mesh network created using the Android Nearby Connections API. This allows for messages to be sent directly from device to device via m to n peer to peer as well as messages to be daisy chain sent from peer to peer to ... to peer to peer. 

Each Babble user's device keeps a localized ledger of all messages that it has sent and received, as well as an amalgamation of all of the ledgers of every device that this instance of Babble has been connected directly to via Android Nearby. 

The combination of the Android Nearby Connections API with this decentralized, distributed ledger allows for messages to propagate across mesh networks and move between isolated networks as users leave one mesh network and join another. 

## Cloud Sync when Online

Whenever an instance of Babble gains internet access, it uploads a copy of its ledger to a MongoDB Atlas Cluster running on Google Cloud. There the local ledger is amalgamated with the global ledger which contains all messages sent world wide. From there the local copy of the ledger is updated from the global copy to contain messages for nearby users. 


## Use Cases

### Internet Infrastructure Failure: Natural Disaster

Imagine a natural disaster situation where large scale internet infrastructure is destroyed or otherwise not working correctly. Only a small number of users of the app would be able to distribute the app to all those affected by the outage and allow them to communicate with loved ones and emergency services. Additionally, this would provide a platform by which emergency services would be able to issue public alerts to the entire mesh network. 

### Untraceable and Unrestrictable Communication in North Korea

One of the future directions we would like to take this would be a Ethereum-esq blockchain based ledger. This would allow for 100% secure, private, and untraceable messaging. Additionally the Android Nearby Connections API is able to communicate between devices via, cellular network, Wifi, Bluetooth, NFC, and Ultrasound which makes our messages relatively immune to jamming. With the mesh network, it would be difficult to block messaging on a large scale.

As a result of this feature set, Babble would be a perfect app to allow for open and unobstructed, censored, or otherwise unrestricted communication inside of a country with heavily restricted internet access like North Korea. 

### Allowing Cubans to Communicate with Family and Friends in the US

Take a use case of a Cuba wide roll out. There will be a limited number of users in large cities like Havana or Santiago de Cuba that will have internet access as well as a number of users distributed across the country who will have occasional internet access. Through both the offline send and the cloud sync, 100% offline users in cuba would be able to communicate with family stateside.

## Future Goals and Directions

Our future goals would be to build better stability and more features such as image and file sharing, emergency messaging, integration with emergency services and the 911 decision tree, end to end encryption, better ledger management, and conversion of ledger to Ethereum-esq anonymized blockchain to allow for 100% secure, private, and untraceable messaging.  

Ultimately, the most insane use of our platform would be as a method for rolling out low bandwidth internet to the offline world. 
