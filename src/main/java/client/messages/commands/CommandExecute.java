package client.messages.commands;

import client.MapleClient;
import constants.ServerConstants.CommandType;

/**
 * Interface for the executable part of a {@link CommandObject}.
 *
 * @author Emilyx3
 */
public abstract class CommandExecute {

    /**
     * The method executed when this command is used.
     *
     * @param c the client executing this command
     * @param splitted the command and any arguments attached
     *
     * @return 1 if you want to log the command, 0 if not. TODO: USE
     * {@link #ReturnValue}
     */
    public abstract int execute(MapleClient c, String[] splitted);
    //1 = Success
    //0 = Something Went Wrong

    enum ReturnValue {

        DONT_LOG,
        LOG;
    }

    public CommandType getType() {
        return CommandType.NORMAL;
    }

    public static abstract class TradeExecute extends CommandExecute {

        @Override
        public CommandType getType() {
            return CommandType.TRADE;
        }
    }
}
