package me.alphamode.nebula.utils;

import me.alphamode.nebula.Nebula;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public abstract class AutoRegistry<T> {
    private final Registry<T> registry;

    public AutoRegistry(Registry<T> registry) {
        this.registry = registry;
        for(Field field : getClass().getFields()) {
            if(field.canAccess(null) && !field.isSynthetic()) {
                try {
                    T obj = register(field.get(null), Nebula.asResource(field.getName()));
                    afterRegister(obj, field.getAnnotation(RegistryInfo.class));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void afterRegister(T registered, RegistryInfo registryInfo) {}

    public T register(Object obj, Identifier id) {
        return Registry.register(registry, id, (T) obj);
    }
}
