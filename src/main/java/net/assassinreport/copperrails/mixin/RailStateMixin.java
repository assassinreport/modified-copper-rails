package net.assassinreport.copperrails.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.assassinreport.copperrails.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RailPlacementHelper.class)
public class RailStateMixin {

    @Unique
    private static boolean canMakeSlopes(World world, BlockPos blockPos, BlockPos blockPosCheckSlopes, Operation<Boolean> original) {
        BlockState railBlockStateCheckSlopes = world.getBlockState(blockPosCheckSlopes);
        return !railBlockStateCheckSlopes.isOf(ModBlocks.RAIL_CROSSING) && original.call(world, blockPos);
    }

    @WrapOperation(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 0
            ))
    private boolean isRailMixinComputeRailShapeNorth(World world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.down().south(), original);
    }

    @WrapOperation(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 1
            )
    )
    private boolean isRailMixinComputeRailShapeSouth(World world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.down().north(), original);
    }

    @WrapOperation(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 2
            )
    )
    private boolean isRailMixinComputeRailShapeEast(World world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.down().west(), original);
    }

    @WrapOperation(
            method = "updateBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/AbstractRailBlock;isRail(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 3
            )
    )
    private boolean isRailMixinComputeRailShapeWest(World world, BlockPos blockPos, Operation<Boolean> original) {
        return canMakeSlopes(world, blockPos, blockPos.down().east(), original);
    }
}