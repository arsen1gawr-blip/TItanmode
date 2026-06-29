package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitanMode implements ModInitializer {
  public static final String MOD_ID = "titan-mode";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  // 1. Создаём наш предмет через кастомный метод регистрации из видео
  public static final Item TITAN_DUST = registerItem("titan_dust", new Item.Properties());
  public static final Item TITAN_INGOT = registerItem("titan_ingot", new Item.Properties());
  public static final Item DIAMOND_STICK = registerItem("diamond_stick", new Item.Properties());

  // 2. Метод из видео, который сам создаёт ResourceKey и спасает от ошибки id
  private static Item registerItem(String name, Item.Properties properties) {
    Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
    ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);

    return Registry.register(
        BuiltInRegistries.ITEM,
        key,
        new Item(properties.setId(key)));
  }

  @Override
  public void onInitialize() {
    LOGGER.info("TitanMod успешно инициализирован для версии 1.21.11!");

    // 3. Добавляем титановую пыль во вкладку Ингредиентов в креативе
    ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
      entries.accept(TITAN_DUST);
      entries.accept(TITAN_INGOT);
      entries.accept(DIAMOND_STICK);
    });
  }
}
