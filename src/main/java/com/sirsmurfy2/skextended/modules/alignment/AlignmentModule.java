package com.sirsmurfy2.skextended.modules.alignment;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import com.sirsmurfy2.skextended.ModuleLoader;
import com.sirsmurfy2.skextended.modules.alignment.classes.AlignedText;
import com.sirsmurfy2.skextended.modules.alignment.classes.LineBuilder;
import com.sirsmurfy2.skextended.modules.alignment.classes.MessageBuilder;

public class AlignmentModule extends ModuleLoader {

	@Override
	public void loadModule() {
		Classes.registerClass(new ClassInfo<>(MessageBuilder.class, "messagebuilder")
			.user("message ?builders?")
			.defaultExpression(new EventValueExpression<>(MessageBuilder.class))
		);

		Classes.registerClass(new ClassInfo<>(LineBuilder.class, "linebuilder")
			.user("line ?builders?")
			.defaultExpression(new EventValueExpression<>(LineBuilder.class))
		);

		Classes.registerClass(new ClassInfo<>(AlignedText.class, "alignedtext")
			.user("aligned ?texts?")
		);
	}

}
