package com.sirsmurfy2.skextended.modules.misc;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import com.sirsmurfy2.skextended.ModuleLoader;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;
import org.skriptlang.skript.lang.converter.Converter;
import org.skriptlang.skript.lang.converter.Converters;

import java.math.BigInteger;

public class MiscModule extends ModuleLoader {

	@Override
	public void loadModule() {
		//  --- BIG INTEGER --- //
		Classes.registerClass(new ClassInfo<>(BigInteger.class, "biginteger")
			.user("big ?integers?")
			.name("Big Integer")
			.description("Represents a big integer object, which allows storing number values that exceed the limit of the system.")
			.examples("""
					set {_big} to big integer of "10000000000000000000000000"
					add 1 to {_big}
					remove 100000 from {_big}
					""")
			.since("1.0.0")
		);

		Converters.registerConverter(Number.class, BigInteger.class, number -> BigInteger.valueOf(number.longValue()),
			Converter.NO_RIGHT_CHAINING);

		Arithmetics.registerOperation(Operator.ADDITION, BigInteger.class, BigInteger::add);
		Arithmetics.registerOperation(Operator.SUBTRACTION, BigInteger.class, BigInteger::subtract);
		Arithmetics.registerOperation(Operator.MULTIPLICATION, BigInteger.class, BigInteger::multiply);
		Arithmetics.registerOperation(Operator.DIVISION, BigInteger.class, BigInteger::divide);
		Arithmetics.registerOperation(Operator.EXPONENTIATION, BigInteger.class, Integer.class, BigInteger::pow);

	}

}
