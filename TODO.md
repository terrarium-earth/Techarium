- [ ] fix block model xray
- [ ] display the outline of obstructing blocks (and rework it (make it display for undeployed rather than multiblock
  can't deploy)).
- [ ] be able to place a self-deploying block where it can't deploy. it should be in an "undeployed" state
- [ ] energy/fluid/gas input output (should defer to modloader)
- [ ] block harvest levels
- [ ] self-deploying block (or component) removed in creative doesn't remove the other blocks of the machine.

- [ ] datagen

# Notes

### On displaying an overlay of the obstructing blocks

(tested only in forge)
right now, there is an overlay, but it is limited : the overlay is displayed forever.  
I wish it would stay ~1 second and then disappear. It would be shown again if the player right click the core.

The code is ugly cause I need somehow to have the PoseStack of the rendering thread, I can't just create a new one.
So I store every position where there is a core obstructed to be rendered in a static list (I don't like that).

I tried to create a timer that save the last clicked time and with a check `System.currentTimeMillis() - timer` remove
the core pos from the list.
Unfortunately the timer is not synced to the client (or server, didn't check which side) and the timer isn't reset after
a click.
So this fucks everything cause the player can't reset it. (And I don't want to send a packet every second to say "hey
reset timer pls").

So, well, this should be looked at in the future.


/*
adrian I'm going to need your help later (not today) with botarium, I'm getting a nullpointerexception with your AutoSerializable while trying to sync energy blocks.
`Cannot invoke "earth.terrarium.botarium.api.Serializable.serialize(net.minecraft.nbt.CompoundTag)" because the return value of "earth.terrarium.botarium.forge.AutoSerializable.getSerializable()" is null`
From what I understood, the getSerializable return the storage got from EnergyBlock#getEnergyStorage but it is somehow null and I'm sure the energy storage is never null in my code. might come from item or fluid storage but they are never null too.
I don't know what happening ;(
*/

https://fabricmc.net/wiki/tutorial:custom_resources
