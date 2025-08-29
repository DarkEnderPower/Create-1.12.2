package darkenderhilda.create.foundation.shapes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public interface IHasExtendedShape {

    List<AxisAlignedBB> getShape(IBlockState state);

}
