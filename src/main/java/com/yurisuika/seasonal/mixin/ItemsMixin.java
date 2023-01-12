package com.yurisuika.seasonal.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {

    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;", cancellable = true)
    private static void onBlockItemRegister(Block block, CallbackInfoReturnable<Item> infoReturnable) {
        Identifier id = null;
        if(block == Blocks.SNOW) {
            id = new Identifier("snow");
        }else if(block == Blocks.ICE) {
            id = new Identifier("ice");
        }
        if(id != null) {
            Block original = Registries.BLOCK.get(id);
            BlockItem item = new BlockItem(original, (new Item.Settings()));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            infoReturnable.setReturnValue(Registry.register(Registries.ITEM, id, item));
        }
    }

}