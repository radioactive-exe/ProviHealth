package com.provismet.provihealth.config.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.provihealth.config.Options;
import com.provismet.provihealth.hud.ElementRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class EntityOptions {
    public static final EntityOptions DEFAULT = new EntityOptions(null, null, null, null);

    public static final Codec<EntityOptions> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("border").forGetter(EntityOptions::getOptionalBorder),
            Identifier.CODEC.optionalFieldOf("healthbar").forGetter(EntityOptions::getOptionalHealthBar),
            ItemStack.CODEC.optionalFieldOf("icon").forGetter(EntityOptions::getOptionalIcon),
            Codecs.NON_EMPTY_STRING.optionalFieldOf("hudType").forGetter(EntityOptions::getOptionalHudType)
        ).apply(instance, (border, health, icon, hud) -> new EntityOptions(border.orElse(null), health.orElse(null), icon.orElse(null), hud.orElse(null)))
    );

    private final Identifier border;
    private final Identifier healthBar;
    private final ItemStack icon;
    private final Options.HUDType hudType;

    public EntityOptions (Identifier border, Identifier healthBar, ItemStack icon, String hudType) {
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

    public @NotNull Identifier getBorder (LivingEntity entity) {
        if (border == null) return Objects.requireNonNullElse(ElementRegistry.getOrCacheBorder(entity), ElementRegistry.DEFAULT_BORDER);
        if (!Options.useCustomHudPortraits) return ElementRegistry.DEFAULT_BORDER;
        return this.border;
    }

    public @NotNull Identifier getHealthBar (LivingEntity entity) {
        // TODO: Add this to border registry!
        return Objects.requireNonNullElse(this.healthBar, ElementRegistry.DEFAULT_BARS);
    }

    public ItemStack getIcon (LivingEntity entity) {
        if (this.icon == null) return ElementRegistry.getOrCacheIcon(entity);
        return this.icon;
    }

    public @NotNull Options.HUDType getHudType (LivingEntity entity) {
        if (Options.isBlacklisted(entity, Options.BarType.HUD)) return Options.HUDType.NONE;
        if (this.hudType == null) {
            if (entity.getType().isIn(ConventionalEntityTypeTags.BOSSES)) return Options.bossHUD;
            else if (entity instanceof HostileEntity) return Options.hostileHUD;
            else if (entity instanceof PlayerEntity) return Options.playerHUD;
            else return Options.otherHUD;
        }
        return this.hudType;
    }
}
