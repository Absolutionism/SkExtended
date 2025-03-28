package com.sirsmurfy2.skextended.modules.alignment.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.EventRestrictedSyntax;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.chat.ChatMessages;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.sirsmurfy2.skextended.modules.alignment.classes.LineBuilder;
import com.sirsmurfy2.skextended.modules.alignment.sections.ExprSecLineBuilder.LineBuilderEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

public class EffLineBounds extends Effect implements EventRestrictedSyntax, SyntaxRuntimeErrorProducer {

    static {
        Skript.registerEffect(EffLineBounds.class,
            "(set|make) [the] [line] left bound character to %string%",
            "(set|make) [the] [line] right bound character to %string%");
    }

    private Expression<String> string;
    private boolean isLeft;
    private Node node;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        isLeft = matchedPattern == 0;
        //noinspection unchecked
        string = (Expression<String>) exprs[0];
        node = getParser().getNode();
        return true;
    }

    @Override
    public Class<? extends Event>[] supportedEvents() {
        return CollectionUtils.array(LineBuilderEvent.class);
    }

    @Override
    protected void execute(Event event) {
        if (!(event instanceof LineBuilderEvent lineBuilderEvent))
            return;
        String string = this.string.getSingle(event);
        if (string == null || string.isEmpty()) {
            error("The line bound character cannot be null nor empty.");
            return;
        }
        String uncolored = ChatMessages.stripStyles(string);
        if (uncolored.length() > 1) {
            error("The line bound character can only be one character.");
            return;
        }
        LineBuilder lineBuilder = lineBuilderEvent.getLineBuilder();
        if (isLeft) {
            lineBuilder.setLeftBound(string);
        } else {
            lineBuilder.setRightBound(string);
        }
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

}
