package me.prism3.logger.utils.playerdeathutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import me.prism3.logger.utils.Log;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class InventoryToBase64 {
    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public static String toBase64(final ItemStack[] contents) throws IOException {

        try (final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(contents.length);

            for (ItemStack stack : contents)
                dataOutput.writeObject(stack);

            final byte[] byteArr = outputStream.toByteArray();
            outputStream.reset();

            return Base64.getEncoder().encodeToString(byteArr);
        }
    }

    public static ItemStack[] fromBase64(final String data) {

        ItemStack[] stacks = null;

        try (final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data)))) {

            stacks = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < stacks.length; i++)
                stacks[i] = (ItemStack) dataInput.readObject();

        } catch (final IOException | ClassNotFoundException e) {
            Log.severe("An error has occurred while retrieving data from the backup file. If the issue persists, contact the authors.");
            e.printStackTrace();
        }

        return stacks;
    }
}
