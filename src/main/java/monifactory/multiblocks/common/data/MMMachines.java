package monifactory.multiblocks.common.data;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import monifactory.multiblocks.MoniMultiblocks;
import monifactory.multiblocks.api.block.IChillerCasingType;
import monifactory.multiblocks.common.CommonProxy;
import monifactory.multiblocks.common.machine.multiblock.HypogeanInfuserMachine;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;

public class MMMachines {

    public static final MultiblockMachineDefinition HYPOGEAN_INFUSER = CommonProxy.REGISTRATE
        .multiblock("hypogean_infuser", HypogeanInfuserMachine::new)
        .rotationState(RotationState.ALL)
        .tier(GTValues.LuV)
        .recipeType(MMRecipeTypes.INFUSER_RECIPES)
        .recipeModifier(HypogeanInfuserMachine::recipeModifer)
        .appearanceBlock(GTBlocks.CASING_ALUMINIUM_FROSTPROOF)
        .pattern(definition -> FactoryBlockPattern.start()
            .aisle("CCCCC", "     ", "     ", "     ", "CCCCC")
            .aisle("CCCCC", " HHH ", " HHH ", " HHH ", "CCCCC")
            .aisle("CCCCC", " HAH ", " HAH ", " HAH ", "CCCCC")
            .aisle("CCCCC", " HHH ", " HHH ", " HHH ", "CCCCC")
            .aisle("CCYCC", "     ", "     ", "     ", "CCCCC").where('H', coolingCoils())
            .where(' ', Predicates.any())
            .where('Y', Predicates.controller(Predicates.blocks(definition.getBlock())))
            .where('C',
                Predicates.blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get())
                    .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                    .or(Predicates
                        .ability(PartAbility.INPUT_ENERGY,
                            GTValues.tiersBetween(GTValues.LuV, GTValues.MAX))
                        .setMinGlobalLimited(1))
                    .or(Predicates
                        .ability(PartAbility.IMPORT_FLUIDS)
                        .setMinGlobalLimited(1)))
            .where('A', Predicates.air()).build())
        .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
            MoniMultiblocks.id("block/multiblock/hypogean_infuser"), false)
        .register();
    public static void init() {

    }

    public static TraceabilityPredicate coolingCoils() {
        return new TraceabilityPredicate(multiState -> {
            BlockState possibleCasing = multiState.getBlockState();
            for (var entry : MMBlocks.CHILLER_CASINGS.entrySet())
            {
                if (possibleCasing.is(entry.getValue().get()))
                {
                    IChillerCasingType stats = entry.getKey();
                    Object currentCoil = multiState.getMatchContext().getOrPut("ChillerCasing",
                        stats);
                    if (!currentCoil.equals(stats))
                    {
                        multiState.setError(
                            new PatternStringError(
                                "moni_multiblocks.multiblock.pattern.error.coils"));
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }, () -> {
            return MMBlocks.CHILLER_CASINGS.entrySet().stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getKey().getCasingTemperature()))
                .map(entry -> BlockInfo.fromBlockState(entry.getValue().get().defaultBlockState()))
                .toArray(BlockInfo[]::new);
        });
    }
}