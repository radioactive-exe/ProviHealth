package com.provismet.provihealth.datagen;

import com.provismet.provihealth.ProviHealthClient;
import com.provismet.provihealth.api.TagOptionsDatagenProvider;
import com.provismet.provihealth.config.resources.TagOptions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class TagOptionsGenerator extends TagOptionsDatagenProvider {
    private static final int DEFAULT_PRIORITY = -999;

    protected TagOptionsGenerator (FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    protected void generate (RegistryWrapper.WrapperLookup lookup, TagConsumer tagConsumer) {
        tagConsumer.add(EntityTypeTags.AQUATIC, new TagOptions(
            DEFAULT_PRIORITY + 1,
            ProviHealthClient.identifier("textures/gui/healthbars/aquatic.png"),
            null,
            Items.COD.getDefaultStack(),
            null
        ));

        tagConsumer.add(EntityTypeTags.ARTHROPOD, new TagOptions(
            DEFAULT_PRIORITY + 2,
            ProviHealthClient.identifier("textures/gui/healthbars/arthropod.png"),
            null,
            Items.COBWEB.getDefaultStack(),
            null
        ));

        tagConsumer.add(EntityTypeTags.ILLAGER, new TagOptions(
            DEFAULT_PRIORITY,
            ProviHealthClient.identifier("textures/gui/healthbars/illager.png"),
            null,
            Items.IRON_AXE.getDefaultStack(),
            null
        ));

        tagConsumer.add(EntityTypeTags.UNDEAD, new TagOptions(
            DEFAULT_PRIORITY + 3,
            ProviHealthClient.identifier("textures/gui/healthbars/undead.png"),
            null,
            Items.ROTTEN_FLESH.getDefaultStack(),
            null
        ));
    }
}
