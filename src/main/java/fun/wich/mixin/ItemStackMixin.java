package fun.wich.mixin;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
	@Inject(method="appendTooltip", at=@At(value="INVOKE", target="Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", shift= At.Shift.AFTER))
	private void InjectTooltips(Item.TooltipContext context, TooltipDisplayComponent displayComponent, @Nullable PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
		//Check components
		DynamicRegistryManager manager = player != null ? player.getRegistryManager() : null;
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.AXOLOTL_VARIANT, "axolotl");
		TryAppendVariant(textConsumer, DataComponentTypes.CAT_VARIANT, "cat");
		TryAppendTooltipDyeColor(textConsumer, DataComponentTypes.CAT_COLLAR, "tooltip.collar_color");
		TryAppendTooltipLazyRegistryEntryReference(manager, textConsumer, DataComponentTypes.CHICKEN_VARIANT, "chicken");
		TryAppendVariant(textConsumer, DataComponentTypes.COW_VARIANT, "cow");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.FOX_VARIANT, "fox");
		TryAppendVariant(textConsumer, DataComponentTypes.FROG_VARIANT, "frog");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.HORSE_VARIANT, "horse");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.LLAMA_VARIANT, "llama");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.MOOSHROOM_VARIANT, "mooshroom");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.PARROT_VARIANT, "parrot");
		TryAppendVariant(textConsumer, DataComponentTypes.PIG_VARIANT, "pig");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.RABBIT_VARIANT, "rabbit");
		TryAppendSimpleVariant(textConsumer, DataComponentTypes.SALMON_SIZE, "salmon");
		TryAppendTooltipDyeColor(textConsumer, DataComponentTypes.SHULKER_COLOR, null);
		TryAppendTooltipDyeColor(textConsumer, DataComponentTypes.SHEEP_COLOR, null);
		TryAppendVariant(textConsumer, DataComponentTypes.WOLF_VARIANT, "wolf");
		TryAppendVariant(textConsumer, DataComponentTypes.WOLF_SOUND_VARIANT, "wolf_sound");
		TryAppendTooltipDyeColor(textConsumer, DataComponentTypes.WOLF_COLLAR, "tooltip.collar_color");
		//Check Raw Entity Data
		TypedEntityData<EntityType<?>> typedEntityData = getOrDefault(DataComponentTypes.ENTITY_DATA, null);
		if (typedEntityData != null) {
			EntityType<?> entityType = typedEntityData.getType();
			NbtCompound nbt = typedEntityData.copyNbtWithoutId();
			if (entityType == EntityType.PANDA) {
				nbt.getString("MainGene").ifPresent(gene -> textConsumer.accept(Text.translatable("tooltip.panda.variant." + gene)));
			}
			//General Variant Keys
			String key = entityType.getTranslationKey().replace("entity.minecraft.","");
			nbt.getString("Variant").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("Variant").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("variant").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("variant").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("Type").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("Type").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("type").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("type").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("RabbitType").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("RabbitType").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("rabbitType").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.replace(':','.'))));
			nbt.getInt("rabbitType").ifPresent(variant -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant)));
			nbt.getString("Color").ifPresent(color -> textConsumer.accept(Text.translatable("color.minecraft." + color)));
			nbt.getInt("Color").ifPresent(color -> textConsumer.accept(Text.translatable("color.minecraft." + DyeColor.byIndex(color).getId())));
			nbt.getString("color").ifPresent(color -> textConsumer.accept(Text.translatable("color.minecraft." + color)));
			nbt.getInt("color").ifPresent(color -> textConsumer.accept(Text.translatable("color.minecraft." + DyeColor.byIndex(color).getId())));
			nbt.getString("CollarColor").ifPresent(color -> textConsumer.accept(Text.translatable("tooltip.collar_color").append(Text.translatable("color.minecraft." + color))));
			nbt.getInt("CollarColor").ifPresent(color -> textConsumer.accept(Text.translatable("tooltip.collar_color").append(Text.translatable("color.minecraft." + DyeColor.byIndex(color).getId()))));
			nbt.getString("collarColor").ifPresent(color -> textConsumer.accept(Text.translatable("tooltip.collar_color").append(Text.translatable("color.minecraft." + color))));
			nbt.getInt("collarColor").ifPresent(color -> textConsumer.accept(Text.translatable("tooltip.collar_color").append(Text.translatable("color.minecraft." + DyeColor.byIndex(color).getId()))));
			//Generic Information Keys
			nbt.getString("Size").ifPresent(size -> textConsumer.accept(Text.translatable("tooltip.size").append(size)));
			nbt.getInt("Size").ifPresent(size -> textConsumer.accept(Text.translatable("tooltip.size").append(size.toString())));
			nbt.getString("size").ifPresent(size -> textConsumer.accept(Text.translatable("tooltip.size").append(size)));
			nbt.getInt("size").ifPresent(size -> textConsumer.accept(Text.translatable("tooltip.size").append(size.toString())));
			nbt.getBoolean("Invisible").ifPresent(invisible -> {
				if (invisible) textConsumer.accept(Text.translatable("tooltip.invisible"));
			});
		}
	}
	@Unique
	private <T> void TryAppendTooltipLazyRegistryEntryReference(DynamicRegistryManager manager, Consumer<Text> textConsumer, ComponentType<LazyRegistryEntryReference<T>> component, String key) {
		if (manager != null) {
			LazyRegistryEntryReference<T> entry = getOrDefault(component, null);
			if (entry != null) {
				entry.resolveEntry(manager).ifPresent(tRegistryEntry -> textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + Identifier.of(tRegistryEntry.getIdAsString()).toTranslationKey())));
			}
		}
	}
	@Unique
	private <T extends StringIdentifiable> void TryAppendSimpleVariant(Consumer<Text> textConsumer, ComponentType<T> component, String key) {
		T variant = getOrDefault(component, null);
		if (variant != null) textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + variant.asString()));
	}
	@Unique
	private <T> void TryAppendVariant(Consumer<Text> textConsumer, ComponentType<RegistryEntry<T>> component, String key) {
		RegistryEntry<T> entry = getOrDefault(component, null);
		if (entry != null) textConsumer.accept(Text.translatable("tooltip." + key + ".variant." + Identifier.of(entry.getIdAsString()).toTranslationKey()));
	}
	@Unique
	private void TryAppendTooltipDyeColor(Consumer<Text> textConsumer, ComponentType<DyeColor> component, String translationKey) {
		DyeColor dye;
		if ((dye = this.getOrDefault(component, null)) != null) {
			Text text = Text.translatable("color.minecraft." + dye.getId());
			if (translationKey != null) text = Text.translatable(translationKey).append(text);
			textConsumer.accept(text);
		}
	}
}
