package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Function;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitanMode implements ModInitializer {
  public static final String MOD_ID = "titan-mode";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  // === ПРЕДМЕТЫ МОДА ===
  public static final Item TITAN_DUST = registerItem("titan_dust", new Item.Properties());
  public static final Item TITAN_INGOT = registerItem("titan_ingot", new Item.Properties());
  public static final Item DIAMOND_STICK = registerItem("diamond_stick", new Item.Properties());

  // === БЛОКИ МОДА (По канону документации Fabric 1.21.11) ===
  public static final Block DEEPSLATE_TITAN_ORE = register("deepslate_titan_ore", Block::new,
      BlockBehaviour.Properties.of().destroyTime(4.5f).explosionResistance(3.0f).requiresCorrectToolForDrops(), true);

  @Override
  public void onInitialize() {
    LOGGER.info("TitanMod успешно инициализирован для версии 1.21.11!");

    // Добавляем пыль, слиток, палочку и блоки руды на витрину креатива в
    // Ингредиенты
    ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
      entries.accept(TITAN_DUST);
      entries.accept(TITAN_INGOT);
      entries.accept(DIAMOND_STICK);
      entries.accept(DEEPSLATE_TITAN_ORE.asItem()); // Получаем предмет глубинной руды!
    });
    // Инструкция для спавна руды по всему верхнему миру
    BiomeModifications.addFeature(
        BiomeSelectors.foundInOverworld(),
        GenerationStep.Decoration.UNDERGROUND_ORES,
        ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(MOD_ID, "titan_ore")));
  }

  // === Вспомогательный метод регистрации БЛОКОВ из официального гайда ===
  private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory,
      BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
    ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
    Block block = blockFactory.apply(settings.setId(blockKey));

    if (shouldRegisterItem) {
      ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
      BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
      Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
    }

    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
  }

  // === Вспомогательный метод регистрации ПРЕДМЕТОВ ===
  private static Item registerItem(String name, Item.Properties properties) {
    Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
    ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
    return Registry.register(BuiltInRegistries.ITEM, key, new Item(properties.setId(key)));
  }
}
