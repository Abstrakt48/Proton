package com.abstrakt.proton.engine;

public abstract class AbstractProton
{
    public abstract void update(ProtonEngine p, float dt);
    public abstract void render(ProtonEngine p, Renderer r);
}
