/*
 * Copyright (c) 2023 Flint Loader Contributors
 *
 * Licensed under the MIT license
 */

package net.flintloader.loader.api.event.common;

import com.mojang.brigadier.ParseResults;
import lombok.Getter;
import lombok.Setter;
import net.flintloader.loader.core.event.FlintEvent;
import net.flintloader.loader.core.event.annot.Cancellable;
import net.minecraft.commands.CommandSourceStack;

@Getter @Setter
@Cancellable
public class CommandEvent extends FlintEvent {

    private ParseResults<CommandSourceStack> parseResults;
    private Throwable exception;
    private String command;

    public CommandEvent(ParseResults<CommandSourceStack> parseResults, String command) {
        this.parseResults = parseResults;
        this.command = command;
    }

}
