package fusion_carpet;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.DedicatedServer;

import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class FusionSettings
{
    private static class SimulationDistanceValidator extends Validator<Integer> {
        @Override public Integer validate(ServerCommandSource source, ParsedRule<Integer> currentRule, Integer newValue, String string)
        {
            if (currentRule.get().equals(newValue) || source == null)
            {
                return newValue;
            }
            if (newValue < 0 || newValue > 32)
            {
                Messenger.m(source, "r simulation distance has to be between 0 and 32");
                return null;
            }
            MinecraftServer server = source.getServer();

            if (server.isDedicated())
            {
                int sd = (newValue >= 2)?newValue:((DedicatedServer) server).getProperties().simulationDistance;
                if (sd != server.getPlayerManager().getSimulationDistance())
                    server.getPlayerManager().setSimulationDistance(sd);
                return newValue;
            }
            else
            {
                Messenger.m(source, "r simulation distance can only be changed on a server");
                return 0;
            }
        }
        @Override
        public String description() { return "You must choose a value from 0 (use server settings) to 32";}
    }

    @Rule(
            desc = "Changes the simulation distance of the server.",
            extra = "Set to 0 to not override the value in server settings.",
            options = {"0", "12", "16", "32"},
            category = CREATIVE,
            strict = false,
            validate = FusionSettings.SimulationDistanceValidator.class
    )
    public static int simulationDistance = 0;
}
