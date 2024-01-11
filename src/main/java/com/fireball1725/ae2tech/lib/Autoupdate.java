package com.fireball1725.ae2tech.lib;

import com.fireball1725.ae2tech.AE2Tech;
import com.fireball1725.ae2tech.util.LogHelper;
import com.google.gson.Gson;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Autoupdate {
    public static boolean CheckForUpdates() {
        try {
            LogHelper.info("Checking for new updates");
            String json = readUrl("http://autoupdate.tsr.me/?id=" + Reference.AUTOUPDATE_ID);

            Gson gson = new Gson();
            UpdateInfoPackage updateInfoPackage = gson.fromJson(json, UpdateInfoPackage.class);

            if (Integer.parseInt(updateInfoPackage.buildnumber) > Integer.parseInt(Reference.VERSION_BUILD_NUMBER)) {
                LogHelper.info("New update detected");
                return true;
            }
            LogHelper.info("No new updates detected");
            return false;
        } catch (Exception e) {
            if (AE2Tech.LogDebug) {
                LogHelper.info("In debug mode, simulating update...");
                return true;
            }
            LogHelper.error(e.getStackTrace());
            return false;
        }
    }

    public static String GetUpdateVersion() {
        try {
            String json = readUrl("http://autoupdate.tsr.me/?id=" + Reference.AUTOUPDATE_ID);

            Gson gson = new Gson();
            UpdateInfoPackage updateInfoPackage = gson.fromJson(json, UpdateInfoPackage.class);

            return updateInfoPackage.buildnumber;
        } catch (Exception e) {
            LogHelper.error(e.getStackTrace());
            return "";
        }
    }

    public static NBTTagCompound GetUpdateDetails() {
        try {
            String json = readUrl("http://autoupdate.tsr.me/?id=" + Reference.AUTOUPDATE_ID);

            Gson gson = new Gson();
            UpdateInfoPackage updateInfoPackage = gson.fromJson(json, UpdateInfoPackage.class);

            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setString("modDisplayName", Reference.MOD_NAME);
            nbtTagCompound.setString("oldVersion", "Build #: " + Reference.VERSION_BUILD_NUMBER);
            nbtTagCompound.setString("newVersion", "Build #: " + updateInfoPackage.buildnumber);
            nbtTagCompound.setString("updateUrl", updateInfoPackage.buildfileurl);
            nbtTagCompound.setBoolean("isDirectLink", true);
            nbtTagCompound.setString("changeLog", updateInfoPackage.buildchangelog);
            nbtTagCompound.setString("newFileName", updateInfoPackage.buildfilename);

            return nbtTagCompound;
        } catch (Exception e) {
            LogHelper.error(e.getStackTrace());
            return null;
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    static class UpdateInfoPackage {
        String buildnumber;
        String buildfilename;
        String buildfileurl;
        String buildchangelog;
    }
}