package com.provismet.provihealth.hud;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.provismet.provihealth.api.ProviHealthApi;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import com.provismet.provihealth.ProviHealthClient;
import com.provismet.provihealth.config.Options;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BorderRegistry {
    private static final HashMap<EntityType<?>, Identifier> borderCache = new HashMap<>();
    private static final HashMap<EntityType<?>, ItemStack> iconCache = new HashMap<>();
    private static final HashMap<TagKey<EntityType<?>>, BorderPriority> tagBorderPriorities = new HashMap<>();
    private static final HashMap<TagKey<EntityType<?>>, ItemPriority> tagIconPriorities = new HashMap<>();
    private static final HashMap<EntityType<?>, BorderPriority> typeBorderPriorities = new HashMap<>();
    private static final HashMap<EntityType<?>, ItemPriority> typeIconPriorities = new HashMap<>();

    private static final List<TitlePriority> orderedTitles = new ArrayList<>();

    private static final Identifier DEFAULT = ProviHealthClient.identifier("textures/gui/healthbars/default.png");

    public static boolean registerBorder (TagKey<EntityType<?>> entityTag, @Nullable Identifier border, int priority) {
        if (entityTag == null) {
            ProviHealthClient.LOGGER.error("Attempted to register a null object to the border registry.");
            return false;
        }
        else if (tagBorderPriorities.containsKey(entityTag) && priority <= tagBorderPriorities.get(entityTag).priority()) {
            return false;
        }
        tagBorderPriorities.put(entityTag, new BorderPriority(border, priority));
        return true;
    }

    public static boolean registerItem (TagKey<EntityType<?>> entityTag, @Nullable ItemStack item, int priority) {
        if (entityTag == null) {
            ProviHealthClient.LOGGER.error("Attempted to register a null EntityGroup to the icon registry.");
            return false;
        }
        else if (tagIconPriorities.containsKey(entityTag) && priority <= tagIconPriorities.get(entityTag).priority()) {
            return false;
        }
        tagIconPriorities.put(entityTag, new ItemPriority(item, priority));
        return true;
    }

    public static boolean registerBorder (EntityType<?> type, @Nullable Identifier border, int priority) {
        if (type == null) {
            ProviHealthClient.LOGGER.error("Attempted to register a null EntityType to the border registry.");
            return false;
        }
        else if (typeBorderPriorities.containsKey(type) && priority <= typeBorderPriorities.get(type).priority()) {
            return false;
        }
        typeBorderPriorities.put(type, new BorderPriority(border, priority));
        return true;
    }

    public static boolean registerItem (EntityType<?> type, @Nullable ItemStack item, int priority) {
        if (type == null) {
            ProviHealthClient.LOGGER.error("Attempted to register a null EntityType to the icon registry.");
            return false;
        }
        else if (typeIconPriorities.containsKey(type) && priority <= typeIconPriorities.get(type).priority()) {
            return false;
        }
        typeIconPriorities.put(type, new ItemPriority(item, priority));
        return true;
    }

    public static void registerTitle (ProviHealthApi.TitleGenerator titleGen, int order) {
        orderedTitles.add(new TitlePriority(titleGen, order));
    }

    public static void sortTitles () {
        orderedTitles.sort(new Comparator<TitlePriority>() {
            @Override
            public int compare (TitlePriority a, TitlePriority b) {
                return a.order() - b.order();
            }
        });
    }

    public static Identifier getBorder (@Nullable LivingEntity entity) {
        if (entity == null || !Options.useCustomHudPortraits) return DEFAULT;
        else {
            if (borderCache.containsKey(entity.getType())) return borderCache.get(entity.getType());

            int maxPriority = -1000;
            Identifier bestBorder = DEFAULT;
            for (TagKey<EntityType<?>> entityTag : tagBorderPriorities.keySet()) {
                if (entity.getType().isIn(entityTag) && tagBorderPriorities.get(entityTag).priority() > maxPriority) {
                    bestBorder = tagBorderPriorities.get(entityTag).borderId();
                    maxPriority = tagBorderPriorities.get(entityTag).priority();
                }
            }
            for (EntityType<?> type : typeBorderPriorities.keySet()) {
                if (entity.getType() == type && typeBorderPriorities.get(type).priority() > maxPriority) {
                    bestBorder = typeBorderPriorities.get(type).borderId();
                    maxPriority = typeBorderPriorities.get(type).priority();
                }
            }
            borderCache.put(entity.getType(), bestBorder);
            return bestBorder;
        }
    }

    @Nullable
    public static ItemStack getItem (LivingEntity entity) {
        if (entity == null) return null;
        else if (iconCache.containsKey(entity.getType())) return iconCache.get(entity.getType());

        int maxPriority = -1000;
        ItemStack bestIcon = null;
        for (TagKey<EntityType<?>> entityTag : tagIconPriorities.keySet()) {
            if (entity.getType().isIn(entityTag) && tagIconPriorities.get(entityTag).priority() > maxPriority) {
                bestIcon = tagIconPriorities.get(entityTag).itemStack();
                maxPriority = tagIconPriorities.get(entityTag).priority();
            }
        }
        for (EntityType<?> type : typeIconPriorities.keySet()) {
            if (entity.getType() == type && typeIconPriorities.get(type).priority() > maxPriority) {
                bestIcon = typeIconPriorities.get(type).itemStack();
                maxPriority = typeIconPriorities.get(type).priority();
            }
        }
        iconCache.put(entity.getType(), bestIcon);
        return bestIcon;
    }

    public static List<Text> getTitle (LivingEntity entity, boolean world, boolean hud) {
        if (entity == null) return null;

        List<Text> titles = orderedTitles.stream().map(title -> title.titleGetter().apply(entity, world, hud)).filter(title -> title != null).toList();
        return titles;
    }

    private record ItemPriority (ItemStack itemStack, int priority) {}
    private record BorderPriority (Identifier borderId, int priority) {}
    private record TitlePriority (ProviHealthApi.TitleGenerator titleGetter, int order) {}
}
