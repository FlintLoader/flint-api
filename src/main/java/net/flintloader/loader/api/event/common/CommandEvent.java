package net.flintloader.loader.api.event.common;

import com.mojang.brigadier.ParseResults;
import lombok.Getter;
import lombok.Setter;
import net.flintloader.loader.core.event.FlintEvent;
import net.minecraft.commands.CommandSourceStack;

@Getter @Setter
public class CommandEvent extends FlintEvent {

    private ParseResults<CommandSourceStack> parseResults;
    private Throwable exception;
    private String command;

    public CommandEvent(ParseResults<CommandSourceStack> parseResults, String command) {
        this.parseResults = parseResults;
        this.command = command;
    }

    @Override
    public boolean canCancel() {
        return true;
    }
}
