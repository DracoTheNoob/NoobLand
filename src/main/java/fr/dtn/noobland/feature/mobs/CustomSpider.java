package fr.dtn.noobland.feature.mobs;

import org.bukkit.entity.Spider;

public class CustomSpider extends CustomEntity<Spider> {
    public CustomSpider(Spider entity, int level) { super(entity, level); }

    @Override public void init(){}

    @Override protected double calculateHealth(int level) { return (int)Math.pow(level-1, 1.2)+1; }
    @Override protected double calculateAttack(int level) { return (int)Math.pow(level-1, 0.75)+1; }
    @Override protected double calculateSpeed(int level) { return 0.3 + 0.01 * level; }
}