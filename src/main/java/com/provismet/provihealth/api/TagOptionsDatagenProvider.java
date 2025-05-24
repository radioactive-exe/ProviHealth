package com.provismet.provihealth.api;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.provismet.provihealth.ProviHealthClient;
import com.provismet.provihealth.config.resources.TagOptions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class TagOptionsDatagenProvider implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> future;
    private final DataOutput.PathResolver pathResolver;

    protected TagOptionsDatagenProvider (FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.future = registriesFuture;
        this.pathResolver = dataOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, ProviHealthClient.MODID + "/tag");
    }

    @Override
    public CompletableFuture<?> run (DataWriter writer) {
        return this.future.thenCompose(wrapperLookup -> {
            TagConsumer consumer = new TagConsumer(wrapperLookup);
            this.generate(wrapperLookup, consumer);

            return CompletableFuture.allOf(consumer.entries.entrySet()
                .stream()
                .map(entry -> {
                    Path path = this.pathResolver.resolveJson(entry.getKey().id());
                    return DataProvider.writeToPath(writer, entry.getValue(), path);
                })
                .toArray(CompletableFuture[]::new)
            );
        });
    }

    protected abstract void generate (RegistryWrapper.WrapperLookup lookup, TagConsumer tagConsumer);

    @Override
    public String getName () {
        return "ProviHealth Tag Options";
    }

    protected static class TagConsumer {
        private final Map<TagKey<EntityType<?>>, JsonElement> entries = new HashMap<>();
        private final RegistryWrapper.WrapperLookup lookup;

        private TagConsumer (RegistryWrapper.WrapperLookup lookup) {
            this.lookup = lookup;
        }

        public void add (TagKey<EntityType<?>> tag, TagOptions options) {
            DataResult<JsonElement> json = TagOptions.CODEC.encodeStart(lookup.getOps(JsonOps.INSTANCE), options);
            this.entries.put(tag, json.mapError(message -> "Invalid entry %s: %s".formatted(tag.id(), message)).getOrThrow());
        }
    }
}
