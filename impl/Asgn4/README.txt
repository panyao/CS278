In Android, activity and service exists in different processes. In order to exchange data between different processes, Android has implemented several mechanisms for interprocedure communication(IPC). Binder is one mechanism among them. It is a lightweight IPC. Some core class include Binder, Service, Service Manager, etc.  The idea is to make the call to service in another process like the call in local by proxying and marshalling.

Binders are entitiy which allow activity to obtain a reference to service and directly invoke methods on the service.

BpBinder are proxy in client. BnService are native in service. They have the same interface and are generated automatically by AIDL.

AIDL (Android Interface Definition Language) allows you to define the programming interface that both the client and service agree upon in order to communicate with each other using interprocess communication (IPC).?

Service Manager maintains a list of registered services and handler to them. It has two public method addService and checkService.

Parcel: Container for a message (data and object references) that can be sent through an IBinder. Used to marshaling and demarshalling an object.
