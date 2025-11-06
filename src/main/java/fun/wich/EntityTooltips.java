package fun.wich;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityTooltips implements ModInitializer {
	public static final String MOD_ID = "wich";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		LOGGER.info("Entity Tooltips - ACTIVE");
	}
}