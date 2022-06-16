package coffee.amo.astromancy.core.registration;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BlockPropertyRegistration {
    public static BlockBehaviour.Properties MAGIC_PROPERTIES() {
        return BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LODESTONE).dynamicShape().isViewBlocking((var1, var2, var3) -> false).noOcclusion();
    }
}
