package fr.dtn.noobland;

import fr.dtn.noobland.block.ModBlocks;
import fr.dtn.noobland.item.ModCreativeModeTabs;
import fr.dtn.noobland.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

@Mod(NoobLand.MOD_ID)
public class NoobLand {
    public static final String MOD_ID = "noobland";

    public NoobLand(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(bus);
        ModBlocks.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event){

    }

    private void addCreative(CreativeModeTabEvent.BuildContents event){
        if(event.getTab() == ModCreativeModeTabs.ITEMS){
            for(RegistryObject<Item> item : ModItems.ITEMS.getEntries())
                if(ModItems.pureItems.contains(item))
                    event.accept(item);
        }

        if(event.getTab() == ModCreativeModeTabs.BLOCKS){
            for(RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries())
                event.accept(block.get().asItem());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            // sadly nothing to do...
        }
    }
}