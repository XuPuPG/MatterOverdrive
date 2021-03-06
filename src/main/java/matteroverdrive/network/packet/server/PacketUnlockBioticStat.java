/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 5/29/2015.
 */
public class PacketUnlockBioticStat extends PacketAbstract
{
    String name;
    int level;

    public PacketUnlockBioticStat()
    {

    }

    public PacketUnlockBioticStat(String name, int level)
    {
        this.name = name;
        this.level = level;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);
        level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf,name);
        buf.writeInt(level);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketUnlockBioticStat>
    {
        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketUnlockBioticStat message, MessageContext ctx)
        {
            IBionicStat stat = MatterOverdrive.statRegistry.getStat(message.name);
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (stat != null && androidPlayer != null && androidPlayer.isAndroid())
            {
                androidPlayer.tryUnlock(stat, message.level);
            }
            return null;
        }
    }
}
