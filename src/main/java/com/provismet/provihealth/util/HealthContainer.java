package com.provismet.provihealth.util;

import net.minecraft.util.math.MathHelper;

public final class HealthContainer {
    private float maxHealth;
    private float currentHealth;
    private float previousHealth;
    private float lerpedHealth;

    public HealthContainer (float health) {
        this.currentHealth = health;
        this.previousHealth = health;
    }

    public void setMaxHealth (float health) {
        this.maxHealth = health;
    }

    public void set (float health) {
        this.previousHealth = this.currentHealth;
        this.currentHealth = health;
    }

    public void lerp (float progress) {
        this.lerpedHealth = MathHelper.lerp(progress, this.previousHealth, this.currentHealth);
    }

    public float getMax () {
        return this.maxHealth;
    }

    public float getCurrent () {
        return this.currentHealth;
    }

    public float getPrevious () {
        return this.previousHealth;
    }

    public float getLerped () {
        return this.lerpedHealth;
    }
}
