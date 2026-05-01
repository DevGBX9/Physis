/*
 * This file is part of the Physis project, licensed under the MIT License.
 *
 * Copyright (C) 2026 GBX Team and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gbxteam.physis;

//#if MC >= 260100
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import com.mojang.brigadier.arguments.FloatArgumentType;
//$$ import com.mojang.brigadier.arguments.IntegerArgumentType;
//$$ import net.minecraft.commands.CommandSourceStack;
//$$ import net.minecraft.commands.Commands;
//$$ import net.minecraft.network.chat.Component;
//#endif

public class PhysisCommand {
//#if MC >= 260100
//$$    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//$$        dispatcher.register(Commands.literal("physis")
//$$            .then(Commands.literal("speed")
//$$                .then(Commands.literal("normal")
//$$                    .executes(context -> setSpeed(context.getSource(), 1.0f, "Normal (1x)")))
//$$                .then(Commands.literal("fast")
//$$                    .executes(context -> setSpeed(context.getSource(), 5.0f, "Fast (5x)")))
//$$                .then(Commands.literal("ultrafast")
//$$                    .executes(context -> setSpeed(context.getSource(), 50.0f, "Ultra Fast (50x)")))
//$$                .then(Commands.literal("slow")
//$$                    .executes(context -> setSpeed(context.getSource(), 0.2f, "Slow (0.2x)")))
//$$                .then(Commands.literal("pause")
//$$                    .executes(context -> setSpeed(context.getSource(), 0.0f, "Paused (0x)")))
//$$                .then(Commands.literal("set")
//$$                    .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 99999.0f))
//$$                        .executes(context -> {
//$$                            float speed = FloatArgumentType.getFloat(context, "value");
//$$                            return setSpeed(context.getSource(), speed, "Custom (" + speed + "x)");
//$$                        })
//$$                    )
//$$                )
//$$            )
//$$            .then(Commands.literal("debug")
//$$                .then(Commands.literal("skip_days")
//$$                    .then(Commands.argument("days", IntegerArgumentType.integer(1, 100))
//$$                        .executes(context -> skipDays(context.getSource(), IntegerArgumentType.getInteger(context, "days"))))
//$$                )
//$$                .then(Commands.literal("stop_skip")
//$$                    .executes(context -> stopSkip(context.getSource())))
//$$            )
//$$        );
//$$    }
//$$
//$$    private static int setSpeed(CommandSourceStack source, float speedMultiplier, String speedName) {
//$$        ForestGrowthHandler.speedMultiplier = speedMultiplier;
//$$        source.sendSuccess(() -> Component.literal("§a[Physis] Simulation speed set to: " + speedName), true);
//$$        return 1;
//$$    }
//$$
//$$    private static int skipDays(CommandSourceStack source, int days) {
//$$        // 24000 ticks per day
//$$        ForestGrowthHandler.fastForwardTicks += (long) days * 24000L;
//$$        source.sendSuccess(() -> Component.literal("§e[Physis] Fast-forwarding simulation for " + days + " days..."), true);
//$$        return 1;
//$$    }
//$$
//$$    private static int stopSkip(CommandSourceStack source) {
//$$        ForestGrowthHandler.fastForwardTicks = 0;
//$$        source.sendSuccess(() -> Component.literal("§c[Physis] Fast-forward stopped."), true);
//$$        return 1;
//$$    }
//#endif
}
