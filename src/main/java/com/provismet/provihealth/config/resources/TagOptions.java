package com.provismet.provihealth.config.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.provihealth.config.Options;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TagOptions {
    public static final Codec<TagOptions> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("priority").forGetter(TagOptions::getPriority),
            Identifier.CODEC.optionalFieldOf("border").forGetter(TagOptions::getOptionalBorder),
            Identifier.CODEC.optionalFieldOf("healthbar").forGetter(TagOptions::getOptionalHealthBar),
            ItemStack.CODEC.optionalFieldOf("icon").forGetter(TagOptions::getOptionalIcon),
            Codecs.NON_EMPTY_STRING.optionalFieldOf("hudType").forGetter(TagOptions::getOptionalHudType)
        ).apply(instance, (priority, border, health, icon, hud) -> new TagOptions(priority, border.orElse(null), health.orElse(null), icon.orElse(null), hud.orElse(null)))
    );

    private final int priority;
    private final Identifier border;
    private final Identifier healthBar;
    private final ItemStack icon;
    private final Options.HUDType hudType;

    public TagOptions (int priority, Identifier border, Identifier healthBar, ItemStack icon, String hudType) {
        this.priority = priority;
        this.border = border;
        this.healthBar = healthBar;
        this.icon = icon;
        if (hudType == null) this.hudType = null;
        else this.hudType = Options.HUDType.valueOf(hudType);
    }

    private Optional<Identifier> getOptionalBorder () {
        return Optional.ofNullable(this.border);
    }

    private Optional<Identifier> getOptionalHealthBar () {
        return Optional.ofNullable(this.healthBar);
    }

    private Optional<ItemStack> getOptionalIcon () {
        return Optional.ofNullable(this.icon);
    }

    private Optional<String> getOptionalHudType () {
        if (hudType == null) return Optional.empty();
        return Optional.of(this.hudType.name());
    }

    public int getPriority () {
        return this.priority;
    }

    public @Nullable Identifier getBorder () {
        return this.border;
    }

    public @Nullable Identifier getHealthBar () {
        return this.healthBar;
    }

    public @Nullable ItemStack getIcon (LivingEntity entity) {
        return this.icon;
    }

    public @Nullable Options.HUDType getHudType (LivingEntity entity) {
        return this.hudType;
    }
}
