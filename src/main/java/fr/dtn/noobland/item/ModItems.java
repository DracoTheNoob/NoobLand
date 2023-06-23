package fr.dtn.noobland.item;

import fr.dtn.noobland.NoobLand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NoobLand.MOD_ID);
    public static final List<RegistryObject<Item>> pureItems = new ArrayList<>();

    public static final RegistryObject<Item> MAGIC_CRYSTAL = register("magic_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_MAGIC_CRYSTAL = register("fire_magic_crystal", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EARTH_MAGIC_CRYSTAL = register("earth_magic_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> WATER_MAGIC_CRYSTAL = register("water_magic_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> THUNDER_MAGIC_CRYSTAL = register("thunder_magic_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RAW_MAGIC_CRYSTAL = register("raw_magic_crystal", () -> new Item(new Item.Properties()));

    private static RegistryObject<Item> register(String name, Supplier<Item> item){
        RegistryObject<Item> object = ITEMS.register(name, item);;
        pureItems.add(object);
        return object;
    }

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}