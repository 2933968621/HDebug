package net.blueheart.hdebug.utils.astar

import net.blueheart.hdebug.utils.block.BlockUtils.isBlockPassable
import net.blueheart.hdebug.utils.extensions.getBlock
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

class NaiveAstarFlyNode(
    override val x: Int,
    override val y: Int,
    override val z: Int,
    override var parentNode: NaiveAstarNode? = null
) : NaiveAstarNode(x, y, z, parentNode) {
    override fun neighbors(): ArrayList<AstarNode> {
        var neighborPoss = mutableListOf<Vec3i>(
            Vec3i(x + 1, y, z)
            , Vec3i(x - 1, y, z)
            , Vec3i(x, y + 1, z)
            , Vec3i(x, y - 1, z)
            , Vec3i(x, y, z + 1)
            , Vec3i(x, y, z - 1)
        )

        neighborPoss = neighborPoss.filter { it -> isBlockPassable(BlockPos(it).getBlock()) } as MutableList<Vec3i>
        neighborPoss = neighborPoss.filter { it ->
            isBlockPassable(
                BlockPos(
                    it.x,
                    it.y + 1,
                    it.z
                ).getBlock()
            )
        } as MutableList<Vec3i>

        var arrayList = ArrayList<AstarNode>()

        neighborPoss.forEach { arrayList.add(NaiveAstarFlyNode(it.x, it.y, it.z, this)) }

        return arrayList
    }


    override fun equals(p: AstarNode): Boolean {
        return super.equals(p)
    }

}