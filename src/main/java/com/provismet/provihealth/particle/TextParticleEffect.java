package com.provismet.provihealth.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record TextParticleEffect (Vector3f colour, float alpha, float scale, int textColour, String text) implements ParticleEffect {
    private final static Codec<String> TEXT_CODEC = Codec.string(1, 8).validate(text -> {
        try {
            Integer.valueOf(text);
            return DataResult.success(text);
        } catch (Exception e) {
            return DataResult.error(() -> "Text must be an integer: " + text);
        }
    });

    public static final MapCodec<TextParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Codecs.VECTOR_3F.fieldOf("colour").forGetter(effect -> effect.colour),
                Codecs.POSITIVE_FLOAT.fieldOf("alpha").forGetter(effect -> effect.alpha),
                Codecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(effect -> effect.scale),
                Codecs.rangedInt(0, 0xFFFFFF).fieldOf("text_colour").forGetter(effect -> effect.textColour),
                TEXT_CODEC.fieldOf("text").forGetter(effect -> effect.text))
            .apply(instance, TextParticleEffect::new)
    );

    public static final PacketCodec<RegistryByteBuf, TextParticleEffect> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.VECTOR_3F,
        effect -> effect.colour,
        PacketCodecs.FLOAT,
        effect -> effect.alpha,
        PacketCodecs.FLOAT,
        effect -> effect.scale,
        PacketCodecs.INTEGER,
        effect -> effect.textColour,
        PacketCodecs.string(8),
        effect -> effect.text,
        TextParticleEffect::new
    );

    @Override
    public ParticleType<TextParticleEffect> getType () {
        return Particles.TEXT_PARTICLE;
    }
}
