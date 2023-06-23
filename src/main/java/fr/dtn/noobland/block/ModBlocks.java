package fr.dtn.noobland.block;

import fr.dtn.noobland.NoobLand;
import fr.dtn.noobland.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NoobLand.MOD_ID);

    public static final RegistryObject<Block> MAGIC_ORE = registerBlock("magic_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(5).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_MAGIC_ORE = registerBlock("deepslate_magic_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MAGIC_BLOCK = registerBlock("magic_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5).requiresCorrectToolForDrops()));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}