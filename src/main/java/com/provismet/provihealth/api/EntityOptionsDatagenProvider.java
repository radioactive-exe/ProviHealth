package com.provismet.provihealth.api;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.provismet.provihealth.ProviHealthClient;
import com.provismet.provihealth.config.resources.EntityOptions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class EntityOptionsDatagenProvider implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> future;
    private final DataOutput.PathResolver pathResolver;

    protected EntityOptionsDatagenProvider (FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.future = registriesFuture;
        this.pathResolver = dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, ProviHealthClient.MODID + "/entity");
    }

    @Override
    public CompletableFuture<?> run (DataWriter writer) {
        return this.future.thenCompose(wrapperLookup -> {
            EntityOptionsConsumer consumer = new EntityOptionsConsumer(wrapperLookup);
            this.generate(consumer);

            return CompletableFuture.allOf(consumer.entries.entrySet()
                .stream()
                .map(entry -> {
                    Path path = this.pathResolver.resolveJson(entry.getKey());
                    return DataProvider.writeToPath(writer, entry.getValue(), path);
                })
                .toArray(CompletableFuture[]::new)
            );
        });
    }

    protected abstract void generate (EntityOptionsConsumer entityConsumer);

    @Override
    public String getName () {
        return "ProviHealth Entity Options";
    }

    protected static class EntityOptionsConsumer {
        private final Map<Identifier, JsonElement> entries = new HashMap<>();
        private final RegistryWrapper.WrapperLookup lookup;

        private EntityOptionsConsumer (RegistryWrapper.WrapperLookup lookup) {
            this.lookup = lookup;
        }

        public void add (EntityType<?> type, EntityOptions options) {
            Optional<RegistryKey<EntityType<?>>> optionalKey = Registries.ENTITY_TYPE.getKey(type);
            if (optionalKey.isEmpty()) throw new RuntimeException("Registry key not found for entity type: " + type.toString());

            DataResult<JsonElement> json = EntityOptions.CODEC.encodeStart(lookup.getOps(JsonOps.INSTANCE), options);
            this.entries.put(optionalKey.get().getValue(), json.mapError(message -> "Invalid entry for entity type %s: %s".formatted(optionalKey.get().getValue().toString(), message)).getOrThrow());
        }
    }
}
