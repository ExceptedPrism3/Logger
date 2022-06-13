package me.prism3.logger.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventoryToBase64 {

    private InventoryToBase64() {}

    public static String toBase64(ItemStack[] contents) {
        boolean convert = false;

        for (ItemStack item : contents) {
            if (item != null) {
                convert = true;
                break;
            }
        }

        if (convert) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeInt(contents.length);

                for (ItemStack stack : contents) {
                    dataOutput.writeObject(stack);
                }

                dataOutput.close();

                byte[] byteArr = outputStream.toByteArray();

                return org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder.encodeLines(byteArr);
            } catch (Exception e) {
                throw new IllegalStateException("An error has occurred whilst saving the Player Inventory. Does the proper file exists? If the issue persists, contact the Authors!", e);
            }
        }

        return null;
    }

    public static ItemStack[] stacksFromBase64(String data) {

        if (data == null) return new ItemStack[]{};

        ByteArrayInputStream inputStream;

        try {
            inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        } catch (IllegalArgumentException e) {
            return new ItemStack[]{};
        }

        BukkitObjectInputStream dataInput = null;
        ItemStack[] stacks = null;

        try {
            dataInput = new BukkitObjectInputStream(inputStream);
            stacks = new ItemStack[dataInput.readInt()];
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (stacks == null) return new ItemStack[]{};

        for (int i = 0; i < stacks.length; i++) {
            try {
                stacks[i] = (ItemStack) dataInput.readObject();
            } catch (IOException | ClassNotFoundException | NullPointerException e) {

                try { dataInput.close(); } catch (IOException ignored) {}

                return new ItemStack[0];
            }
        }

        try { dataInput.close(); } catch (IOException ignored) {}

        return stacks;
    }
}