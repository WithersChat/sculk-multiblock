package monifactory.multiblocks.common.data;

import static monifactory.multiblocks.common.CommonProxy.REGISTRATE;

import com.gregtechceu.gtceu.api.item.RendererBlockItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import monifactory.multiblocks.api.block.IChillerCasingType;
import monifactory.multiblocks.common.block.ChillerCasingBlock;
import monifactory.multiblocks.common.block.ChillerCasingBlock.ChillerCasingType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class MMBlocks {

    public static BlockEntry<ChillerCasingBlock> MESOL_CASING = createChillerCasingBlock(
        ChillerCasingType.MESOL);
    public static BlockEntry<ChillerCasingBlock> BATHYAL_CASING = createChillerCasingBlock(
        ChillerCasingType.BATHYAL);
    public static BlockEntry<ChillerCasingBlock> ABYSSAL_CASING = createChillerCasingBlock(
        ChillerCasingType.ABYSSAL);
    public static BlockEntry<ChillerCasingBlock> HADAL_CASING = createChillerCasingBlock(
        ChillerCasingType.HADAL);

    public static void init() {

    }

    private static BlockEntry<ChillerCasingBlock> createChillerCasingBlock(
        IChillerCasingType coilType) {
        BlockEntry<ChillerCasingBlock> coilBlock = REGISTRATE
            .block("%s_coil_block".formatted(coilType.getName()),
                p -> new ChillerCasingBlock(p, coilType))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate(NonNullBiConsumer.noop())
            .tag(GTToolType.WRENCH.harvestTags.get(0), BlockTags.MINEABLE_WITH_PICKAXE)
            .item(RendererBlockItem::new).model(NonNullBiConsumer.noop())
            .build()
            .register();
        return coilBlock;
    }
}
