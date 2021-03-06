package net.blueheart.hdebug.injection.forge.mixins.world;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.features.module.modules.render.ProphuntESP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunk {

    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void setProphuntBlock(BlockPos pos, IBlockState state, final CallbackInfoReturnable callbackInfo) {
        final ProphuntESP prophuntESP = (ProphuntESP) HDebug.moduleManager.getModule(ProphuntESP.class);

        if(prophuntESP.getState()) {
            synchronized(prophuntESP.blocks) {
                prophuntESP.blocks.put(pos, System.currentTimeMillis());
            }
        }
    }
}
