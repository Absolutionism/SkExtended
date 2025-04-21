package com.sirsmurfy2.skextended.modules.shopkeepers.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.*;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Direction;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.sirsmurfy2.skextended.lang.SectionEvent;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Name("Create Shopkeeper")
@Description("Creates a new admin shop keeper of the provided entity type.")
@Examples("""
	create a new villager shopkeeper at location(0, 0, 0):
		set {_shopkeeper} to event-shopkeeper
	""")
@Since("1.0.0")
@RequiredPlugins("Shopkeepers")
public class EffSecCreateShopkeeper extends EffectSection {

	static {
		Skript.registerSection(EffSecCreateShopkeeper.class,
			"create [a] [new] %entitydata% shop[ ]keeper %direction% %location%");
		//noinspection unchecked
		EventValues.registerEventValue(SectionEvent.class, Shopkeeper.class, event -> (Shopkeeper) event.getValue(Shopkeeper.class));
	}

	private Expression<EntityData<?>> entityData;
	private Expression<Location> location;
	private @Nullable Trigger trigger = null;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		//noinspection unchecked
		entityData = (Expression<EntityData<?>>) exprs[0];
		//noinspection unchecked
		location = Direction.combine(
			(Expression<? extends Direction>) exprs[1],
			(Expression<? extends Location>) exprs[2]
		);
		if (sectionNode != null) {
			AtomicBoolean delayed = new AtomicBoolean(false);
			Runnable afterLoading = () -> delayed.set(!getParser().getHasDelayBefore().isFalse());
			trigger = loadCode(sectionNode, "create shopkeeper", afterLoading, SectionEvent.class);
			if (delayed.get()) {
				Skript.error("Delays can't be used within a Create Shopkeeper Section.");
				return false;
			}
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		EntityData<?> entityData = this.entityData.getSingle(event);
		assert entityData != null;
		EntityType entityType = EntityUtils.toBukkitEntityType(entityData);
		Location location = this.location.getSingle(event);

		ShopObjectType<?> shopObject = null;
		if (entityData.getType().isAssignableFrom(LivingEntity.class))
			shopObject = DefaultShopObjectTypes.LIVING().get(entityType);
		if (shopObject == null)
			return super.walk(event, false);

		ShopCreationData shopCreationData = AdminShopCreationData.create(
			null,
			DefaultShopTypes.ADMIN_REGULAR(),
			shopObject,
			location,
			null
		);
		Shopkeeper shopkeeper = null;
		try {
			shopkeeper = ShopkeepersAPI.getShopkeeperRegistry().createShopkeeper(shopCreationData);
		} catch (ShopkeeperCreateException ignored) {}

		if (shopkeeper != null && trigger != null) {
			SectionEvent<?> sectionEvent = new SectionEvent<>()
				.setValue(Shopkeeper.class, shopkeeper);
			Variables.withLocalVariables(event, sectionEvent, () -> TriggerItem.walk(trigger, sectionEvent));
		}

		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return new SyntaxStringBuilder(event, debug)
			.append("create a new", entityData, "shop keeper", location)
			.toString();
	}

}
