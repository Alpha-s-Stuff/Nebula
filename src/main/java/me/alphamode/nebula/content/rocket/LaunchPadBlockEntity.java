package me.alphamode.nebula.content.rocket;

import me.alphamode.nebula.content.AllBlockEntities;
import me.alphamode.star.util.NbtUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class LaunchPadBlockEntity extends BlockEntity {

    protected boolean isController;
    protected BlockPos controllerPos;

    public LaunchPadBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.LAUNCH_PAD, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        isController = nbt.getBoolean("isController");
        if(isController)
            controllerPos = NbtUtil.fromNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("isController", isController);
        if(isController)
            NbtUtil.toNbt(controllerPos, nbt);
    }
}
