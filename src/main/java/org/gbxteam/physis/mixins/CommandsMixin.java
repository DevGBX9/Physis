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

package org.gbxteam.physis.mixins;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class CommandsMixin {
//#if MC >= 260100
//$$    private static boolean physisCommandsRegistered = false;
//$$
//$$    @Inject(
//#if MC >= 11600
//$$        method = "runServer",
//#else
//$$        method = "run",
//#endif
//$$        at = @At("HEAD")
//$$    )
//$$    private void onRunRegisterPhysisCommands(CallbackInfo ci) {
//$$        if (!physisCommandsRegistered) {
//$$            physisCommandsRegistered = true;
//$$            MinecraftServer server = (MinecraftServer)(Object)this;
//$$            org.gbxteam.physis.PhysisCommand.register(server.getCommands().getDispatcher());
//$$        }
//$$    }
//#endif
}
