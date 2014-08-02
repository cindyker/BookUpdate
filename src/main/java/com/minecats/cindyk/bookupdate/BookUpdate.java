package com.minecats.cindyk.bookupdate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sun.net.www.content.text.plain;

import java.util.Date;

/**
 * Created by cindy on 8/2/14.
 */
public class BookUpdate extends JavaPlugin implements Listener {

    static BookUpdate plugin;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        getServer().getPluginManager().registerEvents(this,this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void editBook(PlayerEditBookEvent event)
    {
        plugin.getLogger().info("editBook: " + event.getPlayer().getName()+ " in Slot " + event.getSlot());
        plugin.getLogger().info("Meta Page Count " + event.getNewBookMeta().getPageCount());
        for(String page:event.getNewBookMeta().getPages())
            plugin.getLogger().info("Page: " + page);

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player =  event.getPlayer();

        if((event.getAction().equals(Action.RIGHT_CLICK_AIR))||(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) // this works fine i tested it
        {
            if(player.getInventory().getItemInHand().getType()==Material.WRITTEN_BOOK)//it is this line of code
            {
                ItemStack is = event.getPlayer().getItemInHand();
                BookMeta bm = (BookMeta)is.getItemMeta();
                bm = UpdateBook(bm, event.getPlayer());
                if(bm != null )
                    is.setItemMeta(bm);
                else
                    plugin.getLogger().info("[BookUpdate] Book Meta was Null");

            }
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void itemHeld(PlayerItemHeldEvent event)
    {

        ItemStack Check = event.getPlayer().getInventory().getContents()[event.getNewSlot()];
        if(Check != null)
        {
            if( Check.getType() == Material.MINECART.WRITTEN_BOOK)
            {
                plugin.getLogger().info("Found a Written Book in slot " + event.getNewSlot() );

                BookMeta bm = (BookMeta)Check.getItemMeta();
                bm = UpdateBook(bm, event.getPlayer());
                if(bm != null )
                    Check.setItemMeta(bm);
                else
                    plugin.getLogger().info("[BookUpdate] Book Meta was Null");
            }
        }

    }

    //Update the Book Meta Function.
    BookMeta UpdateBook(BookMeta bm, Player pl)
    {
        if(bm == null) return bm;
        if(pl == null) return bm;

        if(bm.getAuthor().equalsIgnoreCase("cindy_k") && bm.getTitle().equalsIgnoreCase("Stats"))
        {
            plugin.getLogger().info("Holding a BOOK! " + bm.getTitle());
            Date d = new Date();
            String p = "Hi "+pl.getName() + ", It is NOW: " + d.toString();
            if(bm.hasPages() && bm.getPageCount() > 0)
                bm.setPage(1,p);
            else
                bm.addPage(p);

        }
        return bm;
    }

}
