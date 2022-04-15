package me.alphamode.nebula.content.rocket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class RocketEntity extends Entity {

    private UUID ownerId;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if(ownerId != null)
            nbt.putUuid("owner", ownerId);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if(nbt.contains("owner"))
            ownerId = nbt.getUuid("owner");
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(ownerId == null) {
            ownerId = player.getUuid();
        }
        if(ownerId.equals(player.getUuid())) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
