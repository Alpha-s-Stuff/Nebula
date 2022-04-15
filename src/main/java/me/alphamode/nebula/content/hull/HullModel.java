package me.alphamode.nebula.content.hull;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class HullModel implements UnbakedModel {

    private final JsonUnbakedModel jsonModel;

    public HullModel(JsonUnbakedModel jsonModel) {
        this.jsonModel = jsonModel;
    }


    @Override
    public Collection<Identifier> getModelDependencies() {
        return jsonModel.getModelDependencies();
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return jsonModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return jsonModel.bake(loader, textureGetter, rotationContainer, modelId);
    }

    public class Baked extends ForwardingBakedModel {
        public Baked(BakedModel model) {
            this.wrapped = model;
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
//            context.pushTransform(quad -> {
//
//                return true;
//            });
//
//            context.popTransform();
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }
}
