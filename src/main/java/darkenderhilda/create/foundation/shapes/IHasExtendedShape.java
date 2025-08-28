package darkenderhilda.create.foundation.shapes;

import net.minecraft.block.state.IBlockState;

public interface IHasExtendedShape {

    ExtendedShape getShape(IBlockState state);

}
