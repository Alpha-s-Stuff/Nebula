package me.alphamode.nebula.content.planets;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings("UnstableApiUsage")
public class AtmosphereStorage implements ExtractionOnlyStorage<FluidVariant> {

    private final List<ResourceAmount<FluidVariant>> atmosphere;

    @SafeVarargs
    public AtmosphereStorage(ResourceAmount<FluidVariant>... atmosphere) {
        this.atmosphere = List.of(atmosphere);
    }

    public AtmosphereStorage(List<ResourceAmount<FluidVariant>> atmosphere) {
        this.atmosphere = atmosphere;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        for(ResourceAmount<FluidVariant> gas : atmosphere) {
            if(gas.resource() == resource) {
                return gas.amount();
            }
        }
        return 0;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator(TransactionContext transaction) {
        return new AtmosphereIterator();
    }

    public void toBuffer(PacketByteBuf buf) {
        buf.writeVarInt(atmosphere.size());
        atmosphere.forEach(resourceAmount -> {
            resourceAmount.resource().toPacket(buf);
            buf.writeLong(resourceAmount.amount());
        });
    }

    public static AtmosphereStorage fromBuffer(PacketByteBuf buf) {
        int size = buf.readVarInt();
        List<ResourceAmount<FluidVariant>> atmosphereGases = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FluidVariant fluid = FluidVariant.fromPacket(buf);
            atmosphereGases.add(new ResourceAmount<>(fluid, buf.readLong()));
        }
        return new AtmosphereStorage(atmosphereGases);
    }

    public static final class InternalView implements StorageView<FluidVariant> {
        private final ResourceAmount<FluidVariant> resource;

        public InternalView(ResourceAmount<FluidVariant> resource) {
            this.resource = resource;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return maxAmount;
        }

        @Override
        public boolean isResourceBlank() {
            return resource.resource().isBlank();
        }

        @Override
        public FluidVariant getResource() {
            return resource.resource();
        }

        @Override
        public long getAmount() {
            return resource.amount();
        }

        @Override
        public long getCapacity() {
            return 100;
        }
    }

    public class AtmosphereIterator implements Iterator<StorageView<FluidVariant>>, TransactionContext.CloseCallback {
        private final List<StorageView<FluidVariant>> fluids;
        private boolean open = true;
        private int index = 0;

        public AtmosphereIterator() {
            ImmutableList.Builder<StorageView<FluidVariant>> builder = new ImmutableList.Builder<>();
            atmosphere.forEach(fluidVariantResourceAmount -> {
                builder.add(new InternalView(fluidVariantResourceAmount));
            });
            fluids = builder.build();
        }

        @Override
        public boolean hasNext() {
            return open && index != fluids.size();
        }

        @Override
        public StorageView<FluidVariant> next() {
            if(hasNext()) {
                StorageView<FluidVariant> fluid = fluids.get(index);
                index++;
                return fluid;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void onClose(TransactionContext transaction, TransactionContext.Result result) {
            open = false;
        }
    }
}
