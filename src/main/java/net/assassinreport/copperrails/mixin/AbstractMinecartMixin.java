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
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartMixin {

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	public boolean isPoweringRail(BlockState state, Block block) {
		if (block == Blocks.POWERED_RAIL) {
			Block unknownRail = state.getBlock();
			return (unknownRail instanceof GenericCopperRailBlock || unknownRail == Blocks.POWERED_RAIL);
		} else {
			CopperRails.LOGGER.warn("isOf() Mixin called with something else than Blocks.POWERED_RAIL");
			return state.isOf(block);
		}
	}

	@Unique
	public double getMaxRailSpeed(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block == ModBlocks.COPPER_RAIL) {
			return CopperRailsConfig.COPPER_SPEED;
		} else if (block == Blocks.POWERED_RAIL) {
			return CopperRailsConfig.GOLD_SPEED;
		} else {
			return CopperRailsConfig.NORMAL_RAIL_SPEED;
		}
	}

	/**
	 * @author Robofox
	 * @reason Rewrite all getMaxSpeed (very short) to increase max speed
	 */
	@Overwrite
	public double getMaxSpeed() {
		AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
		return (minecart.isTouchingWater() ? CopperRailsConfig.NORMAL_RAIL_SPEED / 2.0 : CopperRailsConfig.NORMAL_RAIL_SPEED);
	}

	@Unique
	private double convergeAbs(double speed, double targetSpeed) {
		if (Math.abs(speed) > targetSpeed) {
			return Math.signum(speed) * Math.max(Math.abs(speed) * 0.7, targetSpeed);
		} else {
			return speed;
		}
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 9))
	public void setVelocityClamp(AbstractMinecartEntity minecart, Vec3d velocity) {
		BlockPos pos = minecart.getBlockPos();
		double maxSpeed = getMaxRailSpeed(minecart.getWorld().getBlockState(pos));
		minecart.setVelocity(convergeAbs(velocity.x, maxSpeed), velocity.y, convergeAbs(velocity.z, maxSpeed));
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 0))
	public void setVelocityAscendingEast(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 1))
	public void setVelocityAscendingWest(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.x < -CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_x = -CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 2))
	public void setVelocityAscendingNorth(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z < -CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = -CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
	}

	@Redirect(
			method = "moveOnRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
					ordinal = 3))
	public void setVelocityAscendingSouth(AbstractMinecartEntity minecart, Vec3d velocity_adder) {
		Vec3d velocity = minecart.getVelocity();
		double v_x = velocity_adder.x;
		double v_y = velocity_adder.y;
		double v_z = velocity_adder.z;
		if (velocity.z > CopperRailsConfig.MAX_ASCENDING_SPEED) {
			v_z = CopperRailsConfig.MAX_ASCENDING_SPEED;
		}
		minecart.setVelocity(v_x, v_y, v_z);
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
					ordinal = 1))
	public <T extends Comparable<T>> T getMoveAlongTrackMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}

	@Redirect(
			method = "snapPositionToRailWithOffset",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
					ordinal = 0))
	public <T extends Comparable<T>> T getSnapPositionToRailWithOffsetMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}

	@Redirect(
			method = "snapPositionToRail",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;",
					ordinal = 0))
	public <T extends Comparable<T>> T getSnapPositionToRailMixin(BlockState blockState, Property<RailShape> property) {
		return (T) getRailShape(blockState, property);
	}
}