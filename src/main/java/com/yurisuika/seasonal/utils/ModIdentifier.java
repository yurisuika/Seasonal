package com.yurisuika.seasonal.utils;

import com.yurisuika.seasonal.Seasonal;
import net.minecraft.util.Identifier;

public class ModIdentifier extends Identifier {

    public ModIdentifier(String path) {
        super(Seasonal.MOD_ID, path);
    }

}