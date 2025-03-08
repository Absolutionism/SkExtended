package com.sirsmurfy2.skextended.modules.alignment.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.sirsmurfy2.skextended.utils.PixelUtils;
import org.jetbrains.annotations.Nullable;

public class ExprPixelLength extends SimplePropertyExpression<String, Integer> {

    static {
        register(ExprPixelLength.class, Integer.class, "pixel length[s]", "strings");
    }

    @Override
    public @Nullable Integer convert(String string) {
        return PixelUtils.getLength(string);
    }

    @Override
    protected String getPropertyName() {
        return "pixel length";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

}
