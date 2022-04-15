package me.alphamode.nebula.content.condensor;

import me.alphamode.nebula.content.AllBlockEntities;
import me.alphamode.nebula.content.planets.AtmosphereStorage;
import me.alphamode.nebula.content.planets.Planet;
import me.alphamode.nebula.content.planets.PlanetManager;
import me.alphamode.star.world.block.entity.FluidTankBlockEntity;
import me.alphamode.star.world.block.entity.TickableBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class CondenserBlockEntity extends FluidTankBlockEntity implements TickableBlockEntity, ExtendedScreenHandlerFactory {

    private RegistryKey<World> DIMENSION;
    private FluidVariant selectedGas = FluidVariant.blank();

    public CondenserBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.CONDENSER, pos, state, FluidConstants.BUCKET);
    }

    @Override
    public void tick() {
        if(world == null) return;
        Planet planet = PlanetManager.getPlanet(world);
        if(planet == null) return;
        // Attempt to find a selected gas
        if(selectedGas.isBlank()) {
            try(Transaction t = Transaction.openOuter()) {
                for (StorageView<FluidVariant> view : planet.atmosphereGases().iterable(t)) {
                    if (view.isResourceBlank())
                        continue;
                    selectedGas = view.getResource();
                    break;
                }
            }
        }
        // Gas can still be blank
        if(!selectedGas.isBlank()) {
            try (Transaction t = Transaction.openOuter()) {
                long extracted = planet.atmosphereGases().extract(selectedGas, FluidConstants.NUGGET, t);
                tank.insert(selectedGas, extracted, t);
                t.commit();
            }
        }
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        if(Objects.requireNonNull(PlanetManager.getPlanet(player.getWorld())).atmosphereGases() instanceof AtmosphereStorage atmosphereStorage)
            atmosphereStorage.toBuffer(buf);
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Condenser");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CondenserScreenHandler(syncId, inv, tank, Objects.requireNonNull(PlanetManager.getPlanet(player.getWorld())).atmosphereGases());
    }


}
