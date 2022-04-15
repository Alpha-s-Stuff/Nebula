package me.alphamode.nebula.content;

import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.content.condensor.CondenserHandledScreen;
import me.alphamode.nebula.content.condensor.CondenserScreenHandler;
import me.alphamode.nebula.utils.AutoRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AllScreenHandlers extends AutoRegistry<ScreenHandlerType<?>> {
    public static ScreenHandlerType<CondenserScreenHandler> CONDENSER = ScreenHandlerRegistry.registerExtended(Nebula.asResource("Condenser"), CondenserScreenHandler::new);

    public AllScreenHandlers() {
        super(Registry.SCREEN_HANDLER);
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            HandledScreens.register(CONDENSER, CondenserHandledScreen::new);
        }
    }

    @Override
    public ScreenHandlerType<?> register(Object obj, Identifier id) {
        if(Registry.SCREEN_HANDLER.containsId(id))
            return (ScreenHandlerType<?>) obj;
        return super.register(obj, id);
    }
}
