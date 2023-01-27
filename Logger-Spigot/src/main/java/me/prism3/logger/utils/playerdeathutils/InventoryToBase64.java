package me.prism3.logger.utils.playerdeathutils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventoryToBase64 {

    private static final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public static String toBase64(ItemStack[] contents) {

        try (final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(contents.length);

            for (ItemStack stack : contents)
                dataOutput.writeObject(stack);

            byte[] byteArr = outputStream.toByteArray();
            outputStream.reset();
            return Base64Coder.encodeLines(byteArr);
        } catch (final IOException e) {
            throw new IllegalStateException("An error has occurred whilst saving the Player Inventory." +
                    " Does the proper file exists? If the issue persists, contact the Authors!", e);
        }
    }

    public static ItemStack[] stacksFromBase64(String data) {

        try (final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(new ByteArrayInputStream(Base64Coder.decodeLines(data)))) {

            final ItemStack[] stacks = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < stacks.length; i++)
                stacks[i] = (ItemStack) dataInput.readObject();

            return stacks;
        } catch (final IOException | ClassNotFoundException e) {
            throw new IllegalStateException("An error has occurred whilst loading the Player Inventory." +
                    " Does the proper file exists? If the issue persists, contact the Authors!", e);
        }
    }
}
