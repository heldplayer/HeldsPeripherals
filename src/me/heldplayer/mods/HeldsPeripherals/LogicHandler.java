
package me.heldplayer.mods.HeldsPeripherals;

import me.heldplayer.mods.HeldsPeripherals.api.IElectricalFireworksLighter;
import me.heldplayer.mods.HeldsPeripherals.api.IEnderModem;
import me.heldplayer.mods.HeldsPeripherals.api.INoiseMaker;
import me.heldplayer.mods.HeldsPeripherals.api.IThaumicScanner;
import me.heldplayer.mods.HeldsPeripherals.entity.EntityFireworkRocket;
import me.heldplayer.mods.HeldsPeripherals.network.Network;
import me.heldplayer.mods.HeldsPeripherals.packet.Packet1PlaySound;
import me.heldplayer.mods.HeldsPeripherals.packet.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import thaumcraft.api.EnumTag;
import thaumcraft.api.ObjectTags;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aura.AuraNode;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;

public class LogicHandler {

    // { "send", "getChargeLevel", "transport", "getInputOccupied", "getOutputOccupied", "transportLiquid", "getLiquidInfo", "transportFluid", "getFluidInfo"  }
    public static Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments, IEnderModem peripheral) throws Exception {
        switch (method) {
        case 0: // send
            if (peripheral.getRemainingSends() > 0 || ModHeldsPeripherals.chargeCostSend.getValue() <= 0) {
                if (arguments.length < 2) {
                    throw new Exception("Too little arguments, expected at least 2");
                }

                if (!(arguments[0] instanceof Double)) {
                    throw new Exception("Invalid data type for argument 1, expected a number");
                }

                String result = "";
                for (int i = 1; i < arguments.length; i++) {
                    if (i != 1) {
                        result += " ";
                    }

                    result += arguments[i];
                }

                boolean hasSent = Network.send(computer.getID(), peripheral.getWorld().provider.dimensionId, ((Double) arguments[0]).intValue(), result);

                if (hasSent) {
                    if (ModHeldsPeripherals.chargeCostSend.getValue() > 0) {
                        peripheral.decreaseCharge(ModHeldsPeripherals.chargeCostSend.getValue());
                    }

                    peripheral.getWorld().playSoundEffect(peripheral.getX() + 0.5D, peripheral.getY() + 0.5D, peripheral.getZ() + 0.5D, "mob.endermen.portal", 0.2F, 1.5F);
                }

                return new Object[] { Boolean.valueOf(hasSent) };
            }

            return new Object[] { Boolean.FALSE };
        case 1: // getChargeLevel
            return new Object[] { Double.valueOf(peripheral.getChargeLevel()) };
        case 2: // transport
            if (arguments.length < 1) {
                throw new Exception("Too little arguments, expected 1 argument");
            }

            if (arguments.length > 1) {
                throw new Exception("Too many arguments, expected 1 argument");
            }

            if (!(arguments[0] instanceof Double)) {
                throw new Exception("Invalid data type for argument 1, expected a number");
            }

            ItemStack stack = peripheral.getStackInSlot(3);

            if (stack == null || stack.stackSize <= 0 || stack.itemID == 0) {
                return new Object[] { Boolean.FALSE };
            }

            if (peripheral.getRemainingTransports() > 0 || ModHeldsPeripherals.chargeCostTransport.getValue() <= 0) {
                ItemStack returned = Network.transport(computer.getID(), peripheral.getWorld().provider.dimensionId, stack, ((Double) arguments[0]).intValue());

                if (stack != returned) {
                    stack = returned;

                    peripheral.setStackInSlot(3, stack);

                    if (returned == null || returned.stackSize <= 0) {
                        peripheral.setStackInSlot(3, null);
                    }

                    if (ModHeldsPeripherals.chargeCostTransport.getValue() > 0) {
                        peripheral.decreaseCharge(ModHeldsPeripherals.chargeCostTransport.getValue());
                    }

                    peripheral.getWorld().playSoundEffect(peripheral.getX() + 0.5D, peripheral.getY() + 0.5D, peripheral.getZ() + 0.5D, "mob.endermen.portal", 1.0F, 0.8F);

                    return new Object[] { Boolean.TRUE };
                }

                return new Object[] { Boolean.FALSE };
            }

            return new Object[] { Boolean.FALSE };
        case 3: // getInputOccupied
            stack = peripheral.getStackInSlot(3);

            if (stack == null || stack.stackSize <= 0 || stack.itemID == 0) {
                return new Object[] { Boolean.FALSE };
            }
            return new Object[] { Boolean.TRUE };
        case 4: // getOutputOccupied
            stack = peripheral.getStackInSlot(4);

            if (stack == null || stack.stackSize <= 0 || stack.itemID == 0) {
                return new Object[] { Boolean.FALSE };
            }
            return new Object[] { Boolean.TRUE };
        case 5: // transportLiquid
        case 7: // transportFluid
            if (arguments.length < 1) {
                throw new Exception("Too little arguments, expected 1 argument");
            }

            if (arguments.length > 1) {
                throw new Exception("Too many arguments, expected 1 argument");
            }

            if (!(arguments[0] instanceof Double)) {
                throw new Exception("Invalid data type for argument 1, expected a number");
            }

            FluidTank tank = peripheral.getFluidTank();

            FluidStack fluidStack = tank.getFluid();

            if (fluidStack == null || fluidStack.amount <= 0) {
                return new Object[] { Boolean.FALSE };
            }

            if (peripheral.getRemainingTransports() > 0 || ModHeldsPeripherals.chargeCostostTransportFluid.getValue() <= 0) {
                FluidStack returned = Network.transport(computer.getID(), peripheral.getWorld().provider.dimensionId, fluidStack, ((Double) arguments[0]).intValue());

                if (fluidStack != returned) {
                    fluidStack = returned;

                    tank.setFluid(fluidStack);

                    if (returned == null || returned.amount <= 0) {
                        tank.setFluid(null);
                    }

                    if (ModHeldsPeripherals.chargeCostostTransportFluid.getValue() > 0) {
                        peripheral.decreaseCharge(ModHeldsPeripherals.chargeCostostTransportFluid.getValue());
                    }

                    peripheral.getWorld().playSoundEffect(peripheral.getX() + 0.5D, peripheral.getY() + 0.5D, peripheral.getZ() + 0.5D, "mob.endermen.portal", 0.8F, 0.65F);

                    return new Object[] { Boolean.TRUE };
                }

                return new Object[] { Boolean.FALSE };
            }

            return new Object[] { Boolean.FALSE };
        case 6: // getLiquidInfo
        case 8: // getFluidInfo
            tank = peripheral.getFluidTank();

            fluidStack = tank.getFluid();

            if (fluidStack != null && fluidStack.amount > 0) {
                return new Object[] { FluidRegistry.getFluidName(fluidStack), fluidStack.amount };
            }

            return new Object[] { null, 0 };
        default:
            throw new Exception("Error calling method: unknown method ID");
        }
    }

    // { "launchFirework" }
    public static Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments, IElectricalFireworksLighter peripheral) throws Exception {
        if (peripheral.isCoolingDown()) {
            throw new Exception("This peripheral is cooling down, please wait " + ((int) peripheral.getCoolingTime() / 20) + " more seconds.");
        }

        switch (method) {
        case 0: // launchFirework
            if (peripheral.getCoolingTime() > 50) {
                peripheral.setCoolingDown();
                peripheral.setCoolingTime(200);
                throw new Exception("Firing fireworks too fast! Cooldown enabled!");
            }

            if (arguments.length < 7) {
                throw new Exception("Too little arguments, expected at least 7");
            }

            int index = 0;

            if (!(arguments[index] instanceof Double)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
            }

            FireworksPattern firePattern = new FireworksPattern();
            firePattern.flight = (short) (((Double) arguments[index++]) * 20.0D);

            if (!(arguments[index] instanceof Double)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
            }

            firePattern.star = new FireworkStarPattern();
            firePattern.star.type = ((Double) arguments[index++]).byteValue();

            if (!(arguments[index] instanceof Boolean)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a boolean");
            }
            firePattern.star.hasTrail = ((Boolean) arguments[index++]);

            if (!(arguments[index] instanceof Boolean)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a boolean");
            }
            firePattern.star.hasFlicker = ((Boolean) arguments[index++]);

            if (!(arguments[index] instanceof Boolean)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a boolean");
            }
            firePattern.silent = ((Boolean) arguments[index++]);

            if (!(arguments[index] instanceof Boolean)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a boolean");
            }
            firePattern.hideRocketTrail = ((Boolean) arguments[index++]);

            if (!(arguments[index] instanceof Double)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
            }
            int count = ((Double) arguments[index++]).intValue();

            if (count <= 0) {
                throw new Exception("Invalid data type for argument " + index + ", expected a positive number");
            }

            firePattern.star.primaryColors = new int[count];

            for (int i = 0; i < count; i++) {
                if (!(arguments[index] instanceof Double)) {
                    throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
                }

                int color = ((Double) arguments[index++]).intValue();

                firePattern.star.primaryColors[i] = color;
            }

            if (!(arguments[index] instanceof Double)) {
                throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
            }
            count = ((Double) arguments[index++]).intValue();

            if (count < 0) {
                throw new Exception("Invalid data type for argument " + index + ", expected a positive number or zero");
            }

            firePattern.star.secondaryColors = new int[count];

            for (int i = 0; i < count; i++) {
                if (!(arguments[index] instanceof Double)) {
                    throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
                }

                int color = ((Double) arguments[index++]).intValue();

                firePattern.star.secondaryColors[i] = color;
            }

            NBTTagList args = new NBTTagList();

            for (; index < arguments.length;) {
                if (!(arguments[index] instanceof Double)) {
                    throw new Exception("Invalid data type for argument " + (index + 1) + ", expected a number");
                }

                args.appendTag(new NBTTagDouble("", (Double) arguments[index++]));
            }

            firePattern.arguments = args;

            boolean found = false;

            try {
                @SuppressWarnings("unused")
                TileEntity TE = (TileEntity) peripheral;

                ItemStack stack = peripheral.getStack(0);

                if (CommonProxy.isItemOfType(stack, "dustGunpowder")) {
                    found = true;

                    stack.stackSize--;

                    if (stack.stackSize <= 0) {
                        peripheral.setStack(0, null);
                    }
                }
            }
            catch (ClassCastException ex) {
                for (int i = 0; i < 16; i++) {
                    ItemStack stack = peripheral.getStack(i);
                    if (CommonProxy.isItemOfType(stack, "dustGunpowder")) {
                        stack.stackSize--;

                        peripheral.setStack(i, stack);

                        if (stack.stackSize <= 0) {
                            peripheral.setStack(i, null);
                        }

                        found = true;

                        break;
                    }
                }
            }

            if (!found) {
                throw new Exception("Not enough gunpowder to launch.");
            }

            for (int i = 0; i < firePattern.star.primaryColors.length; i++) {
                int color = firePattern.star.primaryColors[i];
                int red = (color & 0xFF0000) >> 16;
                int green = (color & 0xFF00) >> 8;
                int blue = color & 0xFF;

                int tankRed = peripheral.getFluidLevel(0);
                int tankGreen = peripheral.getFluidLevel(1);
                int tankBlue = peripheral.getFluidLevel(2);

                if (tankRed < red) {
                    red = tankRed;
                }
                if (tankGreen < green) {
                    green = tankGreen;
                }
                if (tankBlue < blue) {
                    blue = tankBlue;
                }

                tankRed -= red;
                tankGreen -= green;
                tankBlue -= blue;

                peripheral.setFluidLevel(0, tankRed);
                peripheral.setFluidLevel(1, tankGreen);
                peripheral.setFluidLevel(2, tankBlue);

                color = red << 16 | green << 8 | blue;
                firePattern.star.primaryColors[i] = color;
            }

            for (int i = 0; i < firePattern.star.secondaryColors.length; i++) {
                int color = firePattern.star.secondaryColors[i];
                int red = (color & 0xFF0000) >> 16;
                int green = (color & 0xFF00) >> 8;
                int blue = color & 0xFF;

                int tankRed = peripheral.getFluidLevel(0);
                int tankGreen = peripheral.getFluidLevel(1);
                int tankBlue = peripheral.getFluidLevel(2);

                if (tankRed < red) {
                    red = tankRed;
                }
                if (tankGreen < green) {
                    green = tankGreen;
                }
                if (tankBlue < blue) {
                    blue = tankBlue;
                }

                tankRed -= red;
                tankGreen -= green;
                tankBlue -= blue;

                peripheral.setFluidLevel(0, tankRed);
                peripheral.setFluidLevel(1, tankGreen);
                peripheral.setFluidLevel(2, tankBlue);

                color = red << 16 | green << 8 | blue;
                firePattern.star.secondaryColors[i] = color;
            }

            EntityFireworkRocket entity = new EntityFireworkRocket(peripheral.getWorld(), peripheral.getX() + 0.5D, peripheral.getY() + 0.5D, peripheral.getZ() + 0.5D, firePattern.getItemStack());
            peripheral.getWorld().spawnEntityInWorld(entity);

            peripheral.increaseCoolingTime();

            return null;
        default:
            throw new Exception("Error calling method: unknown method");
        }
    }

    // { "makeNoise" }
    public static Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments, INoiseMaker peripheral) throws Exception {
        if (peripheral.isCoolingDown()) {
            throw new Exception("This peripheral is cooling down, please wait " + ((int) peripheral.getCoolingTime() / 20) + " more seconds.");
        }

        switch (method) {
        case 0: // makeNoise
            if (peripheral.getCoolingTime() > 50) {
                peripheral.setCoolingDown();
                peripheral.setCoolingTime(200);
                throw new Exception("Firing fireworks too fast! Cooldown enabled!");
            }

            if (arguments.length < 3) {
                throw new Exception("Too little arguments, expected 3");
            }

            if (!(arguments[0] instanceof String)) {
                throw new Exception("Invalid data type for argument 1, expected a string");
            }
            if (!(arguments[1] instanceof Double)) {
                throw new Exception("Invalid data type for argument 2, expected a number");
            }
            if (!(arguments[2] instanceof Double)) {
                throw new Exception("Invalid data type for argument 3, expected a number");
            }

            String name = (String) arguments[0];
            float volume = ((Double) arguments[1]).floatValue();
            float pitch = ((Double) arguments[2]).floatValue();

            if (volume < 0.0F || volume > 10.0F) {
                throw new Exception("Invalid range for argument 2, expected a number between 0 and 10");
            }

            if (pitch < 0.5F || pitch > 2.0F) {
                throw new Exception("Invalid range for argument 3, expected a number between 0.5 and 2");
            }

            peripheral.increaseCoolingTime();

            // FIXME: increase string sound size

            Chunk chunk = peripheral.getWorld().getChunkFromBlockCoords(peripheral.getX(), peripheral.getZ());
            Packet1PlaySound packet = new Packet1PlaySound((double) peripheral.getX() + 0.5D, (double) peripheral.getY() + 0.5D, (double) peripheral.getZ() + 0.5D, name, volume, pitch);
            me.heldplayer.util.HeldCore.packet.PacketHandler.sendPacketToPlayersWatching(PacketHandler.instance.createPacket(packet), peripheral.getWorld().provider.dimensionId, chunk.xPosition, chunk.zPosition);
            //peripheral.getWorld().playSoundEffect((double) peripheral.getX() + 0.5D, (double) peripheral.getY() + 0.5D, (double) peripheral.getZ() + 0.5D, name, volume, pitch);
            return null;
        default:
            throw new Exception("Error calling method: unknown method ID");
        }
    }

    // { "getNearestNode" }
    public static Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments, IThaumicScanner peripheral) throws Exception {
        if (!CommonProxy.thaumcraftInstalled) {
            Objects.log.info("Thaumcraft is not installed, but a Thaumic Scanner is being used!");
            throw new Exception("Thaumcraft has not been detected in this installation");
        }

        switch (method) {
        case 0: // getNearestNode
            int nodeId = ThaumcraftApi.getClosestAuraWithinRange(peripheral.getWorld(), (double) peripheral.getX() + 0.5D, (double) peripheral.getY() + 0.5D, (double) peripheral.getZ() + 0.5D, 128.0D);

            if (nodeId < 0) {
                throw new Exception("No nearby node detected");
            }

            AuraNode node = ThaumcraftApi.getNodeCopy(nodeId);

            double distX = abs(peripheral.getX() + 0.5D - node.xPos);
            double distY = abs(peripheral.getY() + 0.5D - node.yPos);
            double distZ = abs(peripheral.getZ() + 0.5D - node.zPos);
            double distance = MathHelper.sqrt_double(distX * distX + distY * distY + distZ * distZ);

            ObjectTags flux = node.flux;

            EnumTag[] tags = flux.getAspects();

            Object[] fluxArray = new Object[tags.length];

            for (int i = 0; i < tags.length; i++) {
                fluxArray[i] = new Object[] { tags[0].id, tags[0].name, flux.getAmount(tags[0]) };
                Objects.log.info(tags[0].id + ": " + tags[0].name + " " + flux.getAmount(tags[0]));
            }

            Objects.log.info(node.key + ": " + node.baseLevel + " / " + node.level + " " + node.type.toString() + " @" + distance);

            return new Object[] { node.key, node.baseLevel, node.level, node.type.toString(), distance, fluxArray };
        default:
            throw new Exception("Error calling method: unknown method ID");
        }
    }

    private static double abs(double value) {
        return value > 0.0D ? value : -value;
    }
}
