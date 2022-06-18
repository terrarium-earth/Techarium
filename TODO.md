- [ ] reimplement gui & container
  - does every deploying machine have a gui ?
- [x] fix particle of slaves
- [x] move be/mbs packages
- [x] save master position in nbt so it's here after reload
- [ ] fix block model xray
- [x] when a deployed block is removed, do the thing adrian said https://discord.com/channels/880995984426020885/980249463652225075/983159642425262141
- [ ] display the outline of obstructing blocks.
- [ ] be able to place a self-deploying block where it can't deploy. it should be in an "undeployed" state
- [ ] energy/fluid/gas input output (should defer to modloader)
- [ ] block harvest levels
- [ ] self-deploying block (or slave) removed in creative doesn't remove the other blocks of the machine.

## Fabric
- [x] replace geckolib-core with its full implementation when on 1.19 and add the models/renderers


# Notes

### On removing block

SelfDeployingBlock :
- remove itself then the slaves

SelfDeployingSlaveBlock :
- if block at master pos is good, proxy removal to it
- else remove itself

### On displaying an overlay of the obstructing blocks

(tested only in forge)
right now, there is an overlay, but it is limited : the overlay is displayed forever.  
I wish it would stay ~1 second and then disappear. It would be shown again if the player right click the core.

The code is ugly cause I need somehow to have the PoseStack of the rendering thread, I can't just create a new one.
So I store every position where there is a core obstructed to be rendered in a static list (I don't like that).

I tried to create a timer that save the last clicked time and with a check `System.currentTimeMillis() - timer` remove the core pos from the list.
Unfortunately the timer is not synced to the client (or server, didn't check which side) and the timer isn't reset after a click.
So this fucks everything cause the player can't reset it. (And I don't want to send a packet every second to say "hey reset timer pls").

So, well, this should be looked at in the future.
