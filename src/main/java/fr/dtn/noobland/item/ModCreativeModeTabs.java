package fr.dtn.noobland.item;

import fr.dtn.noobland.NoobLand;
import fr.dtn.noobland.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoobLand.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab ITEMS;
    public static CreativeModeTab BLOCKS;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        ITEMS = event.registerCreativeModeTab(new ResourceLocation(NoobLand.MOD_ID, "noobland_items"), builder -> builder.icon((() -> new ItemStack(ModItems.MAGIC_CRYSTAL.get()))).title(Component.translatable("creativetab.noobland_items")));
        BLOCKS = event.registerCreativeModeTab(new ResourceLocation(NoobLand.MOD_ID, "noobland_blocks"), builder -> builder.icon((() -> new ItemStack(ModBlocks.MAGIC_BLOCK.get().asItem()))).title(Component.translatable("creativetab.noobland_blocks")));
    }
}