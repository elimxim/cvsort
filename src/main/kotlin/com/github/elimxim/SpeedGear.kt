package com.github.elimxim

enum class SpeedGear(val frameDelayMillis: Long) {
    G1(4000),
    G2(2000),
    G3(1000),
    G4(500),
    G5(100),
    G6(50),
    R(-500);
}