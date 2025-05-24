package com.provismet.provihealth.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ProviHealthDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator (FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LanguageGenerator::new);
        pack.addProvider(LanguageGeneratorUK::new);
        pack.addProvider(ParticleGenerator::new);
        pack.addProvider(TagOptionsGenerator::new);
    }
}
