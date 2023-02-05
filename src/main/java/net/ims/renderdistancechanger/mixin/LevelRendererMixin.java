package net.ims.renderdistancechanger.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ims.renderdistancechanger.DayCycle;
import net.ims.renderdistancechanger.RenderDistanceChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class LevelRendererMixin {
    @Shadow private float renderDistance;

    @Shadow @Final private Minecraft minecraft;

    @Unique
    private DayCycle isDayCycle;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void rdChanger$changeRD(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        long dayTime = Minecraft.getInstance().level.getDayTime() % 24000L;
        if (isDayCycle != DayCycle.DAY && dayTime > RenderDistanceChanger.config.daytimeSwitch && dayTime < RenderDistanceChanger.config.nighttimeSwitch) {
            isDayCycle = DayCycle.DAY;
            if (RenderDistanceChanger.config.tellWhenChanged) {
                Minecraft.getInstance().player.displayClientMessage(new TextComponent("Switching RD to day!"), false);
            }
            Minecraft.getInstance().options.renderDistance = RenderDistanceChanger.config.daytimeRd;
            Minecraft.getInstance().levelRenderer.allChanged();

        } else if (isDayCycle != DayCycle.NIGHT && (dayTime < RenderDistanceChanger.config.daytimeSwitch || dayTime > RenderDistanceChanger.config.nighttimeSwitch)) {
            isDayCycle = DayCycle.NIGHT;
            if (RenderDistanceChanger.config.tellWhenChanged) {
                Minecraft.getInstance().player.displayClientMessage(new TextComponent("Switching RD to night!"), false);
            }
            Minecraft.getInstance().options.renderDistance = RenderDistanceChanger.config.nighttimeRd;
            Minecraft.getInstance().levelRenderer.allChanged();
        }
    }
}
