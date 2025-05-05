package com.provismet.provihealth.mixin;

import com.provismet.provihealth.world.HealthBarRendering;
import net.minecraft.client.gl.ShaderLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderLoader.class)
public abstract class ShaderLoaderMixin extends SinglePreparationResourceReloader<ShaderLoader.Definitions> {
    @Inject(method = "apply(Lnet/minecraft/client/gl/ShaderLoader$Definitions;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
    private void addHealth (ShaderLoader.Definitions definitions, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        HealthBarRendering.init();
    }
}
