package coffee.amo.astromancy.core.systems.multiblock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class MultiblockItem extends BlockItem {
    public final Supplier<? extends MultiblockStructure> structure;

    public MultiblockItem(Block block, Properties properties, Supplier<? extends MultiblockStructure> structure) {
        super(block, properties);
        this.structure = structure;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        if (!structure.get().canPlace(context)) {
            return false;
        }
        return super.canPlace(context, state);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        structure.get().place(context);
        return super.placeBlock(context, state);
    }
}
