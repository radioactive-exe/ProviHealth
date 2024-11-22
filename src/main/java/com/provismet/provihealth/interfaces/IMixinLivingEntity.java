package com.provismet.provihealth.interfaces;

public interface IMixinLivingEntity {
    float provihealth_glideHealth (float glideFactor);
    float provihealth_glideVehicle (float trueValue, float glideFactor);
}
