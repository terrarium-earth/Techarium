package com.techarium.techarium.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface ExtraDataMenuProvider extends MenuProvider {
    void writeExtraData(ServerPlayer player, FriendlyByteBuf buffer);
}
