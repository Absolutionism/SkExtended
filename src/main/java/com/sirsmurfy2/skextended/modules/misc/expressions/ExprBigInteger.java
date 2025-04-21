package com.sirsmurfy2.skextended.modules.misc.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@Name("Big Integer")
@Description("Convert a number or string into a big integer.")
@Examples("""
	set {_big} to big integer from 2
	set {_big} to big integer of "1000000"
	""")
@Since("1.0.0")
public class ExprBigInteger extends SimplePropertyExpression<Object, BigInteger> {

	static {
		Skript.registerExpression(ExprBigInteger.class, BigInteger.class, ExpressionType.PROPERTY,
			"big integer[s] (of|from) %numbers/strings%");
	}

	@Override
	public @Nullable BigInteger convert(Object object) {
		if (object instanceof Number number) {
			return BigInteger.valueOf(number.longValue());
		} else if (object instanceof String string && !string.isEmpty()) {
			try {
				return new BigInteger(string);
			} catch (Exception ignored) {}
		}
		return null;
	}

	@Override
	public Class<? extends BigInteger> getReturnType() {
		return BigInteger.class;
	}

	@Override
	protected String getPropertyName() {
		return "big integer";
	}

}
