package com.provismet.provihealth.mixin;

import com.provismet.provihealth.interfaces.IMixinEntityRenderState;
import com.provismet.provihealth.util.HealthContainer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements IMixinEntityRenderState {
    @Unique
    private boolean shouldRenderHealth;

    @Unique
    private HealthContainer healthContainer;

    @Unique
    private HealthContainer mountHealthContainer;

    @Unique
    private boolean isLiving;

    @Unique
    private List<Text> titles = List.of();

    @Unique
    private boolean shouldRenderLabel;

    @Override
    public void provi_Health$setShouldRenderHealth (boolean value) {
        this.shouldRenderHealth = value;
    }

    @Override
    public void provi_Health$setHealth (float value) {
        if (this.healthContainer == null) this.healthContainer = new HealthContainer(value);
        else this.healthContainer.set(value);
    }

    @Override
    public void provi_Health$setMountHealth (float value) {
        if (this.mountHealthContainer == null) this.mountHealthContainer = new HealthContainer(value);
        else this.mountHealthContainer.set(value);
    }

    @Override
    public void provi_Health$setIsLiving (boolean value) {
        this.isLiving = true;
    }

    @Override
    public void provi_Health$setTitles (List<Text> titles) {
        this.titles = titles;
    }

    @Override
    public void provi_Health$setShouldRenderLabel (boolean value) {
        this.shouldRenderLabel = value;
    }

    @Override
    public boolean provi_Health$shouldRenderHealth () {
        return this.shouldRenderHealth;
    }

    @Override
    public HealthContainer provi_Health$getHealth () {
        return this.healthContainer;
    }

    @Override
    public HealthContainer provi_Health$getMountHealth () {
        return this.mountHealthContainer;
    }

    @Override
    public boolean provi_Health$isLiving () {
        return this.isLiving;
    }

    @Override
    public List<Text> provi_Health$getTitles () {
        return this.titles;
    }

    @Override
    public boolean provi_Health$shouldRenderLabel () {
        return this.shouldRenderLabel;
    }
}
