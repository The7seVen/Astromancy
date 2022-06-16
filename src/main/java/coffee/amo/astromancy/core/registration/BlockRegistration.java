package coffee.amo.astromancy.core.registration;

import coffee.amo.astromancy.Astromancy;
import coffee.amo.astromancy.common.block.Astrolabe;
import coffee.amo.astromancy.common.block.StarGateway;
import coffee.amo.astromancy.common.block.armillary_sphere.ArmillarySphereComponentBlock;
import coffee.amo.astromancy.common.block.armillary_sphere.ArmillarySphereCoreBlock;
import coffee.amo.astromancy.common.block.jar.JarBlock;
import coffee.amo.astromancy.common.blockentity.AstrolabeBlockEntity;
import coffee.amo.astromancy.common.blockentity.StarGatewayBlockEntity;
import coffee.amo.astromancy.common.blockentity.jar.JarBlockEntity;
import coffee.amo.astromancy.core.setup.content.item.tabs.ContentTab;
import com.sammy.ortus.systems.block.OrtusBlockProperties;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static coffee.amo.astromancy.core.registration.BlockPropertyRegistration.MAGIC_PROPERTIES;

public class BlockRegistration {
    private static final Registrate REGISTRATE = Astromancy.registrate().creativeModeTab(ContentTab::get);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Astromancy.MODID);

//    public static final BlockEntry<ArmillarySphere<ArmillarySphereBlockEntity>> ARMILLARY_SPHERE = setupSimpleBlock("armillary_sphere",
//            (p) -> new ArmillarySphere<>(p).<ArmillarySphere<ArmillarySphereBlockEntity>>setBlockEntity(BlockEntityRegistration.ARMILLARY_SPHERE), MAGIC_PROPERTIES()).register();

    public static final BlockEntry<StarGateway<StarGatewayBlockEntity>> STAR_GATEWAY = setupSimpleBlock("star_gateway",
            (p) -> new StarGateway<>(p).<StarGateway<StarGatewayBlockEntity>>setBlockEntity(BlockEntityRegistration.STAR_GATEWAY), MAGIC_PROPERTIES()).register();

    public static final BlockEntry<Astrolabe<AstrolabeBlockEntity>> ASTROLABE = setupSimpleBlock("astrolabe",
            (p) -> new Astrolabe<>(p).<Astrolabe<AstrolabeBlockEntity>>setBlockEntity(BlockEntityRegistration.ASTROLABE), MAGIC_PROPERTIES()).register();

    public static final BlockEntry<JarBlock<JarBlockEntity>> JAR = setupSimpleBlock("jar",
            (p) -> new JarBlock<>(p).<JarBlock<JarBlockEntity>>setBlockEntity(BlockEntityRegistration.JAR), MAGIC_PROPERTIES().noOcclusion().isCutoutLayer()).register();

    public static final RegistryObject<Block> ARMILLARY_SPHERE = BLOCKS.register("armillary_sphere", () -> new ArmillarySphereCoreBlock<>(MAGIC_PROPERTIES().isCutoutLayer().noOcclusion()).setBlockEntity(BlockEntityRegistration.ARMILLARY_SPHERE));
    public static final RegistryObject<Block> ARMILLARY_SPHERE_COMPONENT = BLOCKS.register("armillary_sphere_component", () -> new ArmillarySphereComponentBlock(MAGIC_PROPERTIES().isCutoutLayer().noOcclusion().lootFrom(ARMILLARY_SPHERE)));

    public static <T extends Block> BlockBuilder<T, Registrate> setupSimpleBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties) {
        return REGISTRATE.block(name, factory)
                .properties((x) -> properties)
                .simpleItem();
    }

    public static <T extends Block> BlockBuilder<T, Registrate> setupBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties) {
        return REGISTRATE.block(name, factory)
                .properties((x) -> properties);
    }

    public static <T extends StairBlock> BlockBuilder<T, Registrate> setupStairsBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties, RegistryEntry<? extends Block> parent) {
        return setupSimpleBlock(name, factory, properties).blockstate(stairState(parent));
    }

    public static <T extends SlabBlock> BlockBuilder<T, Registrate> setupSlabBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties, RegistryEntry<? extends Block> parent) {
        return setupSimpleBlock(name, factory, properties).blockstate(slabState(parent));
    }

    @SuppressWarnings("unchecked")
    public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slabState(RegistryEntry<? extends Block> parent) {
        return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(parent.get()));
    }

    @SuppressWarnings("unchecked")
    public static <T extends StairBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairState(RegistryEntry<? extends Block> parent) {
        return (ctx, p) -> p.stairsBlock(ctx.getEntry(), p.blockTexture(parent.get()));
    }
    public static void register(){

    }
}
