package com.github.code3zero;

import cn.nukkit.plugin.PluginBase;

/**
 * @author Code3zero
 * @date 2022/5/14
 */
public class Main extends PluginBase {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }
}
