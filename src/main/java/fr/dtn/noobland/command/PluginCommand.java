package fr.dtn.noobland.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PluginCommand {
    String name();
    String description() default "";
    String[] aliases() default {};
    Class<? extends TabCompleter> completer() default TabCompleter.class;
}