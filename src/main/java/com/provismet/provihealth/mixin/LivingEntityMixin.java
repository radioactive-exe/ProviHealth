package com.provismet.provihealth.mixin;

import com.provismet.provihealth.config.Options;
import com.provismet.provihealth.interfaces.IMixinLivingEntity;
import com.provismet.provihealth.particle.HealthParticleEffect;
import com.provismet.provihealth.util.HealthContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IMixinLivingEntity {
    protected LivingEntityMixin (EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private HealthContainer container;

    @Unique
    private HealthContainer mountContainer;

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getMaxHealth();

    @Override
    public HealthContainer provi_Health$getHealthContainer () {
        return this.container;
    }

    @Override
    public HealthContainer provi_Health$getMountHealthContainer () {
        return this.mountContainer;
    }

    @Inject(method="tick", at=@At("TAIL"))
    private void spawnParticles (CallbackInfo info) {
        if (this.container == null) this.container = new HealthContainer(this.getHealth());
        this.container.set(this.getHealth());
        this.container.setMaxHealth(this.getMaxHealth());

        if (this.hasVehicle()) {
            float vehicleHealthDeep = 0f;
            float vehicleMaxHealthDeep = 0f;

            Entity currentEntity = this.getVehicle();
            while (currentEntity != null) {
                if (currentEntity instanceof LivingEntity currentLiving) {
                    vehicleHealthDeep += currentLiving.getHealth();
                    vehicleMaxHealthDeep += currentLiving.getMaxHealth();
                }
                currentEntity = currentEntity.getVehicle();
            }

            if (this.mountContainer == null) this.mountContainer = new HealthContainer(vehicleHealthDeep);
            else this.mountContainer.set(vehicleHealthDeep);
            this.mountContainer.setMaxHealth(vehicleMaxHealthDeep);
        }
        else this.mountContainer = null;

        final Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        if (cameraEntity != null && this != cameraEntity && this.distanceTo(MinecraftClient.getInstance().getCameraEntity()) <= Options.maxParticleDistance) {
            if (this.container.getCurrent() < this.container.getPrevious() && Options.spawnDamageParticles) {
                this.getWorld().addParticle(new HealthParticleEffect(Options.unpackedDamage, Options.damageAlpha, Options.particleScale, Options.damageParticleTextColour, String.format("%d", (int)this.container.getPrevious() - (int)this.container.getCurrent())), this.getX(), this.getEyeY(), this.getZ(), 0f, 0f, 0f);
            }
            else if (this.container.getCurrent() > this.container.getPrevious() && Options.spawnHealingParticles) {
                this.getWorld().addParticle(new HealthParticleEffect(Options.unpackedHealing, Options.healingAlpha, Options.particleScale, Options.healingParticleTextColour, String.format("%d", (int)this.container.getCurrent() - (int)this.container.getPrevious())), this.getX(), this.getEyeY(), this.getZ(), 0f, 0f, 0f);
            }
        }
    }
}
