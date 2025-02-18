package coffee.amo.astromancy.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

import java.util.Arrays;

public class AstromancyCommand {
    protected static final SimpleCommandExceptionType NO_PERMISSION_EXCEPTION = new SimpleCommandExceptionType(new TextComponent("You don't have permission to use this command."));

    public static void registerSubCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("astromancy")
                .then(AstromancyResearchCommand.register(dispatcher))
                .then(AstromancyResearchRemoveCommand.register(dispatcher));
                dispatcher.register(cmd);
    }

    protected static TextComponent getCmdPrefix(String cmdName){
        return new TextComponent(ChatFormatting.GOLD + "[Astromancy " + cmdName + "] " + ChatFormatting.RESET);
    }

    protected enum CommandFeedbackType{
        INFO(ChatFormatting.GRAY),
        SUCCESS(ChatFormatting.GREEN),
        ERROR(ChatFormatting.RED),
        WARNING(ChatFormatting.YELLOW);

        private final ChatFormatting color;

        CommandFeedbackType(ChatFormatting color){
            this.color = color;
        }

        public ChatFormatting getColor(){
            return color;
        }
    }

    protected static void feedback(CommandSourceStack source, String commandName, String langKey, CommandFeedbackType type, Component... args) {
        source.sendSuccess(AstromancyCommand.getCmdPrefix(commandName).append(new TextComponent(Arrays.toString(args)).setStyle(Style.EMPTY.applyFormat(type.getColor()))), true);
    }

    protected static void error(CommandSourceStack source, String commandName, String langKey, Component... args) {
        source.sendFailure(AstromancyCommand.getCmdPrefix(commandName).append(new TextComponent(Arrays.toString(args)).setStyle(Style.EMPTY.applyFormat(CommandFeedbackType.ERROR.getColor()))));
    }
}
