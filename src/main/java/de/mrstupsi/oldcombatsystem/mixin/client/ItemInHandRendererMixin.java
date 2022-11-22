package de.mrstupsi.oldcombatsystem.mixin.client;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
    public void itemUsed(InteractionHand hand, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}