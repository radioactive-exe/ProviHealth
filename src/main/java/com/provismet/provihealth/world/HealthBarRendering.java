package com.provismet.provihealth.world;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.provismet.provihealth.ProviHealthClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

public abstract class HealthBarRendering {
    public static final RenderPipeline HEALTH_BAR_PIPELINE = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
            .withLocation(ProviHealthClient.identifier("pipeline/healthbar"))
            .withVertexShader("core/entity")
            .withFragmentShader("core/entity")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withShaderDefine("EMISSIVE")
            .withShaderDefine("NO_OVERLAY")
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .withShaderDefine("APPLY_TEXTURE_MATRIX")
            .withUniform("TextureMat", UniformType.MATRIX4X4)
            .withCull(false)
            .build()
    );

    public static RenderLayer getHealthBarLayer (Identifier texture) {
        return RenderLayer.of(
            "provihealth_healthbar",
            1536,
            false,
            false,
            HEALTH_BAR_PIPELINE,
            RenderLayer.MultiPhaseParameters.builder()
                .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
                .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                .build(false)
        );
    }

    public static void init () {}
}
