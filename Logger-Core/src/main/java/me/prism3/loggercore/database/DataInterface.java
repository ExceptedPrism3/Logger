package me.prism3.loggercore.database;

import me.prism3.loggercore.database.data.Arguments;
import me.prism3.loggercore.database.entity.ActionInterface;
import me.prism3.loggercore.database.utils.HQLBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public interface DataInterface {

    default List<ActionInterface> getPlayerChat(Arguments arguments) {

        final HQLBuilder hqlBuilder = new HQLBuilder("PlayerChat", "p");

        if (arguments.getUsername() != null)
            hqlBuilder.addWhere("entityPlayer.playerName", arguments.getUsername(), HQLBuilder.ConditionOperator.LIKE, null);

        if (arguments.getTime() != null) {

            final SimpleDateFormat s = new SimpleDateFormat();
            s.applyPattern("yyyy-MM-dd HH:mm:ss");

            hqlBuilder.addDateCondition(arguments.getMinimumDate());
        }

        arguments.setTotalCount(hqlBuilder.getTotalCount());
        return (List<ActionInterface>) hqlBuilder.getResultsPaginated(arguments.getPage(), arguments.getHeight());
    }

    default List<ActionInterface> getConsoleCommands(Arguments arguments) {

        final HQLBuilder hqlBuilder = new HQLBuilder("ConsoleCommand", "c");

        if (arguments.getTime() != null)
            hqlBuilder.addDateCondition(arguments.getMinimumDate());

        arguments.setTotalCount(hqlBuilder.getTotalCount());

        return (List<ActionInterface>) hqlBuilder.getResultsPaginated(arguments.getPage(), arguments.getHeight());
    }

    default List<ActionInterface> getBlockInteraction(Arguments arguments) {

        final HQLBuilder hqlBuilder = new HQLBuilder("BlockInteraction", "b");

        if (arguments.getTime() != null)
            hqlBuilder.addDateCondition(arguments.getMinimumDate());

        if (arguments.getUsername() != null)
            hqlBuilder.addWhere("entityPlayer.playerName", arguments.getUsername(), HQLBuilder.ConditionOperator.LIKE, null);

        arguments.setTotalCount(hqlBuilder.getTotalCount());

        return (List<ActionInterface>) hqlBuilder.getResultsPaginated(arguments.getPage(), arguments.getHeight());
    }

    default List<ActionInterface> getPlayerConnection(Arguments arguments) {

        final HQLBuilder hqlBuilder = new HQLBuilder("PlayerConnection", "p");

        if (arguments.getTime() != null)
            hqlBuilder.addDateCondition(arguments.getMinimumDate());

        if (arguments.getUsername() != null)
            hqlBuilder.addWhere("entityPlayer.playerName", arguments.getUsername(), HQLBuilder.ConditionOperator.LIKE
                    , null);

        arguments.setTotalCount(hqlBuilder.getTotalCount());

        return (List<ActionInterface>) hqlBuilder.getResultsPaginated(arguments.getPage(), arguments.getHeight());
    }

    //TODO add the rest of the methods
}
