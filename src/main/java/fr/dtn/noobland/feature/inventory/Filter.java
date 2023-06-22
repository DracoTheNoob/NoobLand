package fr.dtn.noobland.feature.inventory;

import org.bukkit.inventory.ItemStack;

public class Filter {
    private final FilterType type;
    private final String target;

    public Filter(FilterType type, String target) {
        this.type = type;
        this.target = target;
    }

    public boolean accept(ItemStack stack){
        String material = stack.getType().name();

        return switch(type){
            case MATERIAL -> material.equals(target);
            case TOOL -> material.contains(target) && (
                        material.contains("SWORD") ||
                        material.contains("PICKAXE") ||
                        material.contains("SHOVEL") ||
                        material.contains("HOE") ||
                        material.contains("AXE"));
            case ARMOR -> material.contains(target) && (
                    material.contains("HELMET") ||
                    material.contains("CHESTPLATE") ||
                    material.contains("LEGGINGS") ||
                    material.contains("BOOTS"));
        };
    }

    public FilterType getType() { return type; }
    public String getTarget() { return target; }

    public String toSql() { return type.name() + '-' + target; }
}