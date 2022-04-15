package me.alphamode.nebula.content.condensor;

import me.alphamode.nebula.content.AllScreenHandlers;
import me.alphamode.nebula.content.planets.AtmosphereStorage;
import me.alphamode.star.screens.StarScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;

@SuppressWarnings("UnstableApiUsage")
public class CondenserScreenHandler extends StarScreenHandler {

    public final Storage<FluidVariant> tank, atmosphere;

    public CondenserScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, ((CondenserBlockEntity)MinecraftClient.getInstance().world.getBlockEntity(buf.readBlockPos())).getStorage(null), AtmosphereStorage.fromBuffer(buf));
    }

    public CondenserScreenHandler(int syncId, PlayerInventory inventory, Storage<FluidVariant> tank, Storage<FluidVariant> atmosphere) {
        super(AllScreenHandlers.CONDENSER, syncId);
        this.tank = tank;
        this.atmosphere = atmosphere;
        addPlayerInventory(inventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
