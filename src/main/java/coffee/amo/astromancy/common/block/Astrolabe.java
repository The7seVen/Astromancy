package coffee.amo.astromancy.common.block;

import coffee.amo.astromancy.common.blockentity.AstrolabeBlockEntity;
import coffee.amo.astromancy.core.systems.block.AstromancyEntityBlock;

public class Astrolabe<T extends AstrolabeBlockEntity> extends AstromancyEntityBlock<T> {
    public Astrolabe(Properties properties) {
        super(properties);
    }
}
