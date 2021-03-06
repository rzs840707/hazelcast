## Enterprise WAN Replication Configuration

![](images/enterprise-onlycopy.jpg)


The following are example Enterprise WAN replication configurations.

**Declarative Configuration:**

```xml
<wan-replication name="my-wan-cluster">
   <target-cluster group-name="tokyo" group-password="tokyo-pass">
      <replication-impl>com.hazelcast.enterprise.wan.replication.WanNoDelayReplication</replication-impl>
      <end-points>
         <address>10.2.1.1:5701</address>
         <address>10.2.1.2:5701</address>
      </end-points> 
   </target-cluster>
</wan-replication>
<wan-replication name="my-wan-cluster-batch" snapshot-enabled="false">
   <target-cluster group-name="london" group-password="london-pass">
      <replication-impl>com.hazelcast.enterprise.wan.replication.WanBatchReplication</replication-impl>
      <end-points>
         <address>10.3.5.1:5701</address>
         <address>10.3.5.2:5701</address>
      </end-points>
   </target-cluster>
</wan-replication>
```

**Programmatic Configuration:**

```java
Config config = new Config();

//No delay replication config
WanReplicationConfig wrConfig = new WanReplicationConfig();
WanTargetClusterConfig  wtcConfig = wrConfig.getWanTargetClusterConfig();

wrConfig.setName("my-wan-cluster");
wtcConfig.setGroupName("tokyo").setGroupPassword("tokyo-pass");
wtcConfig.setReplicationImplObject("com.hazelcast.enterprise.wan.replication.WanNoDelayReplication");
config.addWanReplicationConfig(wrConfig);

//Batch Replication Config
WanReplicationConfig wrConfig = new WanReplicationConfig();
WanTargetClusterConfig  wtcConfig = wrConfig.getWanTargetClusterConfig();

wrConfig.setName("my-wan-cluster-batch");
wrConfig.setSnapshotEnabled(false);
wtcConfig.setGroupName("london").setGroupPassword("london");
wtcConfig.setReplicationImplObject("com.hazelcast.enterprise.wan.replication.WanBatchReplication");
config.addWanReplicationConfig(wrConfig);
```

Enterprise WAN replication configuration has the following elements.

- `name`: Name for your WAN replication configuration.
- `snapshot-enabled`: Only valid when used with `WanBatchReplication`. When set to `true`, only the latest events (based on key) are selected and sent in a batch. 
- `target-cluster`: Creates a group and its password.
- `replication-impl`: Name of the class implementation for the Enterprise WAN replication.
- `end-points`: IP addresses of the cluster members for which the Enterprise WAN replication is implemented.

### IMap and ICache WAN Configuration

To enable WAN replication for an IMap or ICache instance, you can use `wan-replication-ref` element. 
Each IMap and ICache instance can have different WAN replication configurations.

**Declarative Configuration:**

```xml
<wan-replication name="my-wan-cluster">
   ...
</wan-replication>
<map name="testMap">
 <wan-replication-ref name="testWanRef">
    <merge-policy>TestMergePolicy</merge-policy>
    <republishing-enabled>false</republishing-enabled>
 </wan-replication-ref>
</map>
```

**Programmatic Configuration:**

```java
Config config = new Config();

WanReplicationConfig wrConfig = new WanReplicationConfig();
WanTargetClusterConfig  wtcConfig = wrConfig.getWanTargetClusterConfig();

wrConfig.setName("my-wan-cluster");
...
config.addWanReplicationConfig(wrConfig);

WanReplicationRef wanRef = new WanReplicationRef();
wanRef.setName("my-wan-cluster");
wanRef.setMergePolicy(PassThroughMergePolicy.class.getName());
wanRef.setRepublishingEnabled(true);
config.getMapConfig("testMap").setWanReplicationRef(wanRef);
```

`wan-replication-ref` has the following elements;

- `name`: Name of `wan-replication` configuration. IMap or ICache instance uses this `wan-replication` config. Please refer to the [Enterprise WAN Replication Configuration section](#enterprise-wan-replication-configuration) for details about `wan-replication` configuration.
- `merge-policy`: Resolve conflicts that are occurred when target cluster already has the replicated entry key.
- `republishing-enabled`: When enabled, an incoming event to a member is forwarded to target cluster of that member.
 

