package net.assassinreport.copperrails.mixin;

import net.assassinreport.copperrails.CopperRails;
import net.assassinreport.copperrails.CopperRailsConfig;
import net.assassinreport.copperrails.block.ModBlocks;
import net.assassinreport.copperrails.block.custom.GenericCopperRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultMinecartController.class)
public abstract class DefaultMinecartControllerMixin {

	@Unique
	private AbstractMinecartEntity copperrails$minecart;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInit(AbstractMinecartEntity minecart, CallbackInfo ci) {
		this.copperrails$minecart = minecart;
	}

	@Unique
	private double getMaxRailSpeed(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block == ModBlocks.COPPER_RAIL) {
			return CopperRailsConfig.COPPER_SPEED;
		} else if (block == Blocks.POWERED_RAIL) {
			return CopperRailsConfig.GOLD_SPEED;
		} else {
			return CopperRailsConfig.NORMAL_RAIL_SPEED;
		}
	}

	@Unique
	private double convergeAbs(double speed, double targetSpeed) {
		if (Math.abs(speed) > targetSpeed) {
			return Math.signum(speed) * Math.max(Math.abs(speed) * 0.7, targetSpeed);
		} else {
			return speed;
		}
	}

	/**
	 * @author Robofox
	 * @reason Custom speed per rail type
	 */
	@Overwrite
	public double getMaxSpeed(ServerWorld world) {
		BlockPos pos = copperrails$minecart.getRailOrMinecartPos();
		BlockState blockState = copperrails$minecart.getWorld().getBlockState(pos);
		boolean wet = copperrails$minecart.isTouchingWater();
		if (blockState.isOf(ModBlocks.COPPER_RAIL)) {
			return wet ? CopperRailsConfig.COPPER_SPEED / 2.0 : CopperRailsConfig.COPPER_SPEED;
		} else if (blockState.isOf(Blocks.POWERED_RAIL)) {
			return wet ? CopperRailsConfig.GOLD_SPEED / 2.0 : CopperRailsConfig.GOLD_SPEED;
		} else {
			return wet ? CopperRailsConfig.NORMAL_RAIL_SPEED / 2.0 : CopperRailsConfig.NORMAL_RAIL_SPEED;
		}
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
			)
	)
	public boolean isPoweringRail(BlockState state, Block block) {
		if (block == Blocks.POWERED_RAIL) {
			Block railBlock = state.getBlock();
			return railBlock instanceof GenericCopperRailBlock || railBlock == Blocks.POWERED_RAIL;
		}
		return state.isOf(block);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/DefaultMinecartController;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 0
			)
	)
	public void setVelocityAscendingEast(DefaultMinecartController controller, Vec3d velocity) {
		Vec3d current = controller.getVelocity();
		double vx = velocity.x;
		if (current.x > CopperRailsConfig.MAX_ASCENDING_SPEED) vx = CopperRailsConfig.MAX_ASCENDING_SPEED;
		controller.setVelocity(new Vec3d(vx, velocity.y, velocity.z));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/DefaultMinecartController;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 1
			)
	)
	public void setVelocityAscendingWest(DefaultMinecartController controller, Vec3d velocity) {
		Vec3d current = controller.getVelocity();
		double vx = velocity.x;
		if (current.x < -CopperRailsConfig.MAX_ASCENDING_SPEED) vx = -CopperRailsConfig.MAX_ASCENDING_SPEED;
		controller.setVelocity(new Vec3d(vx, velocity.y, velocity.z));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/DefaultMinecartController;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 2
			)
	)
	public void setVelocityAscendingNorth(DefaultMinecartController controller, Vec3d velocity) {
		Vec3d current = controller.getVelocity();
		double vz = velocity.z;
		if (current.z < -CopperRailsConfig.MAX_ASCENDING_SPEED) vz = -CopperRailsConfig.MAX_ASCENDING_SPEED;
		controller.setVelocity(new Vec3d(velocity.x, velocity.y, vz));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/DefaultMinecartController;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 3
			)
	)
	public void setVelocityAscendingSouth(DefaultMinecartController controller, Vec3d velocity) {
		Vec3d current = controller.getVelocity();
		double vz = velocity.z;
		if (current.z > CopperRailsConfig.MAX_ASCENDING_SPEED) vz = CopperRailsConfig.MAX_ASCENDING_SPEED;
		controller.setVelocity(new Vec3d(velocity.x, velocity.y, vz));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/DefaultMinecartController;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 10
			)
	)
	public void setVelocityClamp(DefaultMinecartController controller, Vec3d velocity) {
		BlockPos pos = copperrails$minecart.getBlockPos();
		BlockState blockState = copperrails$minecart.getWorld().getBlockState(pos);
		double maxSpeed = getMaxRailSpeed(blockState);
		controller.setVelocity(new Vec3d(
				convergeAbs(velocity.x, maxSpeed),
				velocity.y,
				convergeAbs(velocity.z, maxSpeed)
		));
	}

	@Unique
	private RailShape getRailShape(BlockState blockState, Property<RailShape> property) {
		RailShape railShape = blockState.get(property);
		if (blockState.isOf(ModBlocks.RAIL_CROSSING)) {
			boolean isPowered = blockState.get(PoweredRailBlock.POWERED);
			if (isPowered) {
				switch (railShape) {
					case NORTH_SOUTH:
						return RailShape.EAST_WEST;
					case EAST_WEST:
						return RailShape.NORTH_SOUTH;
					default:
						CopperRails.LOGGER.error("Crossing rail has invalid shape");
				}
			}
		}
		return railShape;
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
					ordinal = 1
			)
	)
	@SuppressWarnings("unchecked")
	public <T extends Comparable<T>> T getMoveOnRailShapeMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}

	@Redirect(
			method = "snapPositionToRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
					ordinal = 0
			)
	)
	@SuppressWarnings("unchecked")
	public <T extends Comparable<T>> T getSnapPositionToRailMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}
}