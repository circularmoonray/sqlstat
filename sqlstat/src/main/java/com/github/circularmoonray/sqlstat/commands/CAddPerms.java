package com.github.circularmoonray.sqlstat.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.github.circularmoonray.sqlstat.SqlStat;

public class CAddPerms implements TabExecutor {
	SqlStat plugin;

	public CAddPerms(SqlStat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		// オプションの判定
		if (args.length > 1) {
			sender.sendMessage("引数は1つにまでにして下さい");
			return false;

			// configのリロードオプション
		} else if (args.length == 1) {

			if (args[0].equalsIgnoreCase("add")) {
				PermissionAttachment attachment = sender.addAttachment(plugin);
				attachment.setPermission("worldedit.*", true);
				sender.sendMessage("add completed");
				return true;

			}else if (args[0].equalsIgnoreCase("remove")) {
				PermissionAttachment attachment = sender.addAttachment(plugin);
				attachment.setPermission("worldedit.*", false);
				sender.removeAttachment(attachment);
				sender.sendMessage("remove completed");
				return true;

			}else if (args[0].equalsIgnoreCase("list")) {
				sender.sendMessage("change name");
				Player player = (Player) sender;
				player.setDisplayName("&b" + sender.getName() + "&f");
				return true;

			}
		}

		return false;

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {

		return null;

	}

}
