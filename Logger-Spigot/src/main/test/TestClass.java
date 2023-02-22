package test;

import be.seeseemelk.mockbukkit.Coordinate;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.ItemEntityMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.prism3.logger.Main;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestClass {
    private ServerMock server;
    private Main plugin;
    private PlayerMock playerMock;

    private WorldMock world;
    @BeforeEach
    public void setUp() {
        // Start the mock server
        this.server = MockBukkit.mock();

        System.out.println(this.server.getBukkitVersion());
        // Load your plugin
        this.plugin = MockBukkit.load(Main.class);

        this.world = new WorldMock(Material.DIRT, 100);
        this.server.setPlayers(2);
        this.playerMock = new PlayerMock(server, "TestPlayer", UUID.randomUUID());



    }

    @AfterEach
    public void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    public void playerChat()
    {
        // generate random string for testing
        Random rand = new Random();
        String randomString = "";
        for(int i = 0; i < 10; i++)
        {
            randomString += (char) (rand.nextInt(26) + 'a');
        }
        this.playerMock.chat(randomString);
    }
    @Test
    public void blockBreak(){
        Block a = this.world.createBlock(new Coordinate(0, 0, 0));
        this.playerMock.simulateBlockBreak(a);
        this.server.getPluginManager().assertEventFired(BlockBreakEvent.class, e -> {
            return true;
        });

    }
    @Test
    public void blockPlace(){
        Block a = this.world.createBlock(new Coordinate(0, 0, 0));
        this.playerMock.simulateBlockPlace(Material.ACACIA_LOG, new Location(this.world,0, 0, 1));
        this.server.getPluginManager().assertEventFired(BlockPlaceEvent.class, e -> {
            return true;
        });
    }
    @Test
    public void blockStrip(){    }

    @Test
    public void gameMode(){
        this.playerMock.setGameMode(GameMode.SPECTATOR);
        this.playerMock.assertGameMode(GameMode.SPECTATOR);



    }

    @Test
    public void playerConnection(){
            PlayerMock testPlayer = new PlayerMock(this.server, "testPlayer" + new Random().nextInt(),  UUID.randomUUID());
            testPlayer.disconnect();
            this.server.getPluginManager().assertEventFired(PlayerQuitEvent.class, e -> {
            return true;
        });

    }
    @Test
    public void itemEvent(){

        this.server.getPluginManager().assertEventFired(EntityPickupItemEvent.class, e -> {
            return true;
        });

    }
    @Test
    public void playerCommand(){
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(playerMock, "/logger discord");
        assert event != null;
        event.callEvent();
        this.server.getPluginManager().assertEventFired(PlayerCommandPreprocessEvent.class, e -> {
            return true;
        });

    }

    @Test
    public void craft(){
        //get me a hash map of enchantements in minecraft
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantment.KNOCKBACK, 1);
        enchants.put(Enchantment.DAMAGE_ALL, 5);
        enchants.put(Enchantment.BINDING_CURSE, 1);
        EnchantItemEvent event = new EnchantItemEvent(this.playerMock, this.playerMock.getOpenInventory(), this.playerMock.simulateBlockPlace(Material.ENCHANTING_TABLE, new Location(this.playerMock.getWorld(),0,0,0)).getBlockPlaced(), new ItemStack(Material.DIAMOND_SWORD, 1), 30, enchants, 3);
        event.callEvent();
    }
    @Test
    public void xpEvent(){
        this.playerMock.giveExpLevels(10);
        this.playerMock.giveExpLevels(-10);
    }

    @Test
    public void console(){
        ServerCommandEvent event = new ServerCommandEvent((CommandSender) this.server.getConsoleSender(), "logger discord");
        assert event != null;
        event.callEvent();
        this.server.getPluginManager().assertEventFired(ServerCommandEvent.class, e -> {
            return true;
        });
    }
    @Test
    public void sign(){
        Block sign = this.world.createBlock(new Coordinate(10, 20, 10));
        sign.setType(Material.ACACIA_WALL_SIGN);
        SignChangeEvent event = new SignChangeEvent(sign, this.playerMock, new String[] {"Line Num 1", "Player " + this.playerMock.getName(), "Line 3", "Last Line"} );
        assert event != null;
        event.callEvent();
        this.server.getPluginManager().assertEventFired(SignChangeEvent.class, e -> {
            return true;
        });
    }
    @Test
    public void playerDeath(){
        this.server.getPlayer(0).damage(999999, this.server.getPlayer(1));
        this.server.getPluginManager().assertEventFired(PlayerDeathEvent.class, e -> {
            return true;
        });

    }
    @Test
    public void loggerTest() {
        int i = 1;

        while(i++ != 5)
        {
            this.blockPlace();
            this.blockBreak();
            this.playerChat();
            // this.gameMode();
            this.console();

        }
        this.playerConnection();



    }
}