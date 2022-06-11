- [ ] reimplement gui & container
  - does every deploying machine have a gui ?
- [x] fix particle of slaves
- [x] move be/mbs packages
- [ ] save master position in nbt so it's here after reload
- [ ] fix block model xray
- [x] when a deployed block is removed, do the thing adrian said https://discord.com/channels/880995984426020885/980249463652225075/983159642425262141
- [ ] display the outline of obstructing blocks.
- [ ] be able to place a self-deploying block where it can't deploy. it should be in an "undeployed" state


## Fabric
- [ ] replace geckolib-core with its full implementation when on 1.19 and add the models/renderers


# Notes

### on removing block

SelfDeployingBlock :
- remove itself then the slaves

SelfDeployingSlaveBlock :
- if block at master pos is good, proxy removal to it
- else remove itself